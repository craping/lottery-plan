package plan.lottery.utils;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import plan.lottery.biz.vo.BJSCResutVO;
import plan.lottery.common.BJSCPlayType;


/**
 * 数据抓取工具类 jsoup
 * 
 * @author wr
 *
 */
public class JsoupUtil {
	
	public static final Logger log = LogManager.getLogger(JsoupUtil.class);

	public static BJSCResutVO BJSCReslut() {
		Document doc = null;
		try {
			doc = Jsoup.connect("https://www.1396j.com/pk10/kaijiang").get();
		} catch (IOException e) {
			log.error("数据抓取异常：", e);
			e.printStackTrace();
			return null; 
		}
		Elements els = doc.select("#history tbody tr");	
		Element el = els.get(0);
		
		BJSCResutVO vo = new BJSCResutVO();
		vo.setPeriod(el.select(".font_gray666").text());
		vo.setGyh(el.select("td:nth-child(3)").text());
		vo.setGyhdx(el.select("td:nth-child(4)").text());
		vo.setGyhds(el.select("td:nth-child(5)").text());
		String[] lh = new String[]{ // 龙虎
			el.select("td:nth-child(6)").text(),
			el.select("td:nth-child(7)").text(),
			el.select("td:nth-child(8)").text(),
			el.select("td:nth-child(9)").text(),
			el.select("td:nth-child(10)").text()	
		};
		vo.setLh(lh);
		vo.setResult(el.select(".number_pk10 span").text().replace(" ", ",").split(","));
		return vo;
	}

	public static void main(String args[]) throws IOException {
		//System.out.println(JSONObject.fromObject(BJSCReslut()));
		String[] result = {"2","5","9","8","10","4","1","6","7","3"};
		String tz = "1:42.1,2:42.1,3:33,4:33,5:22";
		System.out.println(result[0]);
		System.out.println(tz.contains(result[2]));
		System.out.println(new BigDecimal(Arith.mul("50.00", "50.00")));
		String a = "GYH";
		System.out.println(a.equals(BJSCPlayType.GYH.getSimpleName()));
		System.out.println(a == BJSCPlayType.GYH.getSimpleName());
		
		/*String keys = Tools.split(tz).keySet().toString();
		System.out.println(StringUtils.join(result, ","));*/
		
	}
}
