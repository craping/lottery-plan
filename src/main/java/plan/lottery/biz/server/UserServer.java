package plan.lottery.biz.server;

import plan.data.sql.entity.LotteryUser;

public interface UserServer {

	LotteryUser getUser(String userName, String userPwd);
	int updateUser(LotteryUser user);
}
