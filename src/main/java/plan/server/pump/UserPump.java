package plan.server.pump;

import java.net.InetSocketAddress;
import java.util.HashMap;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryUser;
import plan.lottery.biz.server.UserServer;
import plan.lottery.common.CustomErrors;
import plan.lottery.common.param.TokenParam;

@Pump("user")
@Component
public class UserPump extends DataPump<JSONObject, FullHttpRequest, Channel> {
	
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
		if (user == null) //判断用户是否存在
			return new Result(CustomErrors.USER_ACC_ERR);
		
		redisTemplate.delete("user_" + user.getToken()); //  删除当前缓存
		
		// 生成新的用户token 并持久化
		String new_token = StringUtil.uuid(); 	
		user.setToken(new_token);
		int result = userServer.updateUser(user);
		if (result == 1) {
			// 插入登录日志 
			InetSocketAddress insocket = (InetSocketAddress) getResponse().remoteAddress();
			System.out.println("IP:"+insocket.getAddress().getHostAddress());
			userServer.insertLoginLog(user.getId(), insocket.getAddress().getHostAddress());
			/*// 缓存用户配置信息
			List<LotteryUserSetting> settings = settingServer.getSettings(user.getId());
			settings.forEach((setting) -> 
				userMap.put("setting" + setting.getId() + "_" + Coder.encryptMD5(setting.getName()), JSONObject.fromObject(setting).toString())
			);*/
			Map<Object, Object> userMap = new HashMap<Object, Object>();
			userMap.put("id", user.getId().toString());
			userMap.put("locked", user.getLocked().toString());
			userMap.put("userName", user.getUserName());
			userMap.put("serverStart", String.valueOf(user.getServerStart().getTime()));
			userMap.put("serverEnd", String.valueOf(user.getServerEnd().getTime()));
			userMap.put("token", new_token);
			redisTemplate.opsForHash().putAll("user_" + new_token, userMap);
		} else {
			return new Result(CustomErrors.USER_LOGIN_ERR_EX);
		}
		user.setUserPwd(null);
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
	
	@Pipe("getUserInfo")
	@BarScreen(
		desc="获取用户信息",
		params= {
			@Parameter(type=TokenParam.class)
		}
	)
	public Errcode getUserInfo (JSONObject params) {
		String key = "user_" + params.getString("token");
		Map<Object, Object> userMap = redisTemplate.opsForHash().entries(key);
		userMap.remove("user_pwd");
		return new DataResult(Errors.OK, new Data(userMap));
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
		if (!params.getString("old_pwd").equals(user_pwd))
			return new Result(CustomErrors.USER_PWD_ERR);
		
		String new_pwd = params.getString("new_pwd");
		String confirm_pwd = params.getString("confirm_pwd");
		if (new_pwd != confirm_pwd && !new_pwd.equals(confirm_pwd))
			return new Result(CustomErrors.USER_CHANGE_PWD_ERR);
		
		LotteryUser user = userServer.getUser(user_name, user_pwd);
		user.setUserPwd(new_pwd);
		
		int result = userServer.updateUser(user);
		if (result == 1) {
			redisTemplate.opsForHash().put(key, "user_pwd", new_pwd);
		} else {
			return new Result(CustomErrors.USER_OPR_ERR);
		}
		return new DataResult(Errors.OK);
	}
}