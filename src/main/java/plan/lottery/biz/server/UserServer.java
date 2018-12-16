package plan.lottery.biz.server;

import plan.data.sql.entity.LotteryUser;

public interface UserServer {

	public LotteryUser getUser(String userName, String userPwd);
	
}
