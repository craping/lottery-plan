package plan.lottery.common;

import plan.lottery.biz.vo.BJSCResutVO;
import plan.lottery.utils.Tools;

public enum BJSCPlayType {

	DWD("DWD", "定位胆") {
		@Override
		public int calcWinUnit(BJSCResutVO resultVo, Integer position, String schema) {
			String[] result = resultVo.getResult(); // 开奖结果
			if (schema.contains(result[position-1])) { // 中奖
				return 1;
			}
			return 0;
		}
	},
	LH("LH", "前五龙虎") {
		@Override
		public int calcWinUnit(BJSCResutVO resultVo, Integer position, String schema) {
			String[] result = resultVo.getLh();
			if ((PlayTypeLH.getPlayType(schema).getTypeName()).equals(result[position-1])) { // 中奖
				return 1;
			}
			return 0;
		}
	},
	GYH("GYH", "冠亚和") {
		@Override
		public int calcWinUnit(BJSCResutVO resultVo, Integer position, String schema) {
			String result = resultVo.getGyh();
			if ((Tools.split(schema).keySet().toString()).contains(result)) {
				return 1;
			}
			return 0;
		}
	},
	GYHDS("GYHDS", "冠亚和单双") {
		@Override
		public int calcWinUnit(BJSCResutVO resultVo, Integer position, String schema) {
			String result = resultVo.getGyhds();
			if ((PlayTypeDS.getPlayType(schema).getTypeName()).equals(result)) {
				return 1;
			}
			return 0;
		}
	},
	GYHDX("GYHDX", "冠亚和大小") {
		@Override
		public int calcWinUnit(BJSCResutVO resultVo, Integer position, String schema) {
			String result = resultVo.getGyhdx();
			if ((PlayTypeDX.getPlayType(schema).getTypeName()).equals(result)) {
				return 1;
			}
			return 0;
		}
	};

	private final String simpleName;
	private final String lotteryTypeName;

	private BJSCPlayType(String simpleName, String lotteryTypeName) {
		this.simpleName = simpleName;
		this.lotteryTypeName = lotteryTypeName;
	}

	public String getLotteryTypeName() {
		return lotteryTypeName;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public static BJSCPlayType getPlayType(String simpleName) {
		for (BJSCPlayType l : BJSCPlayType.values()) {
			if (l.simpleName.equals(simpleName))
				return l;
		}
		return null;
	}

	/**
	 * @param resultVo   开奖结果
	 * @param bet_schema   投注方案号码
	 * @param bet_position 投注位置
	 * @return 1中奖0未中奖
	 */
	public abstract int calcWinUnit(BJSCResutVO resultVo, Integer position, String schema);
}
