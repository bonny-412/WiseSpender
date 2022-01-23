package it.bonny.app.wisespender.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountBean implements Parcelable {

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


    protected AccountBean(Parcel in) {
        id = in.readLong();
        name = in.readString();
        openingBalance = in.readInt();
        flagViewTotalBalance = in.readInt();
        flagSelected = in.readInt();
        isMaster = in.readInt();
        currency = in.readString();
        idIcon = in.readInt();
        totMoneyIncome = in.readInt();
        totMoneyExpense = in.readInt();
    }

    public static final Creator<AccountBean> CREATOR = new Creator<AccountBean>() {
        @Override
        public AccountBean createFromParcel(Parcel in) {
            return new AccountBean(in);
        }

        @Override
        public AccountBean[] newArray(int size) {
            return new AccountBean[size];
        }
    };

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

    public static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + AccountBean.TABLE
            + "("
            + AccountBean.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + AccountBean.KEY_NAME + " TEXT,"
            + AccountBean.KEY_OPENING_BALANCE + " INTEGER,"
            + AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE + " INTEGER DEFAULT 0,"
            + AccountBean.KEY_FLAG_SELECTED + " INTEGER DEFAULT 0,"
            + AccountBean.KEY_IS_MASTER + " INTEGER DEFAULT 0,"
            + AccountBean.KEY_CURRENCY + " TEXT,"
            + AccountBean.KEY_ID_ICON + " INTEGER,"
            + AccountBean.KEY_TOT_MONEY_INCOME + " INTEGER,"
            + AccountBean.KEY_TOT_MONEY_EXPENSE + " INTEGER"
            + ")";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeInt(openingBalance);
        parcel.writeInt(flagViewTotalBalance);
        parcel.writeInt(flagSelected);
        parcel.writeInt(isMaster);
        parcel.writeString(currency);
        parcel.writeInt(idIcon);
        parcel.writeInt(totMoneyIncome);
        parcel.writeInt(totMoneyExpense);
    }
}
