package plan.data.sql.mapper;

import org.crap.data.dao.sql.service.Mapper;

import plan.data.sql.entity.LotteryUserSetting;

public interface LotteryUserSettingMapper extends Mapper<LotteryUserSetting> {
	int deleteByPrimaryKey(Integer id);

	int insert(LotteryUserSetting record);

	int insertSelective(LotteryUserSetting record);

	LotteryUserSetting selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(LotteryUserSetting record);

	int updateByPrimaryKey(LotteryUserSetting record);
}