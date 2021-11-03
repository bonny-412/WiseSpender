package it.bonny.app.wisespender.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.bonny.app.wisespender.bean.Account;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Create table ACCOUNT
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + DatabaseStrings.TABLE_ACCOUNT
            + "("
                + DatabaseStrings.KEY_ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + DatabaseStrings.KEY_ACCOUNT_NAME + " TEXT,"
                + DatabaseStrings.KEY_ACCOUNT_AMOUNT + " INTEGER,"
                + DatabaseStrings.KEY_ACCOUNT_VIEW_TOTAL_BALANCE + " INTEGER DEFAULT 0"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DatabaseStrings.DATABASE_NAME, null, DatabaseStrings.DATABASE_VERSION);
    }

    /**
     * Creating a Account
     */
    public long createAccount(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseStrings.KEY_ACCOUNT_NAME, account.getName());
        values.put(DatabaseStrings.KEY_ACCOUNT_AMOUNT, account.getAmount());
        values.put(DatabaseStrings.KEY_ACCOUNT_VIEW_TOTAL_BALANCE, account.getViewTotalBalance());

        return db.insert(DatabaseStrings.TABLE_ACCOUNT, null, values);
    }

    /**
     *
     * @param id
     * @return Account
     */
    public Account getAccountById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseStrings.TABLE_ACCOUNT + " WHERE " + DatabaseStrings.KEY_ACCOUNT_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        Account account = new Account();
        try {
            if(c != null) {
                c.moveToFirst();

                account.setId(c.getInt(c.getColumnIndex(DatabaseStrings.KEY_ACCOUNT_ID)));
                account.setName(c.getString(c.getColumnIndex(DatabaseStrings.KEY_ACCOUNT_NAME)));
                account.setAmount(c.getInt(c.getColumnIndex(DatabaseStrings.KEY_ACCOUNT_AMOUNT)));
                account.setViewTotalBalance(c.getInt(c.getColumnIndex(DatabaseStrings.KEY_ACCOUNT_VIEW_TOTAL_BALANCE)));
            }
        }catch (Exception e) {
            //TODO: Firebase
        }finally {
            if(c != null)
                c.close();
        }


        return account;
    }

    /**
     *
     */


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //On upgrade drop older tables
        db.execSQL(DatabaseStrings.DROP_TABLE_SQL + DatabaseStrings.TABLE_ACCOUNT);

        //Create new tables
        onCreate(db);
    }
}
