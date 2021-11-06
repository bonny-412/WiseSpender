package it.bonny.app.wisespender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wiseSpender";

    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + AccountBean.TABLE
            + "("
            + AccountBean.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + AccountBean.KEY_NAME + " TEXT,"
            + AccountBean.KEY_AMOUNT + " INTEGER,"
            + AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE + " INTEGER DEFAULT 0,"
            + AccountBean.KEY_FLAG_SELECTED + " INTEGER DEFAULT 0,"
            + AccountBean.KEY_IS_MASTER + " INTEGER DEFAULT 0,"
            + AccountBean.KEY_CURRENCY + " TEXT,"
            + AccountBean.KEY_ID_ICON + " TEXT"
            + ")";
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + CategoryBean.TABLE
            + "("
            + CategoryBean.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + CategoryBean.KEY_NAME + " TEXT,"
            //+ CategoryBean.KEY_LIMIT_CASH + " INTEGER,"
            + CategoryBean.KEY_TYPE_CATEGORY + " INTEGER,"
            + CategoryBean.KEY_ID_ICON + " INTEGER"
            + ")";
    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TransactionBean.TABLE
            + "("
            + TransactionBean.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + TransactionBean.KEY_AMOUNT + " INTEGER,"
            + TransactionBean.KEY_DATE_INSERT + " DATETIME,"
            + TransactionBean.KEY_NOTE + " TEXT,"
            + TransactionBean.KEY_TYPE_TRANSACTION + " INTEGER,"
            + TransactionBean.KEY_ID_ACCOUNT + " INTEGER,"
            + TransactionBean.KEY_ID_CATEGORY + " INTEGER"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AccountBean.TABLE);

        // create new tables
        onCreate(sqLiteDatabase);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /*public List<String> getAllTableFromDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> stringList = new ArrayList<>();
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                String a = c.getString(0);
                stringList.add(a);
                c.moveToNext();
            }
        }
        return stringList;
    }*/

    // ------------------------ "account" table methods ----------------//
    /**
     * Creating a Account
     */
    public long insertAccountBean(AccountBean accountBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountBean.KEY_NAME, accountBean.getName());
        values.put(AccountBean.KEY_CURRENCY, accountBean.getCurrency());
        values.put(AccountBean.KEY_AMOUNT, accountBean.getAmount());
        values.put(AccountBean.KEY_FLAG_SELECTED, accountBean.getFlagSelected());
        values.put(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE, accountBean.getFlagViewTotalBalance());
        values.put(AccountBean.KEY_IS_MASTER, accountBean.getIsMaster());
        values.put(AccountBean.KEY_ID_ICON, accountBean.getIdIcon());

        return db.insertOrThrow(AccountBean.TABLE, null, values);
    }

    /**
     * Updating a Account
     */
    public int updateAccountBean(AccountBean accountBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountBean.KEY_NAME, accountBean.getName());
        values.put(AccountBean.KEY_CURRENCY, accountBean.getCurrency());
        values.put(AccountBean.KEY_AMOUNT, accountBean.getAmount());
        values.put(AccountBean.KEY_FLAG_SELECTED, accountBean.getFlagSelected());
        values.put(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE, accountBean.getFlagViewTotalBalance());
        values.put(AccountBean.KEY_IS_MASTER, accountBean.getIsMaster());
        values.put(AccountBean.KEY_ID_ICON, accountBean.getIdIcon());

        return db.update(AccountBean.TABLE, values, AccountBean.KEY_ID + " = ?",
                new String[] {String.valueOf(accountBean.getId())});
    }

    /**
     * Deleting a Account
     */
    public void deleteAccountBean(long accountId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AccountBean.TABLE, AccountBean.KEY_ID + " = ?",new String[] {String.valueOf(accountId)});
    }

    /**
     * Getting single Account
     */
    public AccountBean getAccountBean(long account_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AccountBean accountBean = new AccountBean();
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE + " WHERE " + AccountBean.KEY_ID + " = " + account_id;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c.moveToFirst();

            accountBean.setId(Long.parseLong(c.getString(c.getColumnIndex(AccountBean.KEY_ID))));
            accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
            accountBean.setCurrency(c.getString(c.getColumnIndex(AccountBean.KEY_CURRENCY)));
            accountBean.setIdIcon(c.getString(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
            accountBean.setAmount(c.getInt(c.getColumnIndex(AccountBean.KEY_AMOUNT)));
            accountBean.setFlagViewTotalBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE)));
            accountBean.setFlagSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_SELECTED)));
            accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));
        }
        if(c != null)
            c.close();
        return accountBean;
    }

    /**
     * Getting all Accounts
     */
    public List<AccountBean> getAllAccountBeans() {
        List<AccountBean> accountBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AccountBean accountBean = new AccountBean();
                accountBean.setId(Long.parseLong(c.getString(c.getColumnIndex(AccountBean.KEY_ID))));
                accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
                accountBean.setCurrency(c.getString(c.getColumnIndex(AccountBean.KEY_CURRENCY)));
                accountBean.setIdIcon(c.getString(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
                accountBean.setAmount(c.getInt(c.getColumnIndex(AccountBean.KEY_AMOUNT)));
                accountBean.setFlagViewTotalBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE)));
                accountBean.setFlagSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_SELECTED)));
                accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));
                accountBeans.add(accountBean);
            } while (c.moveToNext());
        }
        c.close();
        return accountBeans;
    }

    /**
     * Getting all Account no master
     */
    public List<AccountBean> getAllAccountBeansNoMaster() {
        List<AccountBean> accountBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE + " a WHERE a." + AccountBean.KEY_IS_MASTER + " = " + TypeObjectBean.NO_MASTER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AccountBean accountBean = new AccountBean();
                accountBean.setId(Long.parseLong(c.getString(c.getColumnIndex(AccountBean.KEY_ID))));
                accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
                accountBean.setCurrency(c.getString(c.getColumnIndex(AccountBean.KEY_CURRENCY)));
                accountBean.setIdIcon(c.getString(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
                accountBean.setAmount(c.getInt(c.getColumnIndex(AccountBean.KEY_AMOUNT)));
                accountBean.setFlagViewTotalBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE)));
                accountBean.setFlagSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_SELECTED)));
                accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));
                accountBeans.add(accountBean);
            } while (c.moveToNext());
        }
        c.close();
        return accountBeans;
    }

    /**
     * Getting single Account
     */
    public AccountBean getAccountBeanSelected() {
        SQLiteDatabase db = this.getReadableDatabase();
        AccountBean accountBean = new AccountBean();
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE + " WHERE " + AccountBean.KEY_FLAG_SELECTED + " = " + TypeObjectBean.SELECTED;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c.moveToFirst();

            accountBean.setId(Long.parseLong(c.getString(c.getColumnIndex(AccountBean.KEY_ID))));
            accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
            accountBean.setCurrency(c.getString(c.getColumnIndex(AccountBean.KEY_CURRENCY)));
            accountBean.setIdIcon(c.getString(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
            accountBean.setAmount(c.getInt(c.getColumnIndex(AccountBean.KEY_AMOUNT)));
            accountBean.setFlagViewTotalBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE)));
            accountBean.setFlagSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_SELECTED)));
            accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));

            c.close();
        }
        return accountBean;
    }

}
