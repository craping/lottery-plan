package plan.lottery.biz.server.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.data.dao.sql.util.support.ServiceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import plan.data.sql.entity.LotteryPlan;
import plan.lottery.biz.server.PlanServer;

@Service
public class PlanServerImpl implements PlanServer {

	public static final Logger log = LogManager.getLogger(PlanServerImpl.class);

	@Autowired
	private ServiceDao serviceDao;

	public ServiceDao getServiceDao() {
		return serviceDao;
	}

	public void setServiceDao(ServiceDao serviceDao) {
		this.serviceDao = serviceDao;
	}

	@Override
	public int batchInsert(List<LotteryPlan> plans) {
		try {
			serviceDao.getMapper().save(plans);
		} catch (Exception e) {
			log.error("批量插入投注记录异常：", e);
			return 0;
		}
		return 1;
	}
}
