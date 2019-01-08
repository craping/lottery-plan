package plan.data.sql.entity;

import java.util.Date;

public class LotteryUser {
	private Integer id;

	private String userName;

	private String userPwd;

	private String qq;

	private String wechat;

	private Integer phoneNum;

	private Integer phoneState;

	private Integer locked;

	private Date serverStart;

	private Date serverEnd;

	private Date regTime;

	private String regIp;

	private String token;

	private String cdkey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName == null ? null : userName.trim();
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd == null ? null : userPwd.trim();
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq == null ? null : qq.trim();
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat == null ? null : wechat.trim();
	}

	public Integer getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(Integer phoneNum) {
		this.phoneNum = phoneNum;
	}

	public Integer getPhoneState() {
		return phoneState;
	}

	public void setPhoneState(Integer phoneState) {
		this.phoneState = phoneState;
	}

	public Integer getLocked() {
		return locked;
	}

	public void setLocked(Integer locked) {
		this.locked = locked;
	}

	public Date getServerStart() {
		return serverStart;
	}

	public void setServerStart(Date serverStart) {
		this.serverStart = serverStart;
	}

	public Date getServerEnd() {
		return serverEnd;
	}

	public void setServerEnd(Date serverEnd) {
		this.serverEnd = serverEnd;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public String getRegIp() {
		return regIp;
	}

	public void setRegIp(String regIp) {
		this.regIp = regIp == null ? null : regIp.trim();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token == null ? null : token.trim();
	}

	public String getCdkey() {
		return cdkey;
	}

	public void setCdkey(String cdkey) {
		this.cdkey = cdkey == null ? null : cdkey.trim();
	}
}