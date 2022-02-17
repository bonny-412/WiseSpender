package it.bonny.app.wisespender.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.FilterTransactionBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WiseSpender";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(AccountBean.CREATE_TABLE_ACCOUNT);
        sqLiteDatabase.execSQL(CategoryBean.CREATE_TABLE_CATEGORY);
        sqLiteDatabase.execSQL(TransactionBean.CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + AccountBean.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryBean.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransactionBean.TABLE);

        // create new tables
        onCreate(sqLiteDatabase);
    }

    public List<String> getAllTableFromDB() {
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
        db.close();
        return stringList;
    }

    // ------------------------ "account" table methods ----------------//
    /**
     * Creating a Account
     */
    public long insertAccountBean(AccountBean accountBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountBean.KEY_NAME, accountBean.getName());
        values.put(AccountBean.KEY_OPENING_BALANCE, accountBean.getOpeningBalance());
        values.put(AccountBean.KEY_IS_SELECTED, accountBean.getIsSelected());
        values.put(AccountBean.KEY_IS_INCLUDED_BALANCE, accountBean.getIsIncludedBalance());
        values.put(AccountBean.KEY_IS_MASTER, accountBean.getIsMaster());
        values.put(AccountBean.KEY_ID_ICON, accountBean.getIdIcon());

        long id = db.insert(AccountBean.TABLE, null, values);
        db.close();
        return id;
    }

    /**
     * Updating a Account
     */
    public void updateAccountBean(AccountBean accountBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountBean.KEY_NAME, accountBean.getName());
        values.put(AccountBean.KEY_OPENING_BALANCE, accountBean.getOpeningBalance());
        values.put(AccountBean.KEY_IS_SELECTED, accountBean.getIsSelected());
        values.put(AccountBean.KEY_IS_INCLUDED_BALANCE, accountBean.getIsIncludedBalance());
        values.put(AccountBean.KEY_IS_MASTER, accountBean.getIsMaster());
        values.put(AccountBean.KEY_ID_ICON, accountBean.getIdIcon());

        db.update(AccountBean.TABLE, values, AccountBean.KEY_ID + "=?",
                new String[] {Long.toString(accountBean.getId())});
        db.close();
    }

    /**
     * Deleting a Account
     */
    public boolean deleteAccountBean(long accountId) {
        boolean result;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            result = db.delete(AccountBean.TABLE, AccountBean.KEY_ID + "=?",new String[] {Long.toString(accountId)}) > 0;
            db.close();
        }catch (Exception e) {
            //TODO: Firebase
            result = false;
        }
        return result;
    }

    /**
     * Getting single Account
     */
    @SuppressLint("Range")
    public AccountBean getAccountBean(long account_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AccountBean accountBean = null;
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE + " WHERE " + AccountBean.KEY_ID + " = " + account_id;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                accountBean = new AccountBean();
                accountBean.setId(c.getLong(c.getColumnIndex(AccountBean.KEY_ID)));
                accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
                accountBean.setIdIcon(c.getInt(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
                accountBean.setOpeningBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_OPENING_BALANCE)));
                accountBean.setIsIncludedBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_INCLUDED_BALANCE)));
                accountBean.setIsSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_SELECTED)));
                accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));
            }while (c.moveToNext());
        }
        c.close();
        return accountBean;
    }

    /**
     * Getting all Accounts
     */
    @SuppressLint("Range")
    public List<AccountBean> getAllAccountBeans() {
        List<AccountBean> accountBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AccountBean accountBean = new AccountBean();
                accountBean.setId(c.getLong(c.getColumnIndex(AccountBean.KEY_ID)));
                accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
                accountBean.setIdIcon(c.getInt(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
                accountBean.setOpeningBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_OPENING_BALANCE)));
                accountBean.setIsIncludedBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_INCLUDED_BALANCE)));
                accountBean.setIsSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_SELECTED)));
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
    @SuppressLint("Range")
    public List<AccountBean> getAllAccountBeansNoMaster() {
        List<AccountBean> accountBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE + " a WHERE a." + AccountBean.KEY_IS_MASTER + " = " + TypeObjectBean.NO_MASTER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AccountBean accountBean = new AccountBean();
                accountBean.setId(c.getLong(c.getColumnIndex(AccountBean.KEY_ID)));
                accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
                accountBean.setIdIcon(c.getInt(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
                accountBean.setOpeningBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_OPENING_BALANCE)));
                accountBean.setIsIncludedBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_INCLUDED_BALANCE)));
                accountBean.setIsSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_SELECTED)));
                accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));
                accountBeans.add(accountBean);
            } while (c.moveToNext());
        }
        c.close();
        return accountBeans;
    }

    /**
     * Getting first Account no master
     */
    @SuppressLint("Range")
    public List<AccountBean> getFirstAccountBeanNoMaster() {
        List<AccountBean> accountBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE + " a WHERE a." + AccountBean.KEY_IS_MASTER + " = " + TypeObjectBean.NO_MASTER
            + " ORDER BY a." + AccountBean.KEY_ID + " ASC LIMIT 1";;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                AccountBean accountBean = new AccountBean();
                accountBean.setId(c.getLong(c.getColumnIndex(AccountBean.KEY_ID)));
                accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
                accountBean.setIdIcon(c.getInt(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
                accountBean.setOpeningBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_OPENING_BALANCE)));
                accountBean.setIsIncludedBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_INCLUDED_BALANCE)));
                accountBean.setIsSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_SELECTED)));
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
    @SuppressLint("Range")
    public AccountBean getAccountBeanSelected() {
        SQLiteDatabase db = this.getReadableDatabase();
        AccountBean accountBean = null;
        String selectQuery = "SELECT * FROM " + AccountBean.TABLE + " WHERE " + AccountBean.KEY_IS_SELECTED + " = " + TypeObjectBean.SELECTED;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                accountBean = new AccountBean();
                accountBean.setId(c.getLong(c.getColumnIndex(AccountBean.KEY_ID)));
                accountBean.setName(c.getString(c.getColumnIndex(AccountBean.KEY_NAME)));
                accountBean.setIdIcon(c.getInt(c.getColumnIndex(AccountBean.KEY_ID_ICON)));
                accountBean.setOpeningBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_OPENING_BALANCE)));
                accountBean.setIsIncludedBalance(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_INCLUDED_BALANCE)));
                accountBean.setIsSelected(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_SELECTED)));
                accountBean.setIsMaster(c.getInt(c.getColumnIndex(AccountBean.KEY_IS_MASTER)));
            }while (c.moveToNext());
        }
        c.close();
        return accountBean;
    }

    /**
     * Get all idAccount no Master
     */
    @SuppressLint("Range")
    public String getAllIdAccountNoMaster() {
        StringBuilder stringBuilder = new StringBuilder();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT a." + AccountBean.KEY_ID + " AS id FROM " + AccountBean.TABLE + " a "
                + "WHERE a." + AccountBean.KEY_IS_MASTER + " = " + TypeObjectBean.NO_MASTER + " "
                + "AND a." + AccountBean.KEY_IS_INCLUDED_BALANCE + " = " + TypeObjectBean.IS_INCLUDED_BALANCE;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                stringBuilder.append("").append(c.getString(c.getColumnIndex("id"))).append(",");
            }while (c.moveToNext());
        }
        c.close();
        String ids = stringBuilder.toString();
        ids = ids.substring(0, ids.length() - 1);
        return ids;
    }

    /**
     * Get count Account no Master
     */
    @SuppressLint("Range")
    public int countAccountNoMaster() {
        int countAccount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT COUNT(*) AS countAccount FROM " + AccountBean.TABLE + " a " +
                "WHERE a." + AccountBean.KEY_IS_MASTER + "=" + TypeObjectBean.NO_MASTER;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            countAccount = c.getInt(c.getColumnIndex("countAccount"));
        }
        c.close();
        return countAccount;
    }

    // ------------------------ "category" table methods ----------------//
    /**
     * Creating a Category
     */
    public long insertCategoryBean(CategoryBean categoryBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategoryBean.KEY_NAME, categoryBean.getName());
        values.put(CategoryBean.KEY_TYPE_CATEGORY, categoryBean.getTypeCategory());
        values.put(CategoryBean.KEY_ID_ICON, categoryBean.getIdIcon());

        long id = db.insert(CategoryBean.TABLE, null, values);
        db.close();
        return id;
    }

    /**
     * Updating a Category
     */
    public void updateCategoryBean(CategoryBean categoryBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategoryBean.KEY_NAME, categoryBean.getName());
        values.put(CategoryBean.KEY_TYPE_CATEGORY, categoryBean.getTypeCategory());
        values.put(CategoryBean.KEY_ID_ICON, categoryBean.getIdIcon());

        db.update(CategoryBean.TABLE, values, CategoryBean.KEY_ID + "=?",
                new String[] {Long.toString(categoryBean.getId())});
        db.close();
    }

    /**
     * Deleting a Category
     */
    public boolean deleteCategoryBean(long categoryId) {
        boolean result;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            result = db.delete(CategoryBean.TABLE, CategoryBean.KEY_ID + "=?",new String[] {Long.toString(categoryId)}) > 0;
            db.close();
        }catch (Exception e) {
            //TODO: Firebase
            result = false;
        }
        return result;
    }

    /**
     * Getting single Category
     */
    @SuppressLint("Range")
    public CategoryBean getCategoryBean(long categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        CategoryBean categoryBean = null;
        String selectQuery = "SELECT * FROM " + CategoryBean.TABLE + " WHERE " + CategoryBean.KEY_ID + " = " + categoryId;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                categoryBean = new CategoryBean();
                categoryBean.setId(c.getLong(c.getColumnIndex(CategoryBean.KEY_ID)));
                categoryBean.setName(c.getString(c.getColumnIndex(CategoryBean.KEY_NAME)));
                categoryBean.setTypeCategory(c.getInt(c.getColumnIndex(CategoryBean.KEY_TYPE_CATEGORY)));
                categoryBean.setIdIcon(c.getInt(c.getColumnIndex(CategoryBean.KEY_ID_ICON)));
            }while (c.moveToNext());
        }
        c.close();
        return categoryBean;
    }

    /**
     * Getting single Category
     */
    @SuppressLint("Range")
    public CategoryBean getCategoryBeanOpeningBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        CategoryBean categoryBean = null;
        String selectQuery = "SELECT * FROM " + CategoryBean.TABLE + " WHERE " + CategoryBean.KEY_TYPE_CATEGORY + " = " + TypeObjectBean.CATEGORY_OPEN_BALANCE;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                categoryBean = new CategoryBean();
                categoryBean.setId(c.getLong(c.getColumnIndex(CategoryBean.KEY_ID)));
                categoryBean.setName(c.getString(c.getColumnIndex(CategoryBean.KEY_NAME)));
                categoryBean.setTypeCategory(c.getInt(c.getColumnIndex(CategoryBean.KEY_TYPE_CATEGORY)));
                categoryBean.setIdIcon(c.getInt(c.getColumnIndex(CategoryBean.KEY_ID_ICON)));
            }while (c.moveToNext());
        }
        c.close();
        return categoryBean;
    }

    /**
     * Getting single Category
     */
    @SuppressLint("Range")
    public CategoryBean getCategoryBeanTransfer() {
        SQLiteDatabase db = this.getReadableDatabase();
        CategoryBean categoryBean = null;
        String selectQuery = "SELECT * FROM " + CategoryBean.TABLE + " WHERE " + CategoryBean.KEY_TYPE_CATEGORY + " = " + TypeObjectBean.CATEGORY_TRANSFER;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                categoryBean = new CategoryBean();
                categoryBean.setId(c.getLong(c.getColumnIndex(CategoryBean.KEY_ID)));
                categoryBean.setName(c.getString(c.getColumnIndex(CategoryBean.KEY_NAME)));
                categoryBean.setTypeCategory(c.getInt(c.getColumnIndex(CategoryBean.KEY_TYPE_CATEGORY)));
                categoryBean.setIdIcon(c.getInt(c.getColumnIndex(CategoryBean.KEY_ID_ICON)));
            }while (c.moveToNext());
        }
        c.close();
        return categoryBean;
    }

    /**
     * Getting all Categories
     */
    @SuppressLint("Range")
    public List<CategoryBean> getAllCategoryBeans() {
        List<CategoryBean> categoryBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CategoryBean.TABLE + " c WHERE c."
                + CategoryBean.KEY_TYPE_CATEGORY + " != " + TypeObjectBean.CATEGORY_OPEN_BALANCE
                + " AND c." + CategoryBean.KEY_TYPE_CATEGORY + " != " + TypeObjectBean.CATEGORY_TRANSFER
                + " ORDER BY c." + CategoryBean.KEY_TYPE_CATEGORY
                + ", c." + CategoryBean.KEY_NAME + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setId(c.getLong(c.getColumnIndex(CategoryBean.KEY_ID)));
                categoryBean.setName(c.getString(c.getColumnIndex(CategoryBean.KEY_NAME)));
                categoryBean.setTypeCategory(c.getInt(c.getColumnIndex(CategoryBean.KEY_TYPE_CATEGORY)));
                categoryBean.setIdIcon(c.getInt(c.getColumnIndex(CategoryBean.KEY_ID_ICON)));
                categoryBeans.add(categoryBean);
            } while (c.moveToNext());
        }
        c.close();
        return categoryBeans;
    }

    /**
     * Getting all Categories
     */
    @SuppressLint("Range")
    public List<CategoryBean> getAllCategoryBeansToTypeCategory(int typeCategory) {
        List<CategoryBean> categoryBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CategoryBean.TABLE + " c WHERE c." + CategoryBean.KEY_TYPE_CATEGORY + " = " + typeCategory + " ORDER BY c." + CategoryBean.KEY_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setId(c.getLong(c.getColumnIndex(CategoryBean.KEY_ID)));
                categoryBean.setName(c.getString(c.getColumnIndex(CategoryBean.KEY_NAME)));
                categoryBean.setTypeCategory(c.getInt(c.getColumnIndex(CategoryBean.KEY_TYPE_CATEGORY)));
                categoryBean.setIdIcon(c.getInt(c.getColumnIndex(CategoryBean.KEY_ID_ICON)));
                categoryBeans.add(categoryBean);
            } while (c.moveToNext());
        }
        c.close();
        return categoryBeans;
    }

    // ------------------------ "transaction" table methods ----------------//
    /**
     * Creating a Transaction
     */
    public long insertTransactionBean(TransactionBean transactionBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TransactionBean.KEY_TITLE, transactionBean.getTitle());
        values.put(TransactionBean.KEY_AMOUNT, transactionBean.getAmount());
        values.put(TransactionBean.KEY_DATE_INSERT, transactionBean.getDateInsert());
        values.put(TransactionBean.KEY_NOTE, transactionBean.getNote());
        values.put(TransactionBean.KEY_TYPE_TRANSACTION, transactionBean.getTypeTransaction());
        values.put(TransactionBean.KEY_ID_ACCOUNT, transactionBean.getIdAccount());
        values.put(TransactionBean.KEY_ID_TRANSACTION_TRANSFER, transactionBean.getIdTransactionTransfer());
        values.put(TransactionBean.KEY_ID_CATEGORY, transactionBean.getIdCategory());

        long id = db.insert(TransactionBean.TABLE, null, values);
        db.close();
        return id;
    }

    /**
     * Updating a Transaction
     */
    public int updateTransactionBean(TransactionBean transactionBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TransactionBean.KEY_TITLE, transactionBean.getTitle());
        values.put(TransactionBean.KEY_AMOUNT, transactionBean.getAmount());
        values.put(TransactionBean.KEY_DATE_INSERT, transactionBean.getDateInsert());
        values.put(TransactionBean.KEY_NOTE, transactionBean.getNote());
        values.put(TransactionBean.KEY_TYPE_TRANSACTION, transactionBean.getTypeTransaction());
        values.put(TransactionBean.KEY_ID_ACCOUNT, transactionBean.getIdAccount());
        values.put(TransactionBean.KEY_ID_TRANSACTION_TRANSFER, transactionBean.getIdTransactionTransfer());
        values.put(TransactionBean.KEY_ID_CATEGORY, transactionBean.getIdCategory());

        int a = db.update(TransactionBean.TABLE, values, TransactionBean.KEY_ID + "=?",
                new String[] {Long.toString(transactionBean.getId())});
        db.close();
        return a;
    }

    /**
     * Deleting a Transaction
     */
    public boolean deleteTransactionBean(long transactionId) {
        boolean result;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            result = db.delete(TransactionBean.TABLE, TransactionBean.KEY_ID + "=?",new String[] {Long.toString(transactionId)}) > 0;
            db.close();
        }catch (Exception e) {
            //TODO: Firebase
            result = false;
        }
        return result;
    }

    /**
     * Getting single Transaction
     */
    @SuppressLint("Range")
    public TransactionBean getTransactionBean(long transactionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        TransactionBean transactionBean = null;
        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " WHERE " + TransactionBean.KEY_ID + " = " + transactionId;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                transactionBean = new TransactionBean();
                transactionBean.setId(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID)));
                transactionBean.setTitle(c.getString(c.getColumnIndex(TransactionBean.KEY_TITLE)));
                transactionBean.setAmount(c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT)));
                transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                transactionBean.setIdAccount(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                transactionBean.setIdTransactionTransfer(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_TRANSACTION_TRANSFER)));
                transactionBean.setIdCategory(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
            }while (c.moveToNext());
        }
        c.close();
        return transactionBean;
    }

    /**
     * Getting all Transactions by TypeTransaction
     */
    @SuppressLint("Range")
    public List<TransactionBean> getAllTransactionBeansToTypeTransaction(int typeTransaction) {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " t WHERE t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + typeTransaction + " ORDER BY t." + TransactionBean.KEY_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setId(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID)));
                transactionBean.setTitle(c.getString(c.getColumnIndex(TransactionBean.KEY_TITLE)));
                transactionBean.setAmount(c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT)));
                transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                transactionBean.setIdAccount(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                transactionBean.setIdTransactionTransfer(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_TRANSACTION_TRANSFER)));
                transactionBean.setIdCategory(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
                transactionBeans.add(transactionBean);
            } while (c.moveToNext());
        }
        c.close();
        return transactionBeans;
    }

    /**
     * Getting all Transactions by Account
     */
    @SuppressLint("Range")
    public List<TransactionBean> getAllTransactionBeansByAccount(long idAccount) {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " t WHERE t." + TransactionBean.KEY_ID_ACCOUNT + " = " + idAccount + " ORDER BY t." + TransactionBean.KEY_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setId(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID)));
                transactionBean.setTitle(c.getString(c.getColumnIndex(TransactionBean.KEY_TITLE)));
                transactionBean.setAmount(c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT)));
                transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                transactionBean.setIdAccount(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                transactionBean.setIdTransactionTransfer(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_TRANSACTION_TRANSFER)));
                transactionBean.setIdCategory(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
                transactionBeans.add(transactionBean);
            } while (c.moveToNext());
        }
        c.close();
        return transactionBeans;
    }

    /**
     * Getting all Transactions by Category
     */
    @SuppressLint("Range")
    public List<TransactionBean> getAllTransactionBeansByCategory(long idCategory) {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " t WHERE t." + TransactionBean.KEY_ID_CATEGORY + " = " + idCategory + " ORDER BY t." + TransactionBean.KEY_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                TransactionBean transactionBean = new TransactionBean();
                transactionBean.setId(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID)));
                transactionBean.setTitle(c.getString(c.getColumnIndex(TransactionBean.KEY_TITLE)));
                transactionBean.setAmount(c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT)));
                transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                transactionBean.setIdAccount(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                transactionBean.setIdTransactionTransfer(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_TRANSACTION_TRANSFER)));
                transactionBean.setIdCategory(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
                transactionBeans.add(transactionBean);
            } while (c.moveToNext());
        }
        c.close();
        return transactionBeans;
    }

    /**
     * Getting all Transactions by TypeTransaction, IdAccount
     */
    @SuppressLint("Range")
    public TransactionBean getAllTransactionBeansByTypeTransactionIdAccount(int typeTransaction, long idAccount) {
        TransactionBean transactionBean = null;
        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " t WHERE t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " +
                typeTransaction + " OR t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_OPEN_BALANCE + " " +
                "AND t." + TransactionBean.KEY_ID_ACCOUNT + " = " + idAccount;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                transactionBean = new TransactionBean();
                transactionBean.setId(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID)));
                transactionBean.setTitle(c.getString(c.getColumnIndex(TransactionBean.KEY_TITLE)));
                transactionBean.setAmount(c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT)));
                transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                transactionBean.setIdAccount(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                transactionBean.setIdTransactionTransfer(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_TRANSACTION_TRANSFER)));
                transactionBean.setIdCategory(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
            }while (c.moveToNext());
        }
        c.close();
        return transactionBean;
    }

    /**
     * Getting all Transactions To MainActivity
     */
    @SuppressLint("Range")
    public List<TransactionBean> getAllTransactionBeansToMainActivity(AccountBean accountBeanSelected, String from, String a) {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        String columns = "t." + TransactionBean.KEY_ID + ", t." + TransactionBean.KEY_AMOUNT + ", t." + TransactionBean.KEY_TYPE_TRANSACTION + ", t."
                + TransactionBean.KEY_ID_TRANSACTION_TRANSFER + ", t." + TransactionBean.KEY_TITLE + ", t." + TransactionBean.KEY_ID_ACCOUNT + ", t."
                + TransactionBean.KEY_DATE_INSERT + ", t." + TransactionBean.KEY_ID_CATEGORY  + ", t." + TransactionBean.KEY_NOTE;
        String selectQuery = "SELECT " + columns + " FROM " + TransactionBean.TABLE + " t ";

        if(accountBeanSelected != null && accountBeanSelected.getIsMaster() == TypeObjectBean.IS_MASTER)
            selectQuery += "INNER JOIN " + AccountBean.TABLE + " a ON a." + AccountBean.KEY_ID + " = t." + TransactionBean.KEY_ID_ACCOUNT + " ";

        selectQuery += "WHERE t." + TransactionBean.KEY_DATE_INSERT + " BETWEEN '" + from + "' AND '" + a + "' ";

        if(accountBeanSelected != null && accountBeanSelected.getIsMaster() == TypeObjectBean.IS_MASTER)
            selectQuery += "AND a." + AccountBean.KEY_IS_INCLUDED_BALANCE + " = " + TypeObjectBean.IS_INCLUDED_BALANCE + " ";

        if(accountBeanSelected != null && accountBeanSelected.getIsMaster() == TypeObjectBean.NO_MASTER)
            selectQuery += "AND t." + TransactionBean.KEY_ID_ACCOUNT + " = " + accountBeanSelected.getId() + " ";

        selectQuery += "ORDER BY t." + TransactionBean.KEY_ID + " DESC LIMIT 7";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                int amount = c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT));
                if(amount > 0) {
                    TransactionBean transactionBean = new TransactionBean();
                    long b = c.getLong(c.getColumnIndex(TransactionBean.KEY_ID));
                    transactionBean.setId(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID)));
                    transactionBean.setTitle(c.getString(c.getColumnIndex(TransactionBean.KEY_TITLE)));
                    transactionBean.setAmount(amount);
                    transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                    transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                    transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                    transactionBean.setIdAccount(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                    transactionBean.setIdTransactionTransfer(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_TRANSACTION_TRANSFER)));
                    transactionBean.setIdCategory(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
                    transactionBeans.add(transactionBean);
                }
            } while (c.moveToNext());
        }
        c.close();
        return transactionBeans;
    }

    @SuppressLint("Range")
    public int getSumTransactionsByAccountAndType(long idAccountBeanSelected, String ids, String from, String a, int typeTransaction) {
        int total = 0;
        String selectQuery = "SELECT SUM(" + TransactionBean.KEY_AMOUNT + ") AS total FROM " + TransactionBean.TABLE + " t WHERE t." + TransactionBean.KEY_DATE_INSERT +
                " BETWEEN '" + from + "' AND '" + a + "' ";

        if(typeTransaction == TypeObjectBean.TRANSACTION_INCOME) {
            selectQuery += "AND (t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + typeTransaction + " "
                    + "OR t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_OPEN_BALANCE + " " +
                    "OR t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_TRANSFER_IN + " ) ";
        }else
            selectQuery += "AND (t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + typeTransaction + " " +
                    "OR t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_TRANSFER_OUT + ") ";

        if(ids != null) {
            selectQuery += "AND t." + TransactionBean.KEY_ID_ACCOUNT + " IN (" + ids + ") ";
        }else {
            selectQuery += "AND t." + TransactionBean.KEY_ID_ACCOUNT + " = " + idAccountBeanSelected + " ";
        }

        selectQuery += "ORDER BY t." + TransactionBean.KEY_ID + "";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            total = c.getInt(c.getColumnIndex("total"));
        }
        c.close();
        return total;
    }

    /**
     * Getting all Transactions by filter bean
     */
    @SuppressLint("Range")
    public List<TransactionBean> getAllTransactionBeansByFilterBean(FilterTransactionBean filterTransactionBean, long lastId) {
        List<TransactionBean> transactionBeans = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TransactionBean.TABLE + " t WHERE t." + TransactionBean.KEY_DATE_INSERT + " BETWEEN '"
                + filterTransactionBean.getDateFromToQuery() + "' AND '" + filterTransactionBean.getDateAToQuery() + "' ";

        if(filterTransactionBean.getTypeFilter() == TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_INCOME) {
            selectQuery += "AND (t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_INCOME + " "
                    + "OR t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_OPEN_BALANCE
                    + "OR t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_TRANSFER_IN
                    + ") ";
        }else if(filterTransactionBean.getTypeFilter() == TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_EXPENSE) {
            selectQuery += "AND (t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_EXPENSE + " " +
                    "OR t." + TransactionBean.KEY_TYPE_TRANSACTION + " = " + TypeObjectBean.TRANSACTION_TRANSFER_OUT + ") ";
        }

        if(filterTransactionBean.getIdCategories() != null && filterTransactionBean.getIdCategories().size() > 0) {
            selectQuery += "AND t." + TransactionBean.KEY_ID_CATEGORY + " IN (" + filterTransactionBean.getIdCategoriesToQuery() + ") ";
        }

        if(filterTransactionBean.getIdAccounts() != null && filterTransactionBean.getIdAccounts().size() > 0) {
            selectQuery += "AND t." + TransactionBean.KEY_ID_ACCOUNT + " IN (" + filterTransactionBean.getIdAccountsToQuery() + ") ";
        }

        if(lastId > 0)
            selectQuery += " AND t." + TransactionBean.KEY_ID + " < " + lastId;

        selectQuery += " ORDER BY t." + TransactionBean.KEY_ID + " DESC LIMIT 10";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                int amount = c.getInt(c.getColumnIndex(TransactionBean.KEY_AMOUNT));
                if(amount > 0) {
                    TransactionBean transactionBean = new TransactionBean();
                    transactionBean.setId(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID)));
                    transactionBean.setTitle(c.getString(c.getColumnIndex(TransactionBean.KEY_TITLE)));
                    transactionBean.setAmount(amount);
                    transactionBean.setDateInsert(c.getString(c.getColumnIndex(TransactionBean.KEY_DATE_INSERT)));
                    transactionBean.setNote(c.getString(c.getColumnIndex(TransactionBean.KEY_NOTE)));
                    transactionBean.setTypeTransaction(c.getInt(c.getColumnIndex(TransactionBean.KEY_TYPE_TRANSACTION)));
                    transactionBean.setIdAccount(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_ACCOUNT)));
                    transactionBean.setIdTransactionTransfer(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_TRANSACTION_TRANSFER)));
                    transactionBean.setIdCategory(c.getLong(c.getColumnIndex(TransactionBean.KEY_ID_CATEGORY)));
                    transactionBeans.add(transactionBean);
                }
            } while (c.moveToNext());
        }
        c.close();
        return transactionBeans;
    }

}
