package it.bonny.app.wisespender.util;

import android.accounts.Account;
import android.content.res.Resources;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;

public class Utility {
    public static  final String PREFS_NAME_FILE = "WiseSpenderFileConf";

    public Utility() {}

    public void insertAccountDefault(DatabaseHelper db) {
        AccountBean accountMaster = new AccountBean();
        accountMaster.setName(Resources.getSystem().getString(R.string.account_bean_master));
        accountMaster.setAmount(0);
        accountMaster.setIsMaster(1);
        accountMaster.setFlagSelected(1);
        accountMaster.setFlagViewTotalBalance(1);
        accountMaster.setCurrency("EUR");

        AccountBean accountCash = new AccountBean();
        accountMaster.setName(Resources.getSystem().getString(R.string.account_bean_cash));
        accountMaster.setAmount(0);
        accountMaster.setIsMaster(0);
        accountMaster.setFlagSelected(0);
        accountMaster.setFlagViewTotalBalance(1);
        accountMaster.setCurrency("EUR");

        AccountBean accountCreditCard = new AccountBean();
        accountMaster.setName(Resources.getSystem().getString(R.string.account_bean_credit_card));
        accountMaster.setAmount(0);
        accountMaster.setIsMaster(0);
        accountMaster.setFlagSelected(0);
        accountMaster.setFlagViewTotalBalance(1);
        accountMaster.setCurrency("EUR");

        db.insertAccount(accountMaster);
        db.insertAccount(accountCash);
        db.insertAccount(accountCreditCard);

        db.closeDB();

    }

    public void insertCategoryDefault(DatabaseHelper db) {
        CategoryBean categoryHobbyIncome = new CategoryBean();
        categoryHobbyIncome.setName(Resources.getSystem().getString(R.string.category_bean_hobby));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.INCOME);
        categoryHobbyIncome.setIdIcon(0);

        CategoryBean categoryHobbyExpense = new CategoryBean();
        categoryHobbyIncome.setName(Resources.getSystem().getString(R.string.category_bean_hobby));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.EXPENSE);
        categoryHobbyIncome.setIdIcon(0);

        CategoryBean categoryNecessityIncome = new CategoryBean();
        categoryHobbyIncome.setName(Resources.getSystem().getString(R.string.category_bean_necessity));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.INCOME);
        categoryHobbyIncome.setIdIcon(0);

        CategoryBean categoryNecessityExpense = new CategoryBean();
        categoryHobbyIncome.setName(Resources.getSystem().getString(R.string.category_bean_necessity));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.EXPENSE);
        categoryHobbyIncome.setIdIcon(0);

        db.insertCategory(categoryHobbyIncome);
        db.insertCategory(categoryHobbyExpense);
        db.insertCategory(categoryNecessityIncome);
        db.insertCategory(categoryNecessityExpense);

        db.closeDB();

    }

}
