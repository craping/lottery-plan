package plan.server.pump;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.jrain.core.asm.annotation.Pipe;
import org.crap.jrain.core.asm.annotation.Pump;
import org.crap.jrain.core.asm.handler.DataPump;
import org.crap.jrain.core.bean.result.Errcode;
import org.crap.jrain.core.bean.result.Result;
import org.crap.jrain.core.error.support.Errors;
import org.crap.jrain.core.validate.annotation.BarScreen;
import org.crap.jrain.core.validate.annotation.Parameter;
import org.springframework.stereotype.Component;

@Pump("user")
@Component
public class UserPump extends DataPump<Map<String, Object>> {
	
	public static final Logger log = LogManager.getLogger(UserPump.class);
	
	@Pipe("login")
	@BarScreen(
		desc="API文档",
		//security=true,
		params= {
			@Parameter(value="login_name",  desc="登录名"),
			@Parameter(value="login_pwd",  desc="密码"),
		}
	)
	public Errcode login (Map<String, Object> params) {
		/*WFKUser user = userServer.getUser(params.get("login_name"), params.get("login_pwd"));
		if (user == null)	//判断用户是否存在
			return new Result(CustomErrors.USER_ERROR_ACCOUNT);

		HttpSession session = request.getSession();
		session.setAttribute("user", user); // 设置登录状态
		return new DataResult(Errors.OK);*/
		return new Result(Errors.OK);
	}
}
