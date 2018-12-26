package plan.data.sql.mapper;

import org.crap.data.dao.sql.service.Mapper;

import plan.data.sql.entity.LotteryPlan;

public interface LotteryPlanMapper extends Mapper<LotteryPlan> {
	int deleteByPrimaryKey(Integer id);

	int insert(LotteryPlan record);

	int insertSelective(LotteryPlan record);

	LotteryPlan selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(LotteryPlan record);

	int updateByPrimaryKey(LotteryPlan record);
}