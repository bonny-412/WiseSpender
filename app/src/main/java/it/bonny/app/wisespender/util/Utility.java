package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.content.res.Resources;

import androidx.appcompat.content.res.AppCompatResources;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        accountMaster.setIdIcon(4);
        accountMaster.setTotMoneyIncome(0);
        accountMaster.setTotMoneyExpense(0);

        AccountBean accountCash = new AccountBean();
        accountCash.setName(activity.getString(R.string.account_bean_cash));
        accountCash.setOpeningBalance(0);
        accountCash.setIsMaster(TypeObjectBean.NO_MASTER);
        accountCash.setFlagSelected(TypeObjectBean.NO_SELECTED);
        accountCash.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
        accountCash.setCurrency("EUR");
        accountCash.setIdIcon(6);
        accountCash.setTotMoneyIncome(0);
        accountCash.setTotMoneyExpense(0);

        AccountBean accountCreditCard = new AccountBean();
        accountCreditCard.setName(activity.getString(R.string.account_bean_credit_card));
        accountCreditCard.setOpeningBalance(0);
        accountCash.setIsMaster(TypeObjectBean.NO_MASTER);
        accountCash.setFlagSelected(TypeObjectBean.NO_SELECTED);
        accountCash.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
        accountCreditCard.setCurrency("EUR");
        accountCreditCard.setIdIcon(3);
        accountCreditCard.setTotMoneyIncome(0);
        accountCreditCard.setTotMoneyExpense(0);

        long a = db.insertAccountBean(accountMaster);
        long b = db.insertAccountBean(accountCash);
        long c = db.insertAccountBean(accountCreditCard);

        db.closeDB();

    }

    public void insertCategoryDefault(DatabaseHelper db, Activity activity) {
        CategoryBean categoryOpenBalance = new CategoryBean();
        categoryOpenBalance.setName(activity.getString(R.string.category_bean_open_balance));
        categoryOpenBalance.setTypeCategory(TypeObjectBean.CATEGORY_OPEN_BALANCE);
        categoryOpenBalance.setIdIcon(-1);

        CategoryBean categoryHobbyIncome = new CategoryBean();
        categoryHobbyIncome.setName(activity.getString(R.string.category_bean_hobby));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.CATEGORY_INCOME);
        categoryHobbyIncome.setIdIcon(23);

        CategoryBean categoryHobbyExpense = new CategoryBean();
        categoryHobbyExpense.setName(activity.getString(R.string.category_bean_hobby));
        categoryHobbyExpense.setTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
        categoryHobbyExpense.setIdIcon(23);

        CategoryBean categoryNecessityIncome = new CategoryBean();
        categoryNecessityIncome.setName(activity.getString(R.string.category_bean_necessity));
        categoryNecessityIncome.setTypeCategory(TypeObjectBean.CATEGORY_INCOME);
        categoryNecessityIncome.setIdIcon(0);

        CategoryBean categoryNecessityExpense = new CategoryBean();
        categoryNecessityExpense.setName(activity.getString(R.string.category_bean_necessity));
        categoryNecessityExpense.setTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
        categoryNecessityExpense.setIdIcon(0);

        db.insertCategoryBean(categoryOpenBalance);
        db.insertCategoryBean(categoryHobbyIncome);
        db.insertCategoryBean(categoryHobbyExpense);
        db.insertCategoryBean(categoryNecessityIncome);
        db.insertCategoryBean(categoryNecessityExpense);

        db.closeDB();

    }

    public int getIdIconByAccountBean(AccountBean accountBean) {
        return getListIconToAccountBean().get(accountBean.getIdIcon()).getDrawableInfo();
    }

    public static List<IconBean> getListIconToAccountBean() {
        List<IconBean> iconBeans = new ArrayList<>();
        iconBeans.add(new IconBean(0, R.drawable.icon_bank, "icon_bank"));
        iconBeans.add(new IconBean(1, R.drawable.icon_clutch_bag, "icon_clutch_bag"));
        iconBeans.add(new IconBean(2, R.drawable.icon_coin, "icon_coin"));
        iconBeans.add(new IconBean(3, R.drawable.icon_credit_card, "icon_credit_card"));
        iconBeans.add(new IconBean(4, R.drawable.icon_dollar, "icon_dollar"));
        iconBeans.add(new IconBean(5, R.drawable.icon_dollar_banknote, "icon_dollar_banknote"));
        iconBeans.add(new IconBean(6, R.drawable.icon_euro_banknote, "icon_euro_banknote"));
        iconBeans.add(new IconBean(7, R.drawable.icon_money_bag, "icon_money_bag"));
        iconBeans.add(new IconBean(8, R.drawable.icon_purse, "icon_purse"));
        iconBeans.add(new IconBean(9, R.drawable.icon_safe, "icon_safe"));
        iconBeans.add(new IconBean(10, R.drawable.icon_wallet, "icon_wallet"));

        return iconBeans;
    }

    public static List<IconBean> getListIconToCategoryBean() {
        List<IconBean> iconBeans = new ArrayList<>();
        iconBeans.add(new IconBean(0, R.drawable.icon_animal, "icon_animal"));
        iconBeans.add(new IconBean(1, R.drawable.icon_animal_1, "icon_animal_1"));
        iconBeans.add(new IconBean(2, R.drawable.icon_baby, "icon_baby"));
        iconBeans.add(new IconBean(3,R.drawable.icon_baby_1, "icon_baby_1"));
        iconBeans.add(new IconBean(4, R.drawable.icon_beauty, "icon_beauty"));
        iconBeans.add(new IconBean(5, R.drawable.icon_beauty_1, "icon_beauty_1"));
        iconBeans.add(new IconBean(6, R.drawable.icon_beauty_2, "icon_beauty_2"));
        iconBeans.add(new IconBean(7, R.drawable.icon_beauty_3, "icon_beauty_3"));
        iconBeans.add(new IconBean(8, R.drawable.icon_beauty_4, "icon_beauty_4"));
        iconBeans.add(new IconBean(9, R.drawable.icon_food, "icon_food"));
        iconBeans.add(new IconBean(10, R.drawable.icon_food_1, "icon_food_1"));
        iconBeans.add(new IconBean(11, R.drawable.icon_food_2, "icon_food_2"));
        iconBeans.add(new IconBean(12, R.drawable.icon_food_3, "icon_food_3"));
        iconBeans.add(new IconBean(13, R.drawable.icon_food_4, "icon_food_4"));
        iconBeans.add(new IconBean(14, R.drawable.icon_health, "icon_health"));
        iconBeans.add(new IconBean(15, R.drawable.icon_health_1, "icon_health_1"));
        iconBeans.add(new IconBean(16, R.drawable.icon_house, "icon_house"));
        iconBeans.add(new IconBean(17, R.drawable.icon_love, "icon_love"));
        iconBeans.add(new IconBean(18, R.drawable.icon_motor, "icon_motor"));
        iconBeans.add(new IconBean(19, R.drawable.icon_motor_1, "icon_motor_1"));
        iconBeans.add(new IconBean(20, R.drawable.icon_motor_2, "icon_motor_2"));
        iconBeans.add(new IconBean(21, R.drawable.icon_motor_3, "icon_motor_3"));
        iconBeans.add(new IconBean(22, R.drawable.icon_party, "icon_party"));
        iconBeans.add(new IconBean(23, R.drawable.icon_party_1, "icon_party_1"));
        iconBeans.add(new IconBean(24, R.drawable.icon_shop, "icon_shop"));
        iconBeans.add(new IconBean(25, R.drawable.icon_shop_1, "icon_shop_1"));
        iconBeans.add(new IconBean(26, R.drawable.icon_shop_4, "icon_shop_4"));
        iconBeans.add(new IconBean(27, R.drawable.icon_shop_15, "icon_shop_15"));
        iconBeans.add(new IconBean(28, R.drawable.icon_shop_2, "icon_shop_2"));
        iconBeans.add(new IconBean(29, R.drawable.icon_shop_11, "icon_shop_11"));
        iconBeans.add(new IconBean(30, R.drawable.icon_shop_5, "icon_shop_5"));
        iconBeans.add(new IconBean(31, R.drawable.icon_shop_6, "icon_shop_6"));
        iconBeans.add(new IconBean(32, R.drawable.icon_shop_7, "icon_shop_7"));
        iconBeans.add(new IconBean(33, R.drawable.icon_shop_9, "icon_shop_9"));
        iconBeans.add(new IconBean(34, R.drawable.icon_shop_10, "icon_shop_10"));
        iconBeans.add(new IconBean(35, R.drawable.icon_shop_12, "icon_shop_12"));
        iconBeans.add(new IconBean(36, R.drawable.icon_shop_17, "icon_shop_17"));
        iconBeans.add(new IconBean(37, R.drawable.icon_shop_18, "icon_shop_18"));
        iconBeans.add(new IconBean(38, R.drawable.icon_sport, "icon_sport"));
        iconBeans.add(new IconBean(39, R.drawable.icon_sport_5, "icon_sport_5"));
        iconBeans.add(new IconBean(40, R.drawable.icon_sport_1, "icon_sport_1"));
        iconBeans.add(new IconBean(41, R.drawable.icon_sport_2, "icon_sport_2"));
        iconBeans.add(new IconBean(42, R.drawable.icon_sport_3, "icon_sport_3"));
        iconBeans.add(new IconBean(43, R.drawable.icon_sport_4, "icon_sport_4"));
        iconBeans.add(new IconBean(44, R.drawable.icon_trips, "icon_trips"));
        iconBeans.add(new IconBean(45, R.drawable.icon_trips_1, "icon_trips_1"));
        iconBeans.add(new IconBean(46, R.drawable.icon_trips_7, "icon_trips_7"));
        iconBeans.add(new IconBean(47, R.drawable.icon_trips_3, "icon_trips_3"));
        iconBeans.add(new IconBean(48, R.drawable.icon_trips_4, "icon_trips_4"));
        iconBeans.add(new IconBean(49, R.drawable.icon_atm, "icon_atm"));

        return iconBeans;
    }

    public int getIdIconByCategoryBean(CategoryBean categoryBean) {
        int value;
        if(categoryBean.getTypeCategory() == TypeObjectBean.CATEGORY_OPEN_BALANCE) {
            value = R.drawable.icon_open_balance;
        }else {
            value = getListIconToCategoryBean().get(categoryBean.getIdIcon()).getDrawableInfo();
        }
        return value;
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

    public String formatNumberCurrency(String number) {
        DecimalFormat format = new DecimalFormat("###,###,##0.00");
        return format.format(Double.parseDouble(number));
    }

    public String getTotMoneyAccountByAllAccounts(List<AccountBean> accountBeans, AccountBean accountBeanSelected) {
        String result = "";
        int number;
        int totMoneyOpeningBalance = 0;
        int totMoneyIncome = 0;
        int totMoneyExpense = 0;
        if(accountBeanSelected == null) {
            for(AccountBean accountBean: accountBeans) {
                if(accountBean.getFlagViewTotalBalance() == TypeObjectBean.IS_TOTAL_BALANCE) {
                    totMoneyOpeningBalance = totMoneyOpeningBalance + accountBean.getOpeningBalance();
                    totMoneyIncome = totMoneyIncome + accountBean.getTotMoneyIncome();
                    totMoneyExpense = totMoneyExpense + accountBean.getTotMoneyExpense();
                }
            }
        }else {
            totMoneyOpeningBalance = accountBeanSelected.getOpeningBalance();
            totMoneyIncome = accountBeanSelected.getTotMoneyIncome();
            totMoneyExpense = accountBeanSelected.getTotMoneyExpense();
        }
        int totSum = totMoneyOpeningBalance + totMoneyIncome;
        if(totMoneyExpense > totSum) {
            number = totMoneyExpense - totSum;
            result = "- ";
        }else {
            number = totSum - totMoneyExpense;
        }
        result += formatNumberCurrency(convertIntInEditTextValue(number).toString());
        return result;
    }

    public String getTotMoneyIncomeAccountByAllAccounts(List<AccountBean> accountBeans, AccountBean accountBeanSelected) {
        String result = "";
        int number = 0;
        int totMoneyOpeningBalance = 0;
        int totMoneyIncome = 0;
        if(accountBeanSelected == null) {
            for(AccountBean accountBean: accountBeans) {
                totMoneyOpeningBalance = totMoneyOpeningBalance + accountBean.getOpeningBalance();
                totMoneyIncome = totMoneyIncome + accountBean.getTotMoneyIncome();
            }
        }else {
            totMoneyOpeningBalance = accountBeanSelected.getOpeningBalance();
            totMoneyIncome = accountBeanSelected.getTotMoneyIncome();
        }
        number = totMoneyOpeningBalance + totMoneyIncome;
        result += formatNumberCurrency(convertIntInEditTextValue(number).toString());
        return result;
    }

    public String getTotMoneyExpenseAccountByAllAccounts(List<AccountBean> accountBeans, AccountBean accountBeanSelected) {
        String result = "";
        int totMoneyExpense = 0;
        if(accountBeanSelected == null) {
            for(AccountBean accountBean: accountBeans) {
                totMoneyExpense = totMoneyExpense + accountBean.getTotMoneyExpense();
            }
        }else {
            totMoneyExpense = accountBeanSelected.getTotMoneyExpense();
        }
        result += formatNumberCurrency(convertIntInEditTextValue(totMoneyExpense).toString());
        return result;
    }

    public String getDateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    public String getDateToShowInPage(String date, Activity activity) {
        String result = "";
        String[] parts = date.split("-");
        String month = parts[1];
        String day = parts[2];
        int monthInt = Integer.parseInt(month);
        if(monthInt == 1)
            result = activity.getString(R.string.month_jan);
        else if(monthInt == 2)
            result = activity.getString(R.string.month_feb);
        else if(monthInt == 3)
            result = activity.getString(R.string.month_mar);
        else if(monthInt == 4)
            result = activity.getString(R.string.month_apr);
        else if(monthInt == 5)
            result = activity.getString(R.string.month_may);
        else if(monthInt == 6)
            result = activity.getString(R.string.month_jun);
        else if(monthInt == 7)
            result = activity.getString(R.string.month_july);
        else if(monthInt == 8)
            result = activity.getString(R.string.month_aug);
        else if(monthInt == 9)
            result = activity.getString(R.string.month_sep);
        else if(monthInt == 10)
            result = activity.getString(R.string.month_oct);
        else if(monthInt == 11)
            result = activity.getString(R.string.month_nov);
        else if(monthInt == 12)
            result = activity.getString(R.string.month_dec);

        result = day + " " + result;
        return result;
    }

}
