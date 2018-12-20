package plan.data.sql.entity;

import java.math.BigDecimal;
import java.util.Date;

public class LotteryBetting {
    private Integer id;

    private Integer uid;

    private String userName;

    private String lotteryType;

    private String lotteryResult;

    private String betType;

    private String betPeriod;

    private String betSchema;

    private Integer betPosition;

    private Date betTime;

    private BigDecimal betAmount;

    private BigDecimal betRate;

    private BigDecimal bonusAmount;

    private Integer win;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(String lotteryType) {
        this.lotteryType = lotteryType == null ? null : lotteryType.trim();
    }

    public String getLotteryResult() {
        return lotteryResult;
    }

    public void setLotteryResult(String lotteryResult) {
        this.lotteryResult = lotteryResult == null ? null : lotteryResult.trim();
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType == null ? null : betType.trim();
    }

    public String getBetPeriod() {
        return betPeriod;
    }

    public void setBetPeriod(String betPeriod) {
        this.betPeriod = betPeriod == null ? null : betPeriod.trim();
    }

    public String getBetSchema() {
        return betSchema;
    }

    public void setBetSchema(String betSchema) {
        this.betSchema = betSchema == null ? null : betSchema.trim();
    }

    public Integer getBetPosition() {
        return betPosition;
    }

    public void setBetPosition(Integer betPosition) {
        this.betPosition = betPosition;
    }

    public Date getBetTime() {
        return betTime;
    }

    public void setBetTime(Date betTime) {
        this.betTime = betTime;
    }

    public BigDecimal getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(BigDecimal betAmount) {
        this.betAmount = betAmount;
    }

    public BigDecimal getBetRate() {
        return betRate;
    }

    public void setBetRate(BigDecimal betRate) {
        this.betRate = betRate;
    }

    public BigDecimal getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(BigDecimal bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }
}