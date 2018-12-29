package plan.lottery.biz.server;

import java.util.List;

import org.crap.jrain.core.bean.result.criteria.DataResult;

import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryBetting;

public interface BettingServer {

	DataResult getBettings(JSONObject params);

	int batchInsert(List<LotteryBetting> bettingList);

}
