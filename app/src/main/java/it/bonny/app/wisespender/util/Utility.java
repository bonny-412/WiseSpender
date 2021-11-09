package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.IconBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;

public class Utility {
    public static  final String PREFS_NAME_FILE = "WiseSpenderFileConf";
    public Utility() {
    }

    public void insertAccountDefault(DatabaseHelper db, Activity activity) {
        AccountBean accountMaster = new AccountBean();
        accountMaster.setName(activity.getString(R.string.account_bean_master));
        accountMaster.setOpeningBalance(0);
        accountMaster.setIsMaster(TypeObjectBean.IS_MASTER);
        accountMaster.setFlagSelected(TypeObjectBean.SELECTED);
        accountMaster.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
        accountMaster.setCurrency("EUR");
        accountMaster.setIdIcon("ic_bank");
        accountMaster.setTotMoneyIncome(0);
        accountMaster.setTotMoneyExpense(0);

        AccountBean accountCash = new AccountBean();
        accountCash.setName(activity.getString(R.string.account_bean_cash));
        accountCash.setOpeningBalance(0);
        accountCash.setIsMaster(TypeObjectBean.NO_MASTER);
        accountCash.setFlagSelected(TypeObjectBean.NO_SELECTED);
        accountCash.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
        accountCash.setCurrency("EUR");
        accountCash.setIdIcon("ic_money");
        accountCash.setTotMoneyIncome(0);
        accountCash.setTotMoneyExpense(0);

        AccountBean accountCreditCard = new AccountBean();
        accountCreditCard.setName(activity.getString(R.string.account_bean_credit_card));
        accountCreditCard.setOpeningBalance(0);
        accountCash.setIsMaster(TypeObjectBean.NO_MASTER);
        accountCash.setFlagSelected(TypeObjectBean.NO_SELECTED);
        accountCash.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
        accountCreditCard.setCurrency("EUR");
        accountCreditCard.setIdIcon("ic_credit_card");
        accountCreditCard.setTotMoneyIncome(0);
        accountCreditCard.setTotMoneyExpense(0);

        long a = db.insertAccountBean(accountMaster);
        long b = db.insertAccountBean(accountCash);
        long c = db.insertAccountBean(accountCreditCard);

        db.closeDB();

    }

    public void insertCategoryDefault(DatabaseHelper db, Activity activity) {
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

    public int getIdIconByAccountBean(AccountBean accountBean) {
        int idIcon = 0;
        if("ic_bank".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.ic_bank;
        }else if("ic_credit_card".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.ic_credit_card;
        }else if("ic_money".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.ic_money;
        }else if("ic_cash_register".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.ic_cash_register;
        }
        return idIcon;
    }

    public static List<IconBean> getListIconToNewAccount() {
        List<IconBean> iconBeans = new ArrayList<>();
        IconBean icBank = new IconBean(R.drawable.ic_bank, "ic_bank");
        IconBean icCashRegister = new IconBean(R.drawable.ic_cash_register, "ic_cash_register");
        IconBean icCreditCard = new IconBean(R.drawable.ic_credit_card, "ic_credit_card");
        IconBean icMoney = new IconBean(R.drawable.ic_money, "ic_money");
        iconBeans.add(icBank);
        iconBeans.add(icCashRegister);
        iconBeans.add(icCreditCard);
        iconBeans.add(icMoney);
        return iconBeans;
    }

    public static int getPositionIconToNewAccount(String nameIcon) {
        int pos = -1;
        if("ic_bank".equals(nameIcon)) {
            pos = 0;
        }else if("ic_cash_register".equals(nameIcon)) {
            pos = 1;
        }else if("ic_credit_card".equals(nameIcon)) {
            pos = 2;
        }else if("ic_money".equals(nameIcon)) {
            pos = 3;
        }
        return pos;
    }

}
