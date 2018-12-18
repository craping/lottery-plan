package plan.lottery.common;

public enum BJSCPlayType {

	DWD("定位胆"), 
	LH("前五龙虎"), 
	GYH("冠亚和");

	private final String lotteryTypeName;

	private BJSCPlayType(String lotteryTypeName) {
		this.lotteryTypeName = lotteryTypeName;
	}

	public String getLotteryTypeName() {
		return lotteryTypeName;
	}
	
	
}
