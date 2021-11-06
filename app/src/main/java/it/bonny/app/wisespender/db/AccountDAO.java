package it.bonny.app.wisespender.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.AccountBean;

public class AccountDAO {

    public long updateAccount(AccountBean accountBean, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(AccountBean.KEY_NAME, accountBean.getName());
        values.put(AccountBean.KEY_AMOUNT, accountBean.getAmount());
        values.put(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE, accountBean.getFlagViewTotalBalance());
        values.put(AccountBean.KEY_FLAG_SELECTED, accountBean.getFlagSelected());
        values.put(AccountBean.KEY_IS_MASTER, accountBean.getIsMaster());
        values.put(AccountBean.KEY_CURRENCY, accountBean.getCurrency());
        values.put(AccountBean.KEY_ID_ICON, accountBean.getIdIcon());

        return db.update(AccountBean.TABLE, values, AccountBean.KEY_ID + " = ?",
                new String[] {String.valueOf(accountBean.getId())});
    }

    public boolean deleteAccount(long id, SQLiteDatabase db) {
        boolean result = false;
        try {
            result = db.delete(AccountBean.TABLE, AccountBean.KEY_ID + " = ?",
                    new String[] {String.valueOf(id)}) > 0;
        }catch (Exception e) {
            //TODO: Firebase
        }
        return result;
    }

    public AccountBean getAccountById(long id, SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE + " WHERE " + AccountBean.KEY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        AccountBean accountBean = new AccountBean();
        if(c != null) {
            c.moveToFirst();

            accountBean.setId(Long.parseLong(c.getString(c.getColumnIndex(AccountBean.KEY_ID))));
            accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
            accountBean.setAmount(c.getInt(c.getColumnIndex(AccountBean.KEY_AMOUNT)));
            accountBean.setFlagViewTotalBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE)));
            accountBean.setFlagSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_SELECTED)));
            accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));
            accountBean.setCurrency(c.getString(c.getColumnIndex(AccountBean.KEY_CURRENCY)));
            accountBean.setIdIcon(c.getString(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
        }
        if(c != null)
            c.close();
        return accountBean;
    }

    public List<AccountBean> getAllAccounts(SQLiteDatabase db) {
        List<AccountBean> accountBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                AccountBean accountBean = new AccountBean();
                accountBean.setId(Long.parseLong(c.getString(c.getColumnIndex(AccountBean.KEY_ID))));
                accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
                accountBean.setAmount(c.getInt(c.getColumnIndex(AccountBean.KEY_AMOUNT)));
                accountBean.setFlagViewTotalBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE)));
                accountBean.setFlagSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_FLAG_SELECTED)));
                accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));
                accountBean.setCurrency(c.getString(c.getColumnIndex(AccountBean.KEY_CURRENCY)));
                accountBean.setIdIcon(c.getString(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
                accountBeans.add(accountBean);
            }while (c.moveToNext());
        }
        c.close();
        return accountBeans;
    }

}
