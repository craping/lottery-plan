package plan.server.pump;

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
import org.crap.jrain.core.util.DateUtil;
import org.crap.jrain.core.validate.annotation.BarScreen;
import org.crap.jrain.core.validate.annotation.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryUser;
import plan.lottery.biz.server.UserServer;
import plan.lottery.common.CustomErrors;
import plan.lottery.utils.Tools;

@Pump("user")
@Component
public class UserPump extends DataPump<JSONObject> {
	
	public static final Logger log = LogManager.getLogger(UserPump.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private UserServer userServer;
	
	@Pipe("login")
	@BarScreen(
		desc="用户登录",
		//security=true,
		params= {
			@Parameter(value="login_name",  desc="登录名"),
			@Parameter(value="login_pwd",  desc="密码"),
		}
	)
	public Errcode login (JSONObject params) {
		String userName = params.getString("login_name");
		String userPwd = params.getString("login_pwd");
		LotteryUser user = userServer.getUser(userName, userPwd);
		if (user == null) {	//判断用户是否存在
			return new Result(CustomErrors.USER_ACC_ERR);
		} else { 
			user.setUserPwd(null);
		}
		
		String flag = "lottery_user_";
		String old_token = user.getToken(); // 获取上一次用户token
		String new_token = Tools.getUuid(); // 生成新的用户token
		Map<Object, Object> userMap = redisTemplate.opsForHash().entries(flag + old_token);
		if (userMap == null || userMap.isEmpty()) {
			userMap.put("userName", user.getUserName());
			userMap.put("server_end", DateUtil.formatDate("yyyyMMddHHmmss", user.getServerEnd()));
		} else {
			redisTemplate.delete(flag+old_token);
		}
		user.setToken(new_token);
		
		// 保存用户token 持久化
		int result = userServer.updateUser(user);
		if (result == 1) {
			redisTemplate.opsForHash().putAll(flag+new_token, userMap);
		} else {
			return new Result(CustomErrors.USER_LOGIN_ERR_EX);
		}
		return new DataResult(Errors.OK, new Data(user));
	}
}
