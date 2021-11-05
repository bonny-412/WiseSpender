package it.bonny.app.wisespender.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wiseSpender";
    
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + AccountBean.TABLE
            + "("
                + AccountBean.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + AccountBean.KEY_NAME + " TEXT,"
                + AccountBean.KEY_AMOUNT + " INTEGER,"
                + AccountBean.KEY_FLAG_VIEW_TOTAL_BALANCE + " INTEGER DEFAULT 0,"
                + AccountBean.KEY_FLAG_SELECTED + " INTEGER DEFAULT 0"
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

    /*******************************
     *********** ACCOUNT ***********
     *****************************/

    public long insertAccount(AccountBean accountBean) {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.insertAccount(accountBean, this.getWritableDatabase());
    }

    public AccountBean getAccountById(long id) {
        AccountBean accountBean = null;
        try {
            AccountDAO accountDAO = new AccountDAO();
            accountBean = accountDAO.getAccountById(id, this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return accountBean;
    }

    public List<AccountBean> getAllAccounts() {
        List<AccountBean> accountBeans = new ArrayList<>();
        try {
            AccountDAO accountDAO = new AccountDAO();
            accountBeans = accountDAO.getAllAccounts(this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return accountBeans;
    }

    public long updateAccount(AccountBean accountBean) {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.updateAccount(accountBean, this.getWritableDatabase());
    }

    public boolean deleteAccount(long id) {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.deleteAccount(id, this.getWritableDatabase());
    }

    /*******************************
     *********** CATEGORY **********
     *****************************/
    public long insertCategory(CategoryBean categoryBean) {
        CategoryDAO categoryDAO = new CategoryDAO();
        return categoryDAO.insertCategory(categoryBean, this.getWritableDatabase());
    }

    public long updateCategory(CategoryBean account) {
        CategoryDAO categoryDAO = new CategoryDAO();
        return categoryDAO.updateCategory(account, this.getWritableDatabase());
    }

    public boolean deleteCategory(long id) {
        CategoryDAO categoryDAO = new CategoryDAO();
        return categoryDAO.deleteCategory(id, this.getWritableDatabase());
    }

    public CategoryBean getCategoryById(long id) {
        CategoryBean categoryBean = null;
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            categoryBean = categoryDAO.getCategoryById(id, this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return categoryBean;
    }

    public List<CategoryBean> getAllCategoryIncome() {
        List<CategoryBean> categories = new ArrayList<>();
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            categories = categoryDAO.getAllCategoryIncome(this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return categories;
    }

    public List<CategoryBean> getAllCategoryExpense() {
        List<CategoryBean> categories = new ArrayList<>();
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            categories = categoryDAO.getAllCategoryExpense(this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return categories;
    }

    /*******************************
     *********** TRANSACTION *******
     *****************************/
    public long insertTransaction(TransactionBean transactionBean) {
        TransactionDAO transactionDAO = new TransactionDAO();
        return transactionDAO.insertTransaction(transactionBean, this.getWritableDatabase());
    }

    public long updateTransaction(TransactionBean transactionBean) {
        TransactionDAO transactionDAO = new TransactionDAO();
        return transactionDAO.updateTransaction(transactionBean, this.getWritableDatabase());
    }

    public boolean deleteTransaction(long id) {
        TransactionDAO transactionDAO = new TransactionDAO();
        return transactionDAO.deleteTransaction(id, this.getWritableDatabase());
    }

    public TransactionBean getTransactionById(long id) {
        TransactionBean transactionBean = null;
        try {
            TransactionDAO transactionDAO = new TransactionDAO();
            transactionBean = transactionDAO.getTransactionById(id, this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return transactionBean;
    }

    public List<TransactionBean> getAllTransactionIncome() {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        try {
            TransactionDAO transactionDAO = new TransactionDAO();
            transactionBeans = transactionDAO.getAllTransactionIncome(this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return transactionBeans;
    }

    public List<TransactionBean> getAllTransactionExpense() {
        List<TransactionBean> transactionBeans = new ArrayList<>();
        try {
            TransactionDAO transactionDAO = new TransactionDAO();
            transactionBeans = transactionDAO.getAllTransactionExpense(this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return transactionBeans;
    }



    ////////////////////////////////////////////////////
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //On upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + AccountBean.TABLE);

        //Create new tables
        onCreate(db);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase dbRead = this.getReadableDatabase();
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        if (dbRead != null && dbRead.isOpen())
            dbRead.close();
        if(dbWrite != null && dbWrite.isOpen())
            dbWrite.close();
    }

}
