package it.bonny.app.wisespender.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.Account;

public class AccountDAO {

    public long insertAccount(Account account, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Account.KEY_NAME, account.getName());
        values.put(Account.KEY_AMOUNT, account.getAmount());
        values.put(Account.KEY_FLAG_VIEW_TOTAL_BALANCE, account.getFlagViewTotalBalance());
        values.put(Account.KEY_FLAG_SELECTED, account.getFlagSelected());

        return db.insert(Account.TABLE, null, values);
    }

    public int updateAccount(Account account, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Account.KEY_NAME, account.getName());
        values.put(Account.KEY_AMOUNT, account.getAmount());
        values.put(Account.KEY_FLAG_VIEW_TOTAL_BALANCE, account.getFlagViewTotalBalance());
        values.put(Account.KEY_FLAG_SELECTED, account.getFlagSelected());

        return db.update(Account.TABLE, values, Account.KEY_ID + " = ?",
                new String[] {String.valueOf(account.getId())});
    }

    public void deleteAccount(long id, SQLiteDatabase db) {
        db.delete(Account.TABLE, Account.KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    public Account getAccountById(long id, SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM " + Account.TABLE + " WHERE " + Account.KEY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        Account account = new Account();
        if(c != null) {
            c.moveToFirst();

            account.setId(c.getInt(c.getColumnIndex(Account.KEY_ID)));
            account.setName(c.getString(c.getColumnIndex(Account.KEY_NAME)));
            account.setAmount(c.getInt(c.getColumnIndex(Account.KEY_AMOUNT)));
            account.setFlagViewTotalBalance(c.getInt(c.getColumnIndex(Account.KEY_FLAG_VIEW_TOTAL_BALANCE)));
            account.setFlagSelected(c.getInt(c.getColumnIndex(Account.KEY_FLAG_SELECTED)));
        }
        if(c != null)
            c.close();
        return account;
    }

    public List<Account> getAllAccounts(SQLiteDatabase db) {
        List<Account> accounts = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Account.TABLE;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                Account account = new Account();
                account.setId(c.getInt(c.getColumnIndex(Account.KEY_ID)));
                account.setName(c.getString(c.getColumnIndex(Account.KEY_NAME)));
                account.setAmount(c.getInt(c.getColumnIndex(Account.KEY_AMOUNT)));
                account.setFlagViewTotalBalance(c.getInt(c.getColumnIndex(Account.KEY_FLAG_VIEW_TOTAL_BALANCE)));
                account.setFlagSelected(c.getInt(c.getColumnIndex(Account.KEY_FLAG_SELECTED)));
                accounts.add(account);
            }while (c.moveToNext());
        }
        c.close();
        return accounts;
    }

}
