package it.bonny.app.wisespender.db;

public class DatabaseStrings {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "wiseSpender";
    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS ";

    //Table Account
    public static final String TABLE_ACCOUNT = "account";
    public static final String KEY_ACCOUNT_ID = "id";
    public static final String KEY_ACCOUNT_NAME = "name";
    public static final String KEY_ACCOUNT_AMOUNT = "amount";
    public static final String KEY_ACCOUNT_VIEW_TOTAL_BALANCE = "view_total_balance";

}
