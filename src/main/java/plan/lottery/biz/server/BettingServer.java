package plan.lottery.biz.server;

import java.util.List;

import plan.data.sql.entity.LotteryBetting;

public interface BettingServer {

	int batchInsert(List<LotteryBetting> bettingList);
}
