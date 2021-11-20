package it.bonny.app.wisespender.bean;

public class AccountBean {

    public static final String TABLE = "account";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_OPENING_BALANCE = "openingBalance";
    public static final String KEY_FLAG_VIEW_TOTAL_BALANCE = "flag_view_total_balance";
    public static final String KEY_FLAG_SELECTED = "flag_selected";
    public static final String KEY_IS_MASTER = "is_master";
    public static final String KEY_CURRENCY = "currency";//EUR
    public static final String KEY_ID_ICON = "id_icon";
    public static final String KEY_TOT_MONEY_INCOME = "totMoneyIncome";
    public static final String KEY_TOT_MONEY_EXPENSE = "totMoneyExpense";

    private long id;
    private String name;
    private int openingBalance;
    private int flagViewTotalBalance;
    private int flagSelected;
    private int isMaster;
    private String currency;
    private int idIcon;
    private int totMoneyIncome;
    private int totMoneyExpense;

    public AccountBean() {
        currency = "EUR";
    }


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getOpeningBalance() {
        return openingBalance;
    }
    public void setOpeningBalance(int openingBalance) {
        this.openingBalance = openingBalance;
    }

    public int getFlagViewTotalBalance() {
        return flagViewTotalBalance;
    }
    public void setFlagViewTotalBalance(int flagViewTotalBalance) {
        this.flagViewTotalBalance = flagViewTotalBalance;
    }

    public int getFlagSelected() {
        return flagSelected;
    }
    public void setFlagSelected(int flagSelected) {
        this.flagSelected = flagSelected;
    }

    public int getIsMaster() {
        return isMaster;
    }
    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getIdIcon() {
        return idIcon;
    }
    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }

    public int getTotMoneyIncome() {
        return totMoneyIncome;
    }
    public void setTotMoneyIncome(int totMoneyIncome) {
        this.totMoneyIncome = totMoneyIncome;
    }

    public int getTotMoneyExpense() {
        return totMoneyExpense;
    }
    public void setTotMoneyExpense(int totMoneyExpense) {
        this.totMoneyExpense = totMoneyExpense;
    }

}
