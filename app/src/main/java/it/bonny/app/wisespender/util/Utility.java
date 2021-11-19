package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.content.res.Resources;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final BigDecimal cent = new BigDecimal(100);

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
        accountMaster.setIdIcon("icon_safe");
        accountMaster.setTotMoneyIncome(0);
        accountMaster.setTotMoneyExpense(0);

        AccountBean accountCash = new AccountBean();
        accountCash.setName(activity.getString(R.string.account_bean_cash));
        accountCash.setOpeningBalance(0);
        accountCash.setIsMaster(TypeObjectBean.NO_MASTER);
        accountCash.setFlagSelected(TypeObjectBean.NO_SELECTED);
        accountCash.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
        accountCash.setCurrency("EUR");
        accountCash.setIdIcon("icon_cash");
        accountCash.setTotMoneyIncome(0);
        accountCash.setTotMoneyExpense(0);

        AccountBean accountCreditCard = new AccountBean();
        accountCreditCard.setName(activity.getString(R.string.account_bean_credit_card));
        accountCreditCard.setOpeningBalance(0);
        accountCash.setIsMaster(TypeObjectBean.NO_MASTER);
        accountCash.setFlagSelected(TypeObjectBean.NO_SELECTED);
        accountCash.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
        accountCreditCard.setCurrency("EUR");
        accountCreditCard.setIdIcon("icon_credit_card");
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
        if("icon_bank".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.icon_bank;
        }else if("icon_cash".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.icon_cash;
        }else if("icon_credit_card".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.icon_credit_card;
        }else if("icon_money_box".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.icon_money_box;
        }else if("icon_safe".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.icon_safe;
        }else if("icon_wallet".equals(accountBean.getIdIcon())) {
            idIcon = R.drawable.icon_wallet;
        }
        return idIcon;
    }

    public static List<IconBean> getListIconToAccountBean() {
        List<IconBean> iconBeans = new ArrayList<>();
        IconBean icon_bank = new IconBean(R.drawable.icon_bank, "icon_bank");
        IconBean icon_cash = new IconBean(R.drawable.icon_cash, "icon_cash");
        IconBean icon_credit_card = new IconBean(R.drawable.icon_credit_card, "icon_credit_card");
        IconBean icon_money_box = new IconBean(R.drawable.icon_money_box, "icon_money_box");
        IconBean icon_safe = new IconBean(R.drawable.icon_safe, "icon_safe");
        IconBean icon_wallet = new IconBean(R.drawable.icon_wallet, "icon_wallet");
        iconBeans.add(icon_bank);
        iconBeans.add(icon_cash);
        iconBeans.add(icon_credit_card);
        iconBeans.add(icon_money_box);
        iconBeans.add(icon_safe);
        iconBeans.add(icon_wallet);
        return iconBeans;
    }

    public static int getPositionIconToAccountBean(String nameIcon) {
        int pos = -1;
        if("icon_bank".equals(nameIcon)) {
            pos = 0;
        }else if("icon_cash".equals(nameIcon)) {
            pos = 1;
        }else if("icon_credit_card".equals(nameIcon)) {
            pos = 2;
        }else if("icon_money_box".equals(nameIcon)) {
            pos = 3;
        }else if("icon_safe".equals(nameIcon)) {
            pos = 3;
        }else if("icon_wallet".equals(nameIcon)) {
            pos = 3;
        }
        return pos;
    }


    public int convertEditTextValueInInt(String valueString) {
        int val;
        if(valueString.contains(","))
            valueString = valueString.replace(",", "");
        BigDecimal bigDecimal = new BigDecimal(valueString);
        val = bigDecimal.multiply(cent).intValue();
        return val;
    }

    public BigDecimal convertIntInEditTextValue(int value) {
        BigDecimal bigDecimal = new BigDecimal(value);
        return bigDecimal.divide(cent, 2, RoundingMode.CEILING);
    }

}
