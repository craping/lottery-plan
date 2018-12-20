package plan.lottery.biz.vo;

import java.math.BigDecimal;

public class BJSCWinVO {

	private Integer win;
	private BigDecimal bonus_amount = new BigDecimal("0.00");

	public Integer getWin() {
		return win;
	}

	public void setWin(Integer win) {
		this.win = win;
	}

	public BigDecimal getBonus_amount() {
		return bonus_amount;
	}

	public void setBonus_amount(BigDecimal bonus_amount) {
		this.bonus_amount = bonus_amount;
	}

}
