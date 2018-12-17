package plan.lottery.common;

import org.crap.jrain.core.bean.result.Errcode;


public enum CustomErrors implements Errcode {

	USER_ACC_ERR(501, "用户帐号或密码错误"),
	USER_LOCKED_ERR(502, "用户状态为锁定"),
	USER_LOGIN_ERR_EX(503, "登录失败，请联系管理员"),
	USER_NOT_LOGIN(604, "用户未登录");
	
	public int errCode;
	public String errMsg;

	private CustomErrors(int errCode, String errMsg) {
		this.errCode = errCode;
		this.errMsg = errMsg;
	}

	@Override
	public int getResult() {
		return 0;
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
