package plan.lottery.biz.server;

import plan.data.sql.entity.LotteryUser;

public interface UserServer {

	LotteryUser getUser(String userName, String userPwd);

	LotteryUser getUserByToken(String token);

	int updateUser(LotteryUser user);

	int insertLoginLog(Integer uid, String ip);
}
