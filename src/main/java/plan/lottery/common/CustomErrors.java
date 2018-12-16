package plan.lottery.common;

import org.crap.jrain.core.bean.result.Errcode;


public enum CustomErrors implements Errcode {

	ENT_ACC_ERR(501, "企业帐号或密码错误"),
	ENT_INFO_ERR(501, "获取企业信息异常"),
	
	USER_INFO_ERR(507, "获取用户信息异常"),
	USER_CHILD_INFO_ERR(505, "子帐号信息异常"),
	USER_MAIN_INFO_ERR(506, "主张号信息异常"),
	USER_CHILD_STATUS_ERR(504, "子帐号状态异常"),
	USER_ACC_ERR(501, "用户帐号或密码错误"),
	USER_LOCKED_ERR(502, "用户状态为锁定"),
	USER_ADDR_ERR(201, "获取用户常用收货地址列表异常"),
	USER_UNKNOW_TYPE_ERR(499, "未知用户类型"),
	USER_NOT_LOGIN(604, "用户未登录"),

	USER_NOT_STORE_ROLE(605,"非商户无权此操作");
	
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
		return errCode;
	}

	@Override
	public String getMsg() {
		return errMsg;
	}
}
