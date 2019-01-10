package plan.server.pump;

import java.util.ArrayList;
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
import plan.data.sql.entity.LotteryUserSetting;
import plan.lottery.biz.server.UserSettingServer;
import plan.lottery.common.CustomErrors;
import plan.lottery.common.param.TokenParam;
import plan.lottery.utils.Tools;

@Pump("setting")
@Component
public class UserSettingPump extends DataPump<JSONObject, FullHttpRequest, Channel> {
	
	public static final Logger log = LogManager.getLogger(UserSettingPump.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private UserSettingServer settingServer;
	
	@Pipe("save")
	@BarScreen(
		desc="保存用户配置",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="name",  desc="用户配置名称"),
			@Parameter(value="lottery_type",  desc="彩票类型"),
			@Parameter(value="start_money",  desc="初始金额"),
			@Parameter(value="chase_max_num",  desc="最大追号"),
			@Parameter(value="chase_mode",  desc="追号方式"),
			@Parameter(value="bet_mode",  desc="投注模式"),
			@Parameter(value="rate",  desc="平台赔率"),
			@Parameter(value="min_multiple",  desc="最小倍数"),
			@Parameter(value="max_multiple",  desc="最大倍数"),
			@Parameter(value="stop_lose",  desc="止损金额"),
			@Parameter(value="stop_win",  desc="止盈金额"),
			@Parameter(value="plan_name",  desc="方案名称"),
			@Parameter(value="bet_type",  desc="计划玩法"),
			@Parameter(value="position",  desc="计划位置"),
			@Parameter(value="bet_count",  desc="方案期数"),
		}
	)
	public Errcode save(JSONObject params) {
		// 获取用户信息
		String key = "user_" + params.getString("token");
		Integer uid = (Integer) redisTemplate.opsForHash().get(key, "uid");
		
		// 保存计划
		LotteryUserSetting setting = new LotteryUserSetting();
		setting.setUid(uid);
		//setting = ClassUtil.fillObject((Map)params, setting);
		settingServer.save(setting);
		
		// 更新缓存
		String setting_key = "setting" + setting.getId() + "_" + Coder.encryptMD5(setting.getName());
		redisTemplate.opsForHash().put(key, setting_key, JSONObject.fromObject(setting).toString());
		return new DataResult(Errors.OK, new Data(setting)); 
	}
	
	@Pipe("update")
	@BarScreen(
		desc="更新用户配置",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="id",  desc="配置ID"),
			@Parameter(value="name",  desc="用户配置名称"),
			@Parameter(value="old_name",  desc="上一次用户配置名称", required=false),
			@Parameter(value="lottery_type",  desc="彩票类型"),
			@Parameter(value="start_money",  desc="初始金额"),
			@Parameter(value="chase_max_num",  desc="最大追号"),
			@Parameter(value="chase_mode",  desc="追号方式"),
			@Parameter(value="bet_mode",  desc="投注模式"),
			@Parameter(value="rate",  desc="平台赔率"),
			@Parameter(value="min_multiple",  desc="最小倍数"),
			@Parameter(value="max_multiple",  desc="最大倍数"),
			@Parameter(value="stop_lose",  desc="止损金额"),
			@Parameter(value="stop_win",  desc="止盈金额"),
			@Parameter(value="plan_name",  desc="方案名称"),
			@Parameter(value="bet_type",  desc="计划玩法"),
			@Parameter(value="position",  desc="计划位置"),
			@Parameter(value="bet_count",  desc="方案期数"),
		}
	)
	public Errcode update(JSONObject params) {
		String key = "user_" + params.getString("token"); // 用户key
		Integer uid = Integer.parseInt(redisTemplate.opsForHash().get(key, "uid").toString());
		// 保存计划
		LotteryUserSetting setting = new LotteryUserSetting();
		setting.setUid(uid);
		//setting = ClassUtil.fillObject((Map)params, setting);
		if (settingServer.update(setting) == 0)
			return new DataResult(CustomErrors.USER_OPR_ERR);
		
		// 如果更改计划名称 则删除老的缓存
		Integer id = params.getInt("id"); 
		if (!Tools.isStrEmpty(params.optString("old_name"))) 
			redisTemplate.opsForHash().delete(key, "setting" + id + "_" + Coder.encryptMD5(params.getString("old_name")));
		
		// 更新缓存
		String setting_key = "setting" + id + "_" + Coder.encryptMD5(setting.getName());
		redisTemplate.opsForHash().put(key, setting_key, JSONObject.fromObject(setting).toString());
		return new DataResult(Errors.OK); 
	}
	
	@Pipe("list")
	@BarScreen(
		desc="获取用户配置列表",
		params= {
			@Parameter(type=TokenParam.class),
		}
	)
	public Errcode list (JSONObject params) {
		List<JSONObject> result = new ArrayList<>();
		// 获取用户信息
		String user_key = "user_" + params.getString("token");
		Map<Object, Object> userMap = redisTemplate.opsForHash().entries(user_key);
		userMap.forEach((key, value) -> {
			if (key.toString().contains("setting")) 
				result.add(JSONObject.fromObject(value));
		});
		return new DataResult(Errors.OK, new Data(result)); 
	}
	
	@Pipe("info")
	@BarScreen(
		desc="查看计划详细",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="id",  desc="方案id"),
			@Parameter(value="name",  desc="方案名称")
		}
	)
	public Errcode info (JSONObject params) {
		String user_key = "user_" + params.getString("token");
		String setting_key = "setting" + params.getString("id") + "_" + Coder.encryptMD5(params.getString("name"));
		String value = (String) redisTemplate.opsForHash().get(user_key, setting_key);
		return new DataResult(Errors.OK, new Data(JSONObject.fromObject(value))); 
	}
	
	@Pipe("del")
	@BarScreen(
		desc="删除计划",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="id",  desc="方案id"),
			@Parameter(value="name",  desc="方案名称")
		}
	)
	public Errcode del (JSONObject params) {
		settingServer.delete(params.getInt("id"));
		String user_key = "user_" + params.getString("token");
		String setting_key = "setting" + params.getString("id") + "_" + Coder.encryptMD5(params.getString("name"));
		redisTemplate.opsForHash().delete(user_key, setting_key);
		return new DataResult(Errors.OK);
	}
}