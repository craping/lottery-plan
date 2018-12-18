package plan.data.sql.mapper;

import org.crap.data.dao.sql.service.Mapper;

import plan.data.sql.entity.LotteryBetting;

public interface LotteryBettingMapper extends Mapper<LotteryBetting> {
	int deleteByPrimaryKey(Integer id);

	int insert(LotteryBetting record);

	int insertSelective(LotteryBetting record);

	LotteryBetting selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(LotteryBetting record);

	int updateByPrimaryKey(LotteryBetting record);
}