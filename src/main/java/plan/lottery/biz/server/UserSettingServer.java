package plan.lottery.biz.server;

import java.util.List;

import plan.data.sql.entity.LotteryUserSetting;

public interface UserSettingServer {

	int delete(Integer id);

	int update(LotteryUserSetting setting);

	int save(LotteryUserSetting setting);

	List<LotteryUserSetting> getSettings(Integer uid);
}
