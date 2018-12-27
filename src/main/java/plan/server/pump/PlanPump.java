package plan.server.pump;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.jrain.core.asm.annotation.Pipe;
import org.crap.jrain.core.asm.annotation.Pump;
import org.crap.jrain.core.asm.handler.DataPump;
import org.crap.jrain.core.bean.result.Errcode;
import org.crap.jrain.core.bean.result.criteria.Data;
import org.crap.jrain.core.bean.result.criteria.DataResult;
import org.crap.jrain.core.error.support.Errors;
import org.crap.jrain.core.validate.annotation.BarScreen;
import org.crap.jrain.core.validate.annotation.Parameter;
import org.crap.jrain.core.validate.security.component.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import net.sf.json.JSONObject;
import plan.lottery.common.CustomErrors;
import plan.lottery.common.param.TokenParam;
import plan.lottery.utils.Arith;
import plan.lottery.utils.Tools;

@Pump("plan")
@Component
public class PlanPump extends DataPump<JSONObject, FullHttpRequest, Channel> {
	
	public static final Logger log = LogManager.getLogger(PlanPump.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Pipe("getHistory")
	@BarScreen(
		desc="查看具体方案历史记录",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="name",  desc="计划名称"),
			@Parameter(value="type",  desc="计划玩法 DWD DX DS"),
			@Parameter(value="position",  desc="方案位置", required=false),
			@Parameter(value="plan_count",  desc="计划投注期数2,3期计划"),
			@Parameter(value="count",  desc="查询数量")
		}
	)
	public Errcode getHistory (JSONObject params) {
		List<JSONObject> result = new ArrayList<>(); // 查询结果
		Map<Object, Object> history_plan = redisTemplate.opsForHash().entries("plan_history");
		if (history_plan == null || history_plan.isEmpty())
			return new DataResult(Errors.OK, new Data(result));
		
		// 模糊搜索条件key 拼接
		String current_plan_key = params.getString("type") + "_"; 
		if (!Tools.isStrEmpty(params.optString("position")))
			current_plan_key += (params.getString("position") + "_");
		current_plan_key = current_plan_key + params.getString("plan_count") + "_" + Coder.encryptMD5(params.getString("name"));
		
		for (Object key : history_plan.keySet()) {
			if (result.size() >= params.getInt("count")) 
				break;
			
			if (key.toString().contains(current_plan_key)) 
				result.add(JSONObject.fromObject(history_plan.get(key)));
		}
		return new DataResult(Errors.OK, new Data(result));
	}
	
	@Pipe("search")
	@BarScreen(
		desc="筛选计划",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="type",  desc="计划玩法 DWD DX DS"),
			@Parameter(value="position",  desc="方案位置", required=false),
			@Parameter(value="plan_count",  desc="计划投注期数2,3期计划"),
			@Parameter(value="count",  desc="查询数量"),
			@Parameter(value="rate", desc="胜率计算数")
		}
	)
	public Errcode search (JSONObject params) {
		Map<Object, Object> current_plan = redisTemplate.opsForHash().entries("plan_current");
		if (current_plan == null || current_plan.isEmpty())
			return new DataResult(CustomErrors.PLAN_OPR_ERR);
		
		// 模糊搜索条件key 拼接
		String current_plan_key = params.getString("type") + "_"; 
		if (!Tools.isStrEmpty(params.optString("position")))
			current_plan_key += (params.getString("position") + "_");
		current_plan_key = current_plan_key + params.getString("plan_count") + "_";
		
		List<JSONObject> result = new ArrayList<>(); // 匹配结果集
		for (Object key : current_plan.keySet()) {
			if (result.size() >= params.getInt("count"))
				break;
			
			if (key.toString().contains(current_plan_key)) {
				// 计算胜率
				Map<Object, Object> history_plan = redisTemplate.opsForHash().entries("plan_history"); 
				JSONObject current_json = JSONObject.fromObject(current_plan.get(key));
				String flag_current_key = current_plan_key + Coder.encryptMD5(current_json.getString("name"));
				
				int idx = 0; 	  
				int err_idx = 0;  // 未中奖次数
				int win_idx = 0;  // 连胜次数
				List<Integer> win_idx_list = new ArrayList<>(); // 连胜次数集合
				for (Object history_key : history_plan.keySet()) {
					if (idx >= params.getInt("rate"))
						break;
					if (history_key.toString().contains(flag_current_key)) {
						idx++;
						JSONObject history_json = JSONObject.fromObject(history_plan.get(history_key));
						if (history_json.getString("win").equals("1")) {
							win_idx++;
							win_idx_list.add(win_idx);
						} else {
							err_idx++;
							win_idx_list.add(win_idx);
							win_idx = 0; // 连胜次数复位
						}
					}
				}
				
				double win_rate = 0;
				if (Arith.sub(idx, err_idx) > 0)
					win_rate = Arith.div(Arith.sub(idx, err_idx), idx);
				current_json.put("win_rate", win_rate);
				
				if (win_idx_list.size() > 0) {
					Collections.sort(win_idx_list);
					current_json.put("win_num", win_idx_list.get(win_idx_list.size()-1));
				}
				result.add(current_json);
			}
		}
		return new DataResult(Errors.OK, new Data(result));
	}
	
	@Pipe("save")
	@BarScreen(
		desc="保存计划",
		params= {
			@Parameter(value="name",  desc="计划名称"),
			@Parameter(value="type",  desc="计划玩法 DWD DX DS"),
			@Parameter(value="plan_count",  desc="计划投注期数2,3期计划"),
			@Parameter(value="period",  desc="计划期号111-113"),
			@Parameter(value="position",  desc="方案位置", required=false),
			@Parameter(value="schema",  desc="计划号码"),
			@Parameter(value="pre_result",  desc="上期开奖结果", required=false),
			@Parameter(value="pre_period",  desc="上期中奖期号", required=false),
			@Parameter(value="pre_win",  desc="上期状态1中奖0未中奖2等待", required=false)
		}
	)
	public Errcode save (JSONObject params) {
		// TODO: 加入ip限制 绑定
		String name = params.getString("name");
		JSONObject plan_info = new JSONObject();
		plan_info.put("name", name);
		plan_info.put("plan_type", params.getString("type"));
		plan_info.put("plan_count", params.getString("plan_count"));
		plan_info.put("plan_schema", params.getString("schema"));
		plan_info.put("period", params.getString("period"));
		plan_info.put("position", params.optString("position", ""));
		plan_info.put("create_time", Tools.getSysTimeFormat("yyyy-MM-dd HH:mm:ss"));
		plan_info.put("win_result", "");
		plan_info.put("win_period", "");
		plan_info.put("win", "");
		
		String current_key = "plan_current"; // 执行中计划key
		String history_key = "plan_history"; // 历史计划key
		String current_plan_key = params.getString("type") + "_"; // 当前操作计划key
		if (!Tools.isStrEmpty(params.optString("position")))
			current_plan_key += (params.getString("position") + "_");
		current_plan_key = current_plan_key + params.getString("plan_count") + "_" + Coder.encryptMD5(name);
		
		// 先处理上一期计划 更新 中奖状态 开奖结果 记录中奖期 
		Map<Object, Object> current_plan = redisTemplate.opsForHash().entries(current_key);
		String pre_plan = (String) current_plan.get(current_plan_key); 
		if (!Tools.isStrEmpty(pre_plan)) {
			JSONObject obj = JSONObject.fromObject(pre_plan);
			obj.put("win", params.optString("pre_win", "0"));
			obj.put("win_period", params.optString("pre_period", ""));
			obj.put("win_result", params.optString("pre_result", ""));
			// 保存到计划历史记录
			redisTemplate.opsForHash().put(history_key, current_plan_key+"_"+obj.getString("period"), obj.toString());
		}
		
		// 更新覆盖当前计划
		redisTemplate.opsForHash().put(current_key, current_plan_key, plan_info.toString());
		return new DataResult(Errors.OK);
	}
}