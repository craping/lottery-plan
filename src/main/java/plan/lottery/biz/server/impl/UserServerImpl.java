package plan.lottery.biz.server.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.data.dao.sql.util.support.ServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plan.data.sql.entity.LotteryUser;
import plan.lottery.biz.server.UserServer;

@Service
public class UserServerImpl implements UserServer {
	
	public static final Logger log = LogManager.getLogger(UserServerImpl.class);

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

	@Override
	public int updateUser(LotteryUser user) {
		int result = 0;
		try {
			serviceDao.getMapper().update(user);
			result = 1; 
		} catch (Exception ex) {
			ex.printStackTrace();
			log.error("用户更新异常:", ex);
		}
		return result;
	}
}
