package plan.server.pump.admin;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import net.sf.json.JSONObject;

@Pump("admin_plan")
@Component
public class AdminPlanPump extends DataPump<JSONObject, FullHttpRequest, Channel> {
	
	public static final Logger log = LogManager.getLogger(AdminPlanPump.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Pipe("list")
	@BarScreen(
		desc="计划列表"
	)
	public Errcode list (JSONObject params) {
		Map<Object, Object> current_plan = redisTemplate.opsForHash().entries("plan_current");
		List<JSONObject> result = new ArrayList<>();
		current_plan.forEach((key, value) -> {
			result.add(JSONObject.fromObject(value));
		});
		return new DataResult(Errors.OK, new Data(result));
	}	
	
	@Pipe("history")
	@BarScreen(
		desc="历史计划列表"
	)
	public Errcode history (JSONObject params) {
		Map<Object, Object> current_plan = redisTemplate.opsForHash().entries("plan_current");
		List<JSONObject> result = new ArrayList<>();
		current_plan.forEach((key, value) -> {
			result.add(JSONObject.fromObject(value));
		});
		return new DataResult(Errors.OK, new Data(result));
	}
}