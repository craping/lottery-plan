package plan.lottery.biz.server;

import org.crap.jrain.core.bean.result.criteria.DataResult;

import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryAdmin;
import plan.data.sql.entity.LotteryUser;

public interface UserServer {

	DataResult getUserList(JSONObject params);

	LotteryUser getUser(String userName, String userPwd);

	LotteryUser getUserByToken(String token);

	int updateUser(LotteryUser user);

	int addUser(LotteryUser user);

	int insertLoginLog(Integer uid, String ip);

	LotteryAdmin getAdminUser(String userName, String userPwd);

	int updateAdminUser(Integer id, String token);
}
