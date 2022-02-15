package it.bonny.app.wisespender.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountBean implements Parcelable {

    public static final String TABLE = "account";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_OPENING_BALANCE = "openingBalance";
    public static final String KEY_IS_INCLUDED_BALANCE = "is_included_balance";
    public static final String KEY_IS_SELECTED = "is_selected";
    public static final String KEY_IS_MASTER = "is_master";
    public static final String KEY_ID_ICON = "id_icon";

    private long id;
    private String name;
    private int openingBalance;
    private int isIncludedBalance;
    private int isSelected;
    private int isMaster;
    private int idIcon;

    public AccountBean() {}

    protected AccountBean(Parcel in) {
        id = in.readLong();
        name = in.readString();
        openingBalance = in.readInt();
        isIncludedBalance = in.readInt();
        isSelected = in.readInt();
        isMaster = in.readInt();
        idIcon = in.readInt();
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

    public int getIsIncludedBalance() {
        return isIncludedBalance;
    }
    public void setIsIncludedBalance(int isIncludedBalance) {
        this.isIncludedBalance = isIncludedBalance;
    }

    public int getIsSelected() {
        return isSelected;
    }
    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public int getIsMaster() {
        return isMaster;
    }
    public void setIsMaster(int isMaster) {
        this.isMaster = isMaster;
    }

    public int getIdIcon() {
        return idIcon;
    }
    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }

    public static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + AccountBean.TABLE
            + "("
            + AccountBean.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + AccountBean.KEY_NAME + " TEXT,"
            + AccountBean.KEY_OPENING_BALANCE + " INTEGER,"
            + AccountBean.KEY_IS_INCLUDED_BALANCE + " INTEGER DEFAULT 1,"
            + AccountBean.KEY_IS_SELECTED + " INTEGER DEFAULT 0,"
            + AccountBean.KEY_IS_MASTER + " INTEGER DEFAULT 0,"
            + AccountBean.KEY_ID_ICON + " INTEGER"
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
        parcel.writeInt(isIncludedBalance);
        parcel.writeInt(isSelected);
        parcel.writeInt(isMaster);
        parcel.writeInt(idIcon);
    }
}
