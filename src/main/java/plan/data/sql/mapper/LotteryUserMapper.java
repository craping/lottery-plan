package plan.data.sql.mapper;

import plan.data.sql.entity.LotteryUser;

public interface LotteryUserMapper extends Mapper<LotteryUser> {
	int deleteByPrimaryKey(Integer id);

	int insert(LotteryUser record);

	int insertSelective(LotteryUser record);

	LotteryUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(LotteryUser record);

	int updateByPrimaryKey(LotteryUser record);
}