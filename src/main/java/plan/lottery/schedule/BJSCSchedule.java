package plan.lottery.schedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryBetting;
import plan.data.sql.entity.LotteryPlan;
import plan.lottery.biz.server.BettingServer;
import plan.lottery.biz.server.PlanServer;
import plan.lottery.biz.vo.BJSCResutVO;
import plan.lottery.common.BJSCPlayType;
import plan.lottery.utils.Arith;
import plan.lottery.utils.ClassUtil;
import plan.lottery.utils.JsoupUtil;
import plan.lottery.utils.Tools;


/**
  *   北京赛车定时任务
 * @author wr
 *
 */
@Component
public class BJSCSchedule {
	
	public static final Logger log = LogManager.getLogger(BJSCSchedule.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private BettingServer bettingServer;
	@Autowired
	private PlanServer planServer;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Scheduled(cron="0 0,5,10,15,20,25,30,35,40,45,50,55 0,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 * * ? ")
	//@Scheduled(fixedDelay = 20000)
	public void oprBJSCBonus() {
		log.info("########## 开始北京赛车开奖任务 ##########");
		BJSCResutVO resultVo = JsoupUtil.BJSCReslut();
		String lottery_reslut = StringUtils.join(resultVo.getResult(), ","); // 开奖结果
		log.info("########## 开奖内容：" + resultVo.getPeriod() + ":" + lottery_reslut);
		String key = "betting_" + resultVo.getPeriod();
		
		// 获取用户投注记录
		List<String> list = redisTemplate.opsForList().range(key, 0, -1);
		log.info("########## 投注记录：" + list.size());
		if (list.size() <= 0)
			return;
		
		List<LotteryBetting> bettingList = new ArrayList<>();
		for (String str : list) {
			JSONObject record = JSONObject.fromObject(str);
			String bet_type = record.getString("bet_type"); // 投注类型
			String schema = record.getString("bet_schema"); 	// 投注方案
			String amount = record.getString("amount");		// 投注金额
			Integer position = record.getInt("position");
			BigDecimal bonus = new BigDecimal("0.00");
			
			// 判断是否中奖；计算奖金
			int isWin = BJSCPlayType.getPlayType(bet_type).calcWinUnit(resultVo, position, schema);
			if (isWin == 1) {
				if (bet_type.equals(BJSCPlayType.GYH.getSimpleName())) {
					Map<String, String> map = Tools.split(schema);
					schema = map.keySet().toString().replace("[","").replace("]", "");
					bonus = new BigDecimal(Arith.mul(amount, map.get(resultVo.getGyh())));
				} else {
					bonus = new BigDecimal(Arith.mul(amount, record.getString("rate")));
				}
			}
			
			LotteryBetting betting = new LotteryBetting();
			betting = ClassUtil.fillObject((Map)record, betting);
			betting.setLotteryResult(lottery_reslut);
			betting.setBonus(bonus);
			betting.setWin(isWin);
			bettingList.add(betting);
		}
		
		// 当前期投注记录持久化 并删除投注缓存记录
		if (bettingServer.batchInsert(bettingList) == 1) {
			redisTemplate.delete(key);
		}
		log.info("########## 插入记录：" + bettingList.size());
	}
	
	//@Scheduled(fixedDelay = 20000)
	public void oprBJSCPlan() {
		List<LotteryPlan> result = new ArrayList<>();
		log.info("########## 开始每天保存推荐计划持久化任务 ##########");
		Set<String> keys = redisTemplate.keys("plan_history_*");
		if (keys.size() <= 0)
			return;
		
		for (String key : keys) {
			List<String> plans = redisTemplate.opsForList().range(key, 0, -1);
			for (String plan : plans) {
				LotteryPlan lotteryPlan = new LotteryPlan();
				JSONObject obj = JSONObject.fromObject(plan);
				lotteryPlan.setName(obj.getString("name"));
				lotteryPlan.setLottery(obj.getString("lottery").toUpperCase());
				lotteryPlan.setPlanType(obj.getString("type"));
				lotteryPlan.setPlanCount(obj.optInt("plan_count"));
				lotteryPlan.setPlanSchema(obj.getString("schema"));
				lotteryPlan.setPeriod(obj.getString("period"));
				lotteryPlan.setPosition(obj.optInt("position"));
				lotteryPlan.setCreateTime(new Date(obj.optLong("time", new Date().getTime())));
				lotteryPlan.setWin(obj.optBoolean("win")?1:0);
				lotteryPlan.setWinPeriod(obj.optString("win_period"));
				lotteryPlan.setWinResult(obj.optString("win_result"));
				result.add(lotteryPlan);
			}
		}
		// 当前期投注记录持久化 并删除投注缓存记录
		if (planServer.batchInsert(result) == 1) {
			redisTemplate.delete(keys);
		}
		log.info("########## 保存推荐计划持久化任务执行完毕：" + keys.size());
	}
}
