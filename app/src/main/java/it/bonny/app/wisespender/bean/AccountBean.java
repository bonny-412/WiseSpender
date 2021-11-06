package it.bonny.app.wisespender.bean;

public class AccountBean {

    public static final String TABLE = "account";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_FLAG_VIEW_TOTAL_BALANCE = "flag_view_total_balance";
    public static final String KEY_FLAG_SELECTED = "flag_selected";
    public static final String KEY_IS_MASTER = "is_master";
    public static final String KEY_CURRENCY = "currency";//EUR
    public static final String KEY_ID_ICON = "id_icon";

    private long id;
    private String name;
    private int amount;
    private int flagViewTotalBalance;
    private int flagSelected;
    private int isMaster;
    private String currency;
    private String idIcon;

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

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
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

    public String getIdIcon() {
        return idIcon;
    }
    public void setIdIcon(String idIcon) {
        this.idIcon = idIcon;
    }

}
