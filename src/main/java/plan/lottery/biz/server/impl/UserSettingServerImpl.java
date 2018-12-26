package plan.lottery.biz.server.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.data.dao.sql.util.support.ServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plan.data.sql.entity.LotteryUserSetting;
import plan.lottery.biz.server.UserSettingServer;

@Service
public class UserSettingServerImpl implements UserSettingServer {

	public static final Logger log = LogManager.getLogger(UserSettingServerImpl.class);

	@Autowired
	private ServiceDao serviceDao;

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	@Override
	public int save(LotteryUserSetting setting) {
		try {
			return (int) serviceDao.getMapper().save(setting);
		} catch (Exception ex) {
			log.error("保存用户计划方案失败：", ex);
		}
		return 0;
	}

	@Override
	public List<LotteryUserSetting> getSettings(Integer uid) {
		return serviceDao.queryEntityList("SELECT * FROM lottery_user_setting", LotteryUserSetting.class);
	}

	@Override
	public int update(LotteryUserSetting setting) {
		try {
			serviceDao.getMapper().update(setting);
		} catch (Exception e) {
			log.error("更新用户计划方案失败：", e);
			return 0;
		}
		return 1;
	}

	@Override
	public int delete(Integer id) {
		return serviceDao.execute("DELETE FROM lottery_user_setting WHERE id="+ id);
	}
}