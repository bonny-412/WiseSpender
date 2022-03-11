package it.bonny.app.wisespender.bean;

public class PeriodSelectedBean {

    private int periodSelectedMain;
    private String dateFrom;
    private String dateTo;
    private String textPeriodSelected;

    public PeriodSelectedBean() {}

    public int getPeriodSelectedMain() {
        return periodSelectedMain;
    }
    public void setPeriodSelectedMain(int periodSelectedMain) {
        this.periodSelectedMain = periodSelectedMain;
    }

    public String getDateFrom() {
        return dateFrom;
    }
    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }
    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTextPeriodSelected() {
        return textPeriodSelected;
    }
    public void setTextPeriodSelected(String textPeriodSelected) {
        this.textPeriodSelected = textPeriodSelected;
    }

}
