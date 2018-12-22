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
import org.crap.jrain.core.validate.security.component.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryUser;
import plan.lottery.biz.server.UserServer;
import plan.lottery.common.CustomErrors;
import plan.lottery.common.param.TokenParam;
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
		}
		
		String flag = "user_";
		String old_token = user.getToken(); // 获取上一次用户token
		String new_token = Tools.getUuid(); // 生成新的用户token
	
		Map<Object, Object> userMap = redisTemplate.opsForHash().entries(flag + old_token);
		if (userMap == null || userMap.isEmpty()) {
			userMap.put("uid", user.getId().toString());
			userMap.put("user_name", user.getUserName());
			userMap.put("user_pwd", user.getUserPwd());
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
	
	@Pipe("logout")
	@BarScreen(
		desc="用户退出"
	)
	public Errcode logout (JSONObject params) {
		String key = "user_" + params.getString("token");
		redisTemplate.delete(key); // 删除缓存
		return new DataResult(Errors.OK);
	}
	
	@Pipe("changePwd")
	@BarScreen(
		desc="修改密码",
		params= {
			@Parameter(type=TokenParam.class),
			@Parameter(value="old_pwd",  desc="当前密码"),
			@Parameter(value="new_pwd",  desc="新密码"),
			@Parameter(value="confirm_pwd",  desc="新密码"),
		}
	)
	public Errcode changePwd (JSONObject params) {
		String key = "user_" + params.getString("token");
		// 获取缓存
		Map<Object, Object> userMap = redisTemplate.opsForHash().entries(key);
		String user_name = userMap.get("user_name").toString();
		String user_pwd = userMap.get("user_pwd").toString();
		if (!Coder.encryptMD5(params.getString("old_pwd")).equals(user_pwd))
			return new Result(CustomErrors.USER_PWD_ERR);
		
		String new_pwd = params.getString("new_pwd");
		String confirm_pwd = params.getString("confirm_pwd");
		if (new_pwd != confirm_pwd && !new_pwd.equals(confirm_pwd))
			return new Result(CustomErrors.USER_CHANGE_PWD_ERR);
		
		LotteryUser user = userServer.getUser(user_name, user_pwd);
		user.setUserPwd(Coder.encryptMD5(new_pwd));
		
		int result = userServer.updateUser(user);
		if (result == 1) {
			redisTemplate.opsForHash().put(key, "user_pwd", new_pwd);
		} else {
			return new Result(CustomErrors.USER_OPR_ERR);
		}
		return new DataResult(Errors.OK);
	}
}
