package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.content.res.Resources;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;

public class Utility {
    public static  final String PREFS_NAME_FILE = "WiseSpenderFileConf";
    private final Activity activity;
    public Utility(Activity activity) {
        this.activity = activity;
    }

    public void insertAccountDefault(DatabaseHelper db) {
        AccountBean accountMaster = new AccountBean();
        accountMaster.setName(activity.getString(R.string.account_bean_master));
        accountMaster.setAmount(0);
        accountMaster.setIsMaster(1);
        accountMaster.setFlagSelected(1);
        accountMaster.setFlagViewTotalBalance(1);
        accountMaster.setCurrency("EUR");
        accountMaster.setIdIcon("2131165369");

        AccountBean accountCash = new AccountBean();
        accountCash.setName(activity.getString(R.string.account_bean_cash));
        accountCash.setAmount(0);
        accountCash.setIsMaster(0);
        accountCash.setFlagSelected(0);
        accountCash.setFlagViewTotalBalance(1);
        accountCash.setCurrency("EUR");
        accountCash.setIdIcon("2131165367");

        AccountBean accountCreditCard = new AccountBean();
        accountCreditCard.setName(activity.getString(R.string.account_bean_credit_card));
        accountCreditCard.setAmount(0);
        accountCreditCard.setIsMaster(0);
        accountCreditCard.setFlagSelected(0);
        accountCreditCard.setFlagViewTotalBalance(1);
        accountCreditCard.setCurrency("EUR");
        accountCreditCard.setIdIcon("2131165356");

        long a = db.insertAccountBean(accountMaster);
        long b = db.insertAccountBean(accountCash);
        long c = db.insertAccountBean(accountCreditCard);

        db.closeDB();

    }

    public void insertCategoryDefault(DatabaseHelper db) {
        CategoryBean categoryHobbyIncome = new CategoryBean();
        categoryHobbyIncome.setName(activity.getString(R.string.category_bean_hobby));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.INCOME);
        categoryHobbyIncome.setIdIcon(0);

        CategoryBean categoryHobbyExpense = new CategoryBean();
        categoryHobbyIncome.setName(activity.getString(R.string.category_bean_hobby));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.EXPENSE);
        categoryHobbyIncome.setIdIcon(0);

        CategoryBean categoryNecessityIncome = new CategoryBean();
        categoryHobbyIncome.setName(activity.getString(R.string.category_bean_necessity));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.INCOME);
        categoryHobbyIncome.setIdIcon(0);

        CategoryBean categoryNecessityExpense = new CategoryBean();
        categoryHobbyIncome.setName(Resources.getSystem().getString(R.string.category_bean_necessity));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.EXPENSE);
        categoryHobbyIncome.setIdIcon(0);

        /*db.insertCategory(categoryHobbyIncome);
        db.insertCategory(categoryHobbyExpense);
        db.insertCategory(categoryNecessityIncome);
        db.insertCategory(categoryNecessityExpense);

        db.closeDB();*/

    }

}
