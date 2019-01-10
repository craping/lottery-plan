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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import net.sf.json.JSONArray;
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
	
	@Pipe("info")
	@BarScreen(
		desc="查看具体方案详情",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="key")
		}
	)
	public Errcode info (JSONObject params) {
		JSONObject info = JSONObject.fromObject(redisTemplate.opsForHash().get("plan_current", 
				params.optString("key")));
		return new DataResult(Errors.OK, new Data(info));
	}
	
	@Pipe("history")
	@BarScreen(
		desc="查看具体方案历史记录",
		params= {
			@Parameter(value="key"),
			@Parameter(value="count",  desc="查询数量")
		}
	)
	public Errcode history (JSONObject params) {
		List<String> result = redisTemplate.opsForList().range("plan_history_" + params.optString("key"), 0, params.optLong("count")-1);
		@SuppressWarnings("unchecked")
		List<JSONArray> l = JSONArray.fromObject(result);
		return new DataResult(Errors.OK, new Data(l));
	}
	
	@Pipe("search")
	@BarScreen(
		desc="筛选计划",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="lottery",  desc="彩种", required=false),
			@Parameter(value="type",  desc="计划玩法 DWD DX DS", required=false),
			@Parameter(value="position",  desc="方案位置", required=false),
			@Parameter(value="plan_count",  desc="计划投注期数2,3期计划", required=false),
			@Parameter(value="count",  desc="查询数量"),
			@Parameter(value="rate", desc="胜率计算数")
		}
	)
	public Errcode search (JSONObject params) {
		if (!redisTemplate.hasKey("plan_current"))
			return new DataResult(CustomErrors.PLAN_OPR_ERR);
		
		// 模糊搜索条件key 拼接
		String pattern = "";
		if (!Tools.isStrEmpty(params.optString("lottery")))
			pattern = params.optString("lottery") + "_";
		if (!Tools.isStrEmpty(params.optString("type")))
			pattern = pattern + params.optString("type") + "_"; 
		if (!Tools.isStrEmpty(params.optString("position")))
			pattern = pattern + params.optString("position") + "_"; 
		if (!Tools.isStrEmpty(params.optString("plan_count")))
			pattern = pattern + params.optString("plan_count") + "_*";
		
		List<JSONObject> data = new ArrayList<>(); // 返回结果
		long count = params.optLong("count"); 
		double rate = params.optDouble("rate"); 
		ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
		Cursor<Map.Entry<Object, Object>> curosr = redisTemplate.opsForHash().scan("plan_current", options);
        while(curosr.hasNext()){
            Map.Entry<Object, Object> entry = curosr.next();
            JSONObject plan = JSONObject.fromObject(entry.getValue());
            
            // 查询历史记录 计算胜率 最大连胜数
            List<String> history = redisTemplate.opsForList().range("plan_history_" + plan.optString("key"), 0, count-1);
            int idx = 0; 	  
			int err_idx = 0;  // 未中奖次数
			int win_idx = 0;  // 连胜次数
			List<Integer> win_idx_list = new ArrayList<>(); // 连胜次数集合
			for (int i = 0; i < history.size(); i++) {
				idx++;
				JSONObject his_plan = JSONObject.fromObject(history.get(i));
				if (his_plan.getBoolean("win")) {
					win_idx++;
					win_idx_list.add(win_idx);
				} else {
					err_idx++;
					win_idx_list.add(win_idx);
					win_idx = 0; // 连胜次数复位
				}
			}
			
			// 胜率
			double win_rate = 0;
			if (Arith.sub(idx, err_idx) > 0)
				win_rate = Arith.div(Arith.sub(idx, err_idx), idx);
			plan.put("win_rate", win_rate);
			
			// 最大连胜次数
			plan.put("win_num", win_idx_list.size());
			if (win_idx_list.size() > 0) {
				Collections.sort(win_idx_list);
				plan.put("win_num", win_idx_list.get(win_idx_list.size()-1));
			}
			
			// 最近战绩
			plan.put("grade", idx + ":" + (idx-err_idx));
			if (win_rate >= rate)
				data.add(plan);
        }
		return new DataResult(Errors.OK, new Data(data));
	}
	
	@Pipe("save")
	@BarScreen(
		desc="保存计划",
		params= {}
	)
	public Errcode save (JSONObject params) {
		// TODO: 加入ip限制 绑定
		return new DataResult(Errors.OK);
	}
}