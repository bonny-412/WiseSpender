package it.bonny.app.wisespender.bean;

public class Account {

    public static final String TABLE = "account";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_FLAG_VIEW_TOTAL_BALANCE = "flag_view_total_balance";
    public static final String KEY_FLAG_SELECTED = "flag_selected";

    private Integer id;
    private String name;
    private Integer amount;
    private Integer flagViewTotalBalance;
    private Integer flagSelected;

    public Account() {}


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
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getFlagViewTotalBalance() {
        return flagViewTotalBalance;
    }
    public void setFlagViewTotalBalance(Integer flagViewTotalBalance) {
        this.flagViewTotalBalance = flagViewTotalBalance;
    }

    public Integer getFlagSelected() {
        return flagSelected;
    }
    public void setFlagSelected(Integer flagSelected) {
        this.flagSelected = flagSelected;
    }

}
