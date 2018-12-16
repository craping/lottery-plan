package plan.lottery.biz.server.impl;

import org.crap.data.dao.sql.util.support.ServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plan.data.sql.entity.LotteryUser;
import plan.lottery.biz.server.UserServer;

@Service
public class UserServerImpl implements UserServer {

	@Autowired
	private ServiceDao serviceDao;
	
	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	@Override
	public LotteryUser getUser(String userName, String userPwd) {
		String sql = "SELECT * FROM lottery_user WHERE user_name=? and user_pwd=md5(?)";
		return serviceDao.get(sql,  LotteryUser.class, new Object[]{userName, userPwd});
	}
}
