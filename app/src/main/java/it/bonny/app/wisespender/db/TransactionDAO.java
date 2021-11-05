package it.bonny.app.wisespender.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;

public class TransactionDAO {

    public long insertTransaction(TransactionBean transactionBean, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TransactionBean.KEY_AMOUNT, transactionBean.getAmount());
        values.put(TransactionBean.KEY_DATE_INSERT, transactionBean.getDateInsert());
        values.put(TransactionBean.KEY_NOTE, transactionBean.getNote());
        values.put(TransactionBean.KEY_TYPE_TRANSACTION, transactionBean.getTypeTransaction());
        values.put(TransactionBean.KEY_ID_ACCOUNT, transactionBean.getIdAccount());
        values.put(TransactionBean.KEY_ID_CATEGORY, transactionBean.getIdCategory());
        return db.insert(TransactionBean.TABLE, null, values);
    }

    public long updateTransaction(TransactionBean transactionBean, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TransactionBean.KEY_AMOUNT, transactionBean.getAmount());
        values.put(TransactionBean.KEY_DATE_INSERT, transactionBean.getDateInsert());
        values.put(TransactionBean.KEY_NOTE, transactionBean.getNote());
        values.put(TransactionBean.KEY_TYPE_TRANSACTION, transactionBean.getTypeTransaction());
        values.put(TransactionBean.KEY_ID_ACCOUNT, transactionBean.getIdAccount());
        values.put(TransactionBean.KEY_ID_CATEGORY, transactionBean.getIdCategory());
        return db.update(TransactionBean.TABLE, values, TransactionBean.KEY_ID + " = ?",
                new String[] {String.valueOf(transactionBean.getId())});
    }

    public boolean deleteTransaction(long id, SQLiteDatabase db) {
        boolean result = false;
        try {
            result = db.delete(TransactionBean.TABLE, TransactionBean.KEY_ID + " = ?",
                    new String[] {String.valueOf(id)}) > 0;
        }catch (Exception e) {
            //TODO: Firebase
        }
        return result;
    }

    public TransactionBean getTransactionById(long id, SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " WHERE " + TransactionBean.KEY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        TransactionBean transactionBean = new TransactionBean();
        if(c != null) {
            c.moveToFirst();

            transactionBean.setId(Long.parseLong(c.getString(c.getColumnIndex(TransactionBean.KEY_ID))));
            transactionBean.setAmount(c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT)));
            transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
            transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
            transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
            transactionBean.setIdAccount(c.getInt(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
            transactionBean.setIdCategory(c.getInt(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));

        }
        if(c != null)
            c.close();
        return transactionBean;
    }

    public List<TransactionBean> getAllTransactionIncome(SQLiteDatabase db) {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " t WHERE t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.INCOME;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setId(Long.parseLong(c.getString(c.getColumnIndex(TransactionBean.KEY_ID))));
                transactionBean.setAmount(c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT)));
                transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                transactionBean.setIdAccount(c.getInt(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                transactionBean.setIdCategory(c.getInt(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
                transactionBeans.add(transactionBean);
            }while (c.moveToNext());
        }
        c.close();
        return transactionBeans;
    }

    public List<TransactionBean> getAllTransactionExpense(SQLiteDatabase db) {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " t WHERE t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.EXPENSE;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setId(Long.parseLong(c.getString(c.getColumnIndex(TransactionBean.KEY_ID))));
                transactionBean.setAmount(c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT)));
                transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                transactionBean.setIdAccount(c.getInt(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                transactionBean.setIdCategory(c.getInt(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
                transactionBeans.add(transactionBean);
            }while (c.moveToNext());
        }
        c.close();
        return transactionBeans;
    }

}
