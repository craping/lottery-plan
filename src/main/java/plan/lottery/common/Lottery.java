package plan.lottery.common;

/**
 * 彩票类型.<br/>
 * 注：添加类型只能在后面添加，不能插入中间
 * 
 */
public enum Lottery {
	
	/** 时时彩 */
	SSC("时时彩", "SSC"),//0
	PK10("北京赛车", "PK10");//1

	/** 彩种简称 */
	private final String simpleName;
	/** 彩种名称 */
	private final String lotteryName;
	
	private Lottery(String lotteryName, String simpleName) {
		this.lotteryName = lotteryName;
		this.simpleName = simpleName;
	}

	public String getSimpleName() {     
		return simpleName;
	}

	public String getLotteryName() {
		return lotteryName;
	}
	 
	public static Lottery getLottery(String simpleName) {
		for (Lottery l : Lottery.values()) {
			if (l.simpleName == simpleName)
				return l;
		}
		return null;
	}
	
	public static String getLotteryName(String simpleName) {
		for (Lottery l : Lottery.values()) {
			if (l.simpleName == simpleName)
				return l.getLotteryName();
		}
		return null;
	}
}
