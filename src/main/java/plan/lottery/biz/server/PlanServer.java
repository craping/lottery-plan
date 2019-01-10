package plan.lottery.biz.server;

import java.util.List;

import org.crap.jrain.core.bean.result.criteria.DataResult;

import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryPlan;

public interface PlanServer {

	int batchInsert(List<LotteryPlan> plans);

	DataResult getPlanList(JSONObject params);
}
