package plan.lottery.schedule;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.jrain.core.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import plan.data.sql.entity.LotteryBetting;
import plan.lottery.biz.server.BettingServer;
import plan.lottery.biz.vo.BJSCResutVO;
import plan.lottery.common.BJSCPlayType;
import plan.lottery.utils.Arith;
import plan.lottery.utils.JsoupUtil;
import plan.lottery.utils.Tools;

@Component
public class BJSCSchedule {
	
	public static final Logger log = LogManager.getLogger(BJSCSchedule.class);
	
	@Autowired
	private StringRedisTemplate redisTemplate;
	
	@Autowired
	private BettingServer bettingServer;

	//@Scheduled(cron="0 0,10,15,20,25,30,35,40,45,50,55 9-23 * * ?")
	//@Scheduled(fixedDelay = 20000)
	public void oprBJSCBonus() {
		log.info("########## 开始北京赛车开奖任务 ##########");
		BJSCResutVO resultVo = JsoupUtil.BJSCReslut();
		String lottery_reslut = StringUtils.join(resultVo.getResult(), ","); // 开奖结果
		log.info("########## 开奖内容：" + lottery_reslut);
		String key = "user_betting_" + resultVo.getPeriod();
		
		// 获取用户投注记录
		List<String> l = redisTemplate.opsForList().range("user_betting_721336", 0, -1);
		log.info("########## 投注记录：" + l.size());
		
		if (l.size() > 0) {
			List<LotteryBetting> bettingList = new ArrayList<>();
			for (String s : l) {
				System.out.println(s);
				JSONObject record = JSONObject.fromObject(s);
			
				// 缓存获取用户信息
				String user_key = "user_" + record.getString("token");
				Map<Object, Object> userMap = redisTemplate.opsForHash().entries(user_key);
				Integer uid = Integer.parseInt(userMap.get("uid").toString());
				String user_name = userMap.get("user_name").toString();
				String bet_type = record.getString("bet_type"); // 投注类型
				String bet_schema = record.getString("bet_schema"); // 投注方案
				String bet_amount = record.getString("bet_amount");
				Integer bet_position = Integer.parseInt(record.getString("bet_position"));
				BigDecimal bonus_amount = new BigDecimal("0.00");
				
				// 判断是否中奖
				int isWin = BJSCPlayType.getPlayType(bet_type).calcWinUnit(resultVo, bet_position, bet_schema);
				if (isWin == 1) {
					// 计算奖金
					if (bet_type.equals(BJSCPlayType.GYH.getSimpleName())) {
						Map<String, String> map = Tools.split(bet_schema);
						bet_schema = map.keySet().toString().replace("[","").replace("]", "");
						bonus_amount = new BigDecimal(Arith.mul(bet_amount, map.get(resultVo.getGyh())));
					} else {
						bonus_amount = new BigDecimal(Arith.mul(bet_amount, record.getString("bet_rate")));
					}
				}
				
				LotteryBetting betting = new LotteryBetting();
				betting.setUid(uid);
				betting.setUserName(user_name);
				betting.setLotteryType(record.getString("lottery_type"));
				betting.setLotteryResult(lottery_reslut);
				betting.setBetType(bet_type);
				betting.setBetPeriod(record.getString("bet_period"));
				betting.setBetSchema(bet_schema);
				betting.setBetPosition(bet_position);
				betting.setBetTime(DateUtil.parseDate(record.getString("bet_time"), "yyyyMMddHHmmss"));
				betting.setBetAmount(new BigDecimal(bet_amount));
				betting.setBetRate(new BigDecimal(record.getString("bet_rate")));
				betting.setBonusAmount(bonus_amount);
				betting.setWin(isWin);
				bettingList.add(betting);
			}
			
			// 当前期投注记录持久化 并删除投注缓存记录
			if (bettingServer.batchInsert(bettingList) == 1) {
				redisTemplate.delete("user_betting_721336"); 
			}
		}
		log.info("########## 北京赛车开奖任务结束 ##########");
	}
}
