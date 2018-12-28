package plan.server.pump.admin;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.jrain.core.asm.annotation.Pipe;
import org.crap.jrain.core.asm.annotation.Pump;
import org.crap.jrain.core.asm.handler.DataPump;
import org.crap.jrain.core.bean.result.Errcode;
import org.crap.jrain.core.bean.result.Result;
import org.crap.jrain.core.bean.result.criteria.Data;
import org.crap.jrain.core.bean.result.criteria.DataResult;
import org.crap.jrain.core.error.support.Errors;
import org.crap.jrain.core.util.StringUtil;
import org.crap.jrain.core.validate.annotation.BarScreen;
import org.crap.jrain.core.validate.annotation.Parameter;
import org.crap.jrain.core.validate.security.component.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryAdmin;
import plan.data.sql.entity.LotteryUser;
import plan.lottery.biz.server.UserServer;
import plan.lottery.common.CustomErrors;
import plan.lottery.utils.ClassUtil;

@Pump("admin_user")
@Component
public class AdminUserPump extends DataPump<JSONObject, FullHttpRequest, Channel> {
	
	public static final Logger log = LogManager.getLogger(AdminUserPump.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private UserServer userServer;
	
	@Pipe("list")
	@BarScreen(
		desc="用户列表"
	)
	public Errcode list (JSONObject params) {
		return userServer.getUserList(params);
	}
	
	@Pipe("addUser")
	@BarScreen(
		desc="新增用户"
	)
	public Errcode addUser (JSONObject params) {
		LotteryUser user = new LotteryUser();
		user = ClassUtil.fillObject((Map) params, user);
		userServer.addUser(user);
		return new DataResult(Errors.OK, new Data(user));
	}
}