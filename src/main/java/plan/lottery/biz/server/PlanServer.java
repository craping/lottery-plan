package plan.lottery.biz.server;

import java.util.List;

import plan.data.sql.entity.LotteryPlan;

public interface PlanServer {

	int batchInsert(List<LotteryPlan> plans);

}
