package plan.lottery.common.param;

import java.util.HashMap;
import java.util.Map;

import org.crap.jrain.core.bean.result.Errcode;
import org.crap.jrain.core.bean.result.Result;
import org.crap.jrain.core.bean.result.criteria.Data;
import org.crap.jrain.core.bean.result.criteria.DataResult;
import org.crap.jrain.core.error.support.Errors;
import org.crap.jrain.core.validate.exception.ValidationException;
import org.crap.jrain.core.validate.support.param.SingleParam;
import org.crap.jrain.core.validate.support.param.StringParam;

import plan.lottery.common.CustomErrors;
import plan.lottery.utils.RedisUtil;
import plan.lottery.utils.Tools;

public class TokenParam extends StringParam implements SingleParam {
	
	public TokenParam() {
		this.desc="用户Token";
		this.value = "token";
	}

	@Override
	protected Errcode validateValue(Object param) throws ValidationException {
		String token = param.toString();
		if (Tools.isStrEmpty(token))
			return new Result(CustomErrors.USER_TOKEN_NULL);
		
		String key = "user_" + token;
		if (!(new RedisUtil().exists(key))) {
			return new Result(CustomErrors.USER_NOT_LOGIN);
		}
		
		Map<String, String> userMap = new RedisUtil().hgetall(key);
		Map<String, Object> info = new HashMap<>();
		info.put("id", Integer.valueOf(userMap.get("id")));
		info.put("locked", Integer.valueOf(userMap.get("locked")));
		info.put("userName", userMap.get("userName"));
		info.put("regTime", Long.valueOf(userMap.get("regTime")));
		info.put("serverStart", Long.valueOf(userMap.get("serverStart")));
		info.put("serverEnd", Long.valueOf(userMap.get("serverEnd")));
		info.put("token", userMap.get("token"));
		if (!(new RedisUtil().hget(key, "locked").equals("0"))) {
			return new DataResult(CustomErrors.USER_LOCKED, new Data(info));
		}
		
		long serverEnd = Long.parseLong(new RedisUtil().hget(key, "serverEnd"));
		if (Tools.isOverTime(serverEnd, 5)) {
			return new DataResult(CustomErrors.USER_SERVER_END, new Data(info));
		}
		
		return Errors.OK;
	}

	@Override
	protected String cast0(Object param) {
		return param.toString();
	}
}
