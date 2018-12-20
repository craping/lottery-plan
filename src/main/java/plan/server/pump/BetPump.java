package plan.server.pump;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.jrain.core.asm.annotation.Pipe;
import org.crap.jrain.core.asm.annotation.Pump;
import org.crap.jrain.core.asm.handler.DataPump;
import org.crap.jrain.core.bean.result.Errcode;
import org.crap.jrain.core.bean.result.criteria.DataResult;
import org.crap.jrain.core.error.support.Errors;
import org.crap.jrain.core.validate.annotation.BarScreen;
import org.crap.jrain.core.validate.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import plan.lottery.biz.server.BettingServer;
import plan.lottery.common.param.TokenParam;
import plan.lottery.utils.Tools;

@Pump("bet")
@Component
public class BetPump extends DataPump<JSONObject> {
	
	public static final Logger log = LogManager.getLogger(BetPump.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private BettingServer bettingServer;
	
	@Pipe("betting")
	@BarScreen(
		desc="用户投注",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="lottery_type",  desc="彩票类型"),
			@Parameter(value="bet_type",  desc="投注类型"),
			@Parameter(value="bet_period",  desc="投注期号"),
			@Parameter(value="bet_schema",  desc="投注方案"),
			@Parameter(value="bet_position",  desc="方案位置"),
			@Parameter(value="bet_amount",  desc="投注金额"),
			@Parameter(value="bet_rate",  desc="赔率")
		}
	)
	public Errcode betting (JSONObject params) {
		// redis key : user_betting_720956
		String key = "user_betting_" + params.getString("bet_period"); 
		try {
			Map<String, String> info = new HashMap<>();
			info.put("lottery_type", params.getString("lottery_type"));
			info.put("bet_type", params.getString("bet_type"));
			info.put("bet_period", params.getString("bet_period"));
			info.put("bet_schema", params.getString("bet_schema"));
			info.put("bet_position", params.getString("bet_position"));
			info.put("bet_amount", params.getString("bet_amount"));
			info.put("bet_time", Tools.getSysTime());
			info.put("bet_rate", params.getString("bet_rate"));
			info.put("token", params.getString("token"));
			redisTemplate.opsForList().leftPush(key, JSONObject.fromObject(info).toString());
			return new DataResult(Errors.OK);
		} catch (Exception ex) {
			log.error("bet/betting 操作异常:", ex);
			return new DataResult(Errors.EXCEPTION_UNKNOW);
		} 
	}
}