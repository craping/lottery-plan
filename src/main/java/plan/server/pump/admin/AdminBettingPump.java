package plan.server.pump.admin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.crap.jrain.core.asm.annotation.Pipe;
import org.crap.jrain.core.asm.annotation.Pump;
import org.crap.jrain.core.asm.handler.DataPump;
import org.crap.jrain.core.bean.result.Errcode;
import org.crap.jrain.core.validate.annotation.BarScreen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.FullHttpRequest;
import net.sf.json.JSONObject;
import plan.lottery.biz.server.BettingServer;

@Pump("admin_betting")
@Component
public class AdminBettingPump extends DataPump<JSONObject, FullHttpRequest, Channel> {
	
	public static final Logger log = LogManager.getLogger(AdminBettingPump.class);
	
	@Autowired
	private BettingServer bettingServer;
	
	@Pipe("list")
	@BarScreen(
		desc="投注列表"
	)
	public Errcode list (JSONObject params) {
		return bettingServer.getBettings(params);
	}	
}