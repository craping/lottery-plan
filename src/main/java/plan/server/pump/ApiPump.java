package plan.server.pump;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.jrain.core.asm.annotation.Pipe;
import org.crap.jrain.core.asm.annotation.Pump;
import org.crap.jrain.core.asm.handler.DataPump;
import org.crap.jrain.core.bean.result.Errcode;
import org.crap.jrain.core.bean.result.Result;
import org.crap.jrain.core.bean.result.criteria.Data;
import org.crap.jrain.core.bean.result.criteria.DataResult;
import org.crap.jrain.core.error.support.Errors;
import org.crap.jrain.core.util.PackageUtil;
import org.crap.jrain.core.validate.annotation.BarScreen;
import org.springframework.stereotype.Component;

import net.sf.json.JSONObject;
import plan.lottery.common.Lottery;
import plan.server.HttpServer;

@Pump("api")
@Component
public class ApiPump extends DataPump<Map<String, Object>> {
	
	public static final Logger log = LogManager.getLogger(ApiPump.class);
	
	@Pipe("apiDocument")
	@BarScreen(desc="API文档")
	public Errcode api (Map<String, Object> params) {
		try {
			String info = PackageUtil.apiResolve("plan.server.pump", "http://127.0.0.1:"+HttpServer.PORT);
			log.info("info:"+info);
			return new DataResult(Errors.OK, new Data(info));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(Errors.OK);
	}
	
	public static void main (String args[]) throws IOException {
		System.out.println(Lottery.PK10.ordinal());
		System.out.println(Lottery.PK10);
		System.out.println(Lottery.PK10.getLotteryName());
		System.out.println(Lottery.PK10.getSimpleName());
		System.out.println(Lottery.getLotteryName("PK10"));
		
		Map<String, String> info = new HashMap<>();
		info.put("uid", "a");
		info.put("lottery_type", "b");
		info.put("bet_type", "c");
		info.put("bet_period", "d");
		info.put("bet_schema", "e");
		info.put("bet_amount", "f");
		System.out.println(info);
		
		JSONObject jsonStr = JSONObject.fromObject(info);
		System.out.println(jsonStr.toString());
		Map map = jsonStr;
		System.out.println(map);
		System.out.println(map.get("bet_type"));
		System.out.println(JSONObject.fromObject(info));
		
		String num = "1|2|3|4|5|6|7|8||10";
		String ns[] = num.split("\\|");
		System.out.println(ns.length);
	}
}
