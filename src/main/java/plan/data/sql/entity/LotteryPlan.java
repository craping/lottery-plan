package plan.data.sql.entity;

import java.util.Date;

public class LotteryPlan {
    private Integer id;

    private String name;

    private String planType;

    private Integer planCount;

    private String planSchema;

    private String period;

    private Integer position;

    private Date createTime;

    private String winResult;

    private String winPeriod;

    private Integer win;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType == null ? null : planType.trim();
    }

    public Integer getPlanCount() {
        return planCount;
    }

    public void setPlanCount(Integer planCount) {
        this.planCount = planCount;
    }

    public String getPlanSchema() {
        return planSchema;
    }

    public void setPlanSchema(String planSchema) {
        this.planSchema = planSchema == null ? null : planSchema.trim();
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period == null ? null : period.trim();
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWinResult() {
        return winResult;
    }

    public void setWinResult(String winResult) {
        this.winResult = winResult == null ? null : winResult.trim();
    }

    public String getWinPeriod() {
        return winPeriod;
    }

    public void setWinPeriod(String winPeriod) {
        this.winPeriod = winPeriod == null ? null : winPeriod.trim();
    }

    public Integer getWin() {
        return win;
    }

    public void setWin(Integer win) {
        this.win = win;
    }
}