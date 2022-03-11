package it.bonny.app.wisespender.bean;

public class SettingsBean {

    private boolean isReminder;
    private int theme;
    private String currency;

    public SettingsBean() {}

    public boolean isReminder() {
        return isReminder;
    }
    public void setReminder(boolean reminder) {
        isReminder = reminder;
    }

    public int getTheme() {
        return theme;
    }
    public void setTheme(int theme) {
        this.theme = theme;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
