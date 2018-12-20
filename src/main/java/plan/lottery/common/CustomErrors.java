package plan.lottery.common;

import org.crap.jrain.core.bean.result.Errcode;


public enum CustomErrors implements Errcode {

	USER_TOKEN_NULL(1,500, "请求错误，缺少参数token"),
	USER_ACC_ERR(1,501, "用户帐号或密码错误"),
	USER_LOCKED_ERR(1,502, "用户状态为锁定"),
	USER_LOGIN_ERR_EX(1,503, "登录失败，请联系管理员"),
	USER_NOT_LOGIN(1,504, "用户未登录");
	
	public int result;
	public int errCode;
	public String errMsg;

	private CustomErrors(int result, int errCode, String errMsg) {
		this.result = result;
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	@Override
	public int getResult() {
		return this.result;
	}

	@Override
	public int getErrcode() {
		return this.errCode;
	}

	@Override
	public String getMsg() {
		return this.errMsg;
	}
}
