package it.bonny.app.wisespender.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.Account;
import it.bonny.app.wisespender.bean.Category;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wiseSpender";
    
    private static final String CREATE_TABLE_ACCOUNT = "CREATE TABLE " + Account.TABLE
            + "("
                + Account.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + Account.KEY_NAME + " TEXT,"
                + Account.KEY_AMOUNT + " INTEGER,"
                + Account.KEY_FLAG_VIEW_TOTAL_BALANCE + " INTEGER DEFAULT 0,"
                + Account.KEY_FLAG_SELECTED + " INTEGER DEFAULT 0"
            + ")";
    private static final String CREATE_TABLE_CATEGORY = "CREATE TABLE " + Category.TABLE
            + "("
            + Category.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + Category.KEY_NAME + " TEXT,"
            + Category.KEY_LIMIT_CASH + " INTEGER,"
            + Category.KEY_TYPE_CATEGORY + " INTEGER,"
            + Category.KEY_ID_CATEGORY_ASSOCIATED + " INTEGER,"
            + Category.KEY_ID_ICON + " INTEGER"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*******************************
     *********** ACCOUNT ***********
     *****************************/

    public long insertAccount(Account account) {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.insertAccount(account, this.getWritableDatabase());
    }

    public Account getAccountById(long id) {
        Account account = null;
        try {
            AccountDAO accountDAO = new AccountDAO();
            account = accountDAO.getAccountById(id, this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return account;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        try {
            AccountDAO accountDAO = new AccountDAO();
            accounts = accountDAO.getAllAccounts(this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return accounts;
    }

    public int updateAccount(Account account) {
        AccountDAO accountDAO = new AccountDAO();
        return accountDAO.updateAccount(account, this.getWritableDatabase());
    }

    public void deleteAccount(long id) {
        AccountDAO accountDAO = new AccountDAO();
        accountDAO.deleteAccount(id, this.getWritableDatabase());
    }

    /*******************************
     *********** CATEGORY **********
     *****************************/
    public long insertCategory(Category category) {
        CategoryDAO categoryDAO = new CategoryDAO();
        return categoryDAO.insertCategory(category, this.getWritableDatabase());
    }

    public int updateCategory(Category account) {
        CategoryDAO categoryDAO = new CategoryDAO();
        return categoryDAO.updateCategory(account, this.getWritableDatabase());
    }

    public void deleteCategory(long id) {
        CategoryDAO categoryDAO = new CategoryDAO();
        categoryDAO.deleteCategory(id, this.getWritableDatabase());
    }

    public Category getCategoryById(long id) {
        Category category = null;
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            category = categoryDAO.getCategoryById(id, this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return category;
    }

    public List<Category> getAllCategoryIncome() {
        List<Category> categories = new ArrayList<>();
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            categories = categoryDAO.getAllCategoryIncome(this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return categories;
    }

    public List<Category> getAllCategoryExpense() {
        List<Category> categories = new ArrayList<>();
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            categories = categoryDAO.getAllCategoryExpense(this.getReadableDatabase());
        }catch (Exception e) {
            //TODO: Firebase
        }
        return categories;
    }



   ////////////////////////////////////////////////////
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //On upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + Account.TABLE);

        //Create new tables
        onCreate(db);
    }
}
