package plan.data.sql.service;

import plan.data.sql.entity.TBetting;

public interface TBettingMapper extends Mapper<TBetting> {
    int deleteByPrimaryKey(Integer id);

    int insert(TBetting record);

    int insertSelective(TBetting record);

    TBetting selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TBetting record);

    int updateByPrimaryKey(TBetting record);
}