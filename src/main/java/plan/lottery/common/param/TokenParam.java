package plan.lottery.common.param;

import org.crap.jrain.core.bean.result.Errcode;
import org.crap.jrain.core.bean.result.Result;
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
		return Errors.OK;
	}

	@Override
	protected String cast0(Object param) {
		return param.toString();
	}
}
