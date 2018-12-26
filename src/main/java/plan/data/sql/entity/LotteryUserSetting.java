package plan.data.sql.entity;

import java.math.BigDecimal;

public class LotteryUserSetting {
    private Integer id;

    private Integer uid;

    private String name;

    private String lotteryType;

    private BigDecimal startMoney;

    private Integer chaseMaxNum;

    private String chaseMode;

    private String betMode;

    private String rate;

    private Integer minMultiple;

    private Integer maxMultiple;

    private BigDecimal stopLose;

    private BigDecimal stopWin;

    private String planName;

    private String betType;

    private Integer position;

    private Integer betCount;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(String lotteryType) {
        this.lotteryType = lotteryType == null ? null : lotteryType.trim();
    }

    public BigDecimal getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(BigDecimal startMoney) {
        this.startMoney = startMoney;
    }

    public Integer getChaseMaxNum() {
        return chaseMaxNum;
    }

    public void setChaseMaxNum(Integer chaseMaxNum) {
        this.chaseMaxNum = chaseMaxNum;
    }

    public String getChaseMode() {
        return chaseMode;
    }

    public void setChaseMode(String chaseMode) {
        this.chaseMode = chaseMode == null ? null : chaseMode.trim();
    }

    public String getBetMode() {
        return betMode;
    }

    public void setBetMode(String betMode) {
        this.betMode = betMode == null ? null : betMode.trim();
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate == null ? null : rate.trim();
    }

    public Integer getMinMultiple() {
        return minMultiple;
    }

    public void setMinMultiple(Integer minMultiple) {
        this.minMultiple = minMultiple;
    }

    public Integer getMaxMultiple() {
        return maxMultiple;
    }

    public void setMaxMultiple(Integer maxMultiple) {
        this.maxMultiple = maxMultiple;
    }

    public BigDecimal getStopLose() {
        return stopLose;
    }

    public void setStopLose(BigDecimal stopLose) {
        this.stopLose = stopLose;
    }

    public BigDecimal getStopWin() {
        return stopWin;
    }

    public void setStopWin(BigDecimal stopWin) {
        this.stopWin = stopWin;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName == null ? null : planName.trim();
    }

    public String getBetType() {
        return betType;
    }

    public void setBetType(String betType) {
        this.betType = betType == null ? null : betType.trim();
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getBetCount() {
        return betCount;
    }

    public void setBetCount(Integer betCount) {
        this.betCount = betCount;
    }
}