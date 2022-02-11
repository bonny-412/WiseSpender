package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.FilterTransactionBean;
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
        accountMaster.setIdIcon(-1);
        accountMaster.setTotMoneyIncome(0);
        accountMaster.setTotMoneyExpense(0);

        AccountBean accountCash = new AccountBean();
        accountCash.setName(activity.getString(R.string.account_bean_main));
        accountCash.setOpeningBalance(0);
        accountCash.setIsMaster(TypeObjectBean.NO_MASTER);
        accountCash.setFlagSelected(TypeObjectBean.NO_SELECTED);
        accountCash.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
        accountCash.setCurrency("EUR");
        accountCash.setIdIcon(7);
        accountCash.setTotMoneyIncome(0);
        accountCash.setTotMoneyExpense(0);

        db.insertAccountBean(accountMaster);
        db.insertAccountBean(accountCash);

    }

    public void insertCategoryDefault(DatabaseHelper db, Activity activity) {
        CategoryBean categoryOpenBalance = new CategoryBean();
        categoryOpenBalance.setName(activity.getString(R.string.category_bean_open_balance));
        categoryOpenBalance.setTypeCategory(TypeObjectBean.CATEGORY_OPEN_BALANCE);
        categoryOpenBalance.setIdIcon(-1);

        CategoryBean categoryHobbyIncome = new CategoryBean();
        categoryHobbyIncome.setName(activity.getString(R.string.category_bean_hobby));
        categoryHobbyIncome.setTypeCategory(TypeObjectBean.CATEGORY_INCOME);
        categoryHobbyIncome.setIdIcon(76);

        CategoryBean categoryHobbyExpense = new CategoryBean();
        categoryHobbyExpense.setName(activity.getString(R.string.category_bean_hobby));
        categoryHobbyExpense.setTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
        categoryHobbyExpense.setIdIcon(76);

        CategoryBean categoryNecessityIncome = new CategoryBean();
        categoryNecessityIncome.setName(activity.getString(R.string.category_bean_necessity));
        categoryNecessityIncome.setTypeCategory(TypeObjectBean.CATEGORY_INCOME);
        categoryNecessityIncome.setIdIcon(50);

        CategoryBean categoryNecessityExpense = new CategoryBean();
        categoryNecessityExpense.setName(activity.getString(R.string.category_bean_necessity));
        categoryNecessityExpense.setTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
        categoryNecessityExpense.setIdIcon(50);

        db.insertCategoryBean(categoryOpenBalance);
        db.insertCategoryBean(categoryHobbyIncome);
        db.insertCategoryBean(categoryHobbyExpense);
        db.insertCategoryBean(categoryNecessityIncome);
        db.insertCategoryBean(categoryNecessityExpense);

    }

    public int getIdIconByAccountBean(AccountBean accountBean) {
        int value;
        if(accountBean.getIsMaster() == TypeObjectBean.IS_MASTER) {
            value = R.drawable.ic_new_account_19;
        }else {
            value = getListIconToAccountBean().get(accountBean.getIdIcon()).getDrawableInfo();
        }
        return value;
    }

    public static List<IconBean> getListIconToAccountBean() {
        List<IconBean> iconBeans = new ArrayList<>();
        iconBeans.add(new IconBean(0, R.drawable.ic_new_account_1, "ic_new_account_1"));
        iconBeans.add(new IconBean(1, R.drawable.ic_new_account_2, "ic_new_account_2"));
        iconBeans.add(new IconBean(2, R.drawable.ic_new_account_3, "ic_new_account_3"));
        iconBeans.add(new IconBean(3, R.drawable.ic_new_account_4, "ic_new_account_4"));
        iconBeans.add(new IconBean(4, R.drawable.ic_new_account_5, "ic_new_account_5"));
        iconBeans.add(new IconBean(5, R.drawable.ic_new_account_6, "ic_new_account_6"));
        iconBeans.add(new IconBean(6, R.drawable.ic_new_account_7, "ic_new_account_7"));
        iconBeans.add(new IconBean(7, R.drawable.ic_new_account_8, "ic_new_account_8"));
        iconBeans.add(new IconBean(8, R.drawable.ic_new_account_9, "ic_new_account_9"));
        iconBeans.add(new IconBean(9, R.drawable.ic_new_account_10, "ic_new_account_10"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_account_11, "ic_new_account_11"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_account_12, "ic_new_account_12"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_account_13, "ic_new_account_13"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_account_14, "ic_new_account_14"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_account_15, "ic_new_account_15"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_account_16, "ic_new_account_16"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_account_17, "ic_new_account_17"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_account_18, "ic_new_account_18"));


        return iconBeans;
    }

    public static List<IconBean> getListIconToCategoryBean() {
        List<IconBean> iconBeans = new ArrayList<>();
        iconBeans.add(new IconBean(0, R.drawable.ic_new_category_84, "ic_new_category_84"));
        iconBeans.add(new IconBean(1, R.drawable.ic_new_category_2, "ic_new_category_2"));
        iconBeans.add(new IconBean(2, R.drawable.ic_new_category_3, "ic_new_category_3"));
        iconBeans.add(new IconBean(3,R.drawable.ic_new_category_4, "ic_new_category_4"));
        iconBeans.add(new IconBean(4, R.drawable.ic_new_category_5, "ic_new_category_5"));
        iconBeans.add(new IconBean(5, R.drawable.ic_new_category_6, "ic_new_category_6"));
        iconBeans.add(new IconBean(6, R.drawable.ic_new_category_7, "ic_new_category_7"));
        iconBeans.add(new IconBean(7, R.drawable.ic_new_category_8, "ic_new_category_8"));
        iconBeans.add(new IconBean(8, R.drawable.ic_new_category_9, "ic_new_category_9"));
        iconBeans.add(new IconBean(9, R.drawable.ic_new_category_10, "ic_new_category_10"));
        iconBeans.add(new IconBean(10, R.drawable.ic_new_category_11, "ic_new_category_11"));
        iconBeans.add(new IconBean(11, R.drawable.ic_new_category_12, "ic_new_category_12"));
        iconBeans.add(new IconBean(12, R.drawable.ic_new_category_13, "ic_new_category_13"));
        iconBeans.add(new IconBean(13, R.drawable.ic_new_category_14, "ic_new_category_14"));
        iconBeans.add(new IconBean(14, R.drawable.ic_new_category_15, "ic_new_category_15"));
        iconBeans.add(new IconBean(15, R.drawable.ic_new_category_16, "ic_new_category_16"));
        iconBeans.add(new IconBean(16, R.drawable.ic_new_category_17, "ic_new_category_17"));
        iconBeans.add(new IconBean(17, R.drawable.ic_new_category_18, "ic_new_category_18"));
        iconBeans.add(new IconBean(18, R.drawable.ic_new_category_19, "ic_new_category_19"));
        iconBeans.add(new IconBean(19, R.drawable.ic_new_category_20, "ic_new_category_20"));
        iconBeans.add(new IconBean(20, R.drawable.ic_new_category_21, "ic_new_category_21"));
        iconBeans.add(new IconBean(21, R.drawable.ic_new_category_22, "ic_new_category_22"));
        iconBeans.add(new IconBean(22, R.drawable.ic_new_category_23, "ic_new_category_23"));
        iconBeans.add(new IconBean(23, R.drawable.ic_new_category_24, "ic_new_category_24"));
        iconBeans.add(new IconBean(24, R.drawable.ic_new_category_25, "ic_new_category_25"));
        iconBeans.add(new IconBean(25, R.drawable.ic_new_category_26, "ic_new_category_26"));
        iconBeans.add(new IconBean(27, R.drawable.ic_new_category_28, "ic_new_category_28"));
        iconBeans.add(new IconBean(28, R.drawable.ic_new_category_29, "ic_new_category_29"));
        iconBeans.add(new IconBean(29, R.drawable.ic_new_category_30, "ic_new_category_30"));
        iconBeans.add(new IconBean(30, R.drawable.ic_new_category_31, "ic_new_category_31"));
        iconBeans.add(new IconBean(31, R.drawable.ic_new_category_32, "ic_new_category_32"));
        iconBeans.add(new IconBean(32, R.drawable.ic_new_category_33, "ic_new_category_33"));
        iconBeans.add(new IconBean(33, R.drawable.ic_new_category_34, "ic_new_category_34"));
        iconBeans.add(new IconBean(34, R.drawable.ic_new_category_35, "ic_new_category_35"));
        iconBeans.add(new IconBean(35, R.drawable.ic_new_category_37, "ic_new_category_37"));
        iconBeans.add(new IconBean(36, R.drawable.ic_new_category_38, "ic_new_category_38"));
        iconBeans.add(new IconBean(37, R.drawable.ic_new_category_39, "ic_new_category_39"));
        iconBeans.add(new IconBean(38, R.drawable.ic_new_category_40, "ic_new_category_40"));
        iconBeans.add(new IconBean(39, R.drawable.ic_new_category_41, "ic_new_category_41"));
        iconBeans.add(new IconBean(40, R.drawable.ic_new_category_42, "ic_new_category_42"));
        iconBeans.add(new IconBean(42, R.drawable.ic_new_category_44, "ic_new_category_44"));
        iconBeans.add(new IconBean(43, R.drawable.ic_new_category_45, "ic_new_category_45"));
        iconBeans.add(new IconBean(44, R.drawable.ic_new_category_46, "ic_new_category_46"));
        iconBeans.add(new IconBean(45, R.drawable.ic_new_category_47, "ic_new_category_47"));
        iconBeans.add(new IconBean(46, R.drawable.ic_new_category_48, "ic_new_category_48"));
        iconBeans.add(new IconBean(47, R.drawable.ic_new_category_49, "ic_new_category_49"));
        iconBeans.add(new IconBean(48, R.drawable.ic_new_category_51, "ic_new_category_51"));
        iconBeans.add(new IconBean(49, R.drawable.ic_new_category_52, "ic_new_category_52"));
        iconBeans.add(new IconBean(50, R.drawable.ic_new_category_54, "ic_new_category_54"));
        iconBeans.add(new IconBean(51, R.drawable.ic_new_category_55, "ic_new_category_55"));
        iconBeans.add(new IconBean(52, R.drawable.ic_new_category_56, "ic_new_category_56"));
        iconBeans.add(new IconBean(53, R.drawable.ic_new_category_57, "ic_new_category_57"));
        iconBeans.add(new IconBean(54, R.drawable.ic_new_category_58, "ic_new_category_58"));
        iconBeans.add(new IconBean(55, R.drawable.ic_new_category_59, "ic_new_category_59"));
        iconBeans.add(new IconBean(56, R.drawable.ic_new_category_61, "ic_new_category_61"));
        iconBeans.add(new IconBean(57, R.drawable.ic_new_category_62, "ic_new_category_62"));
        iconBeans.add(new IconBean(58, R.drawable.ic_new_category_63, "ic_new_category_63"));
        iconBeans.add(new IconBean(59, R.drawable.ic_new_category_65, "ic_new_category_65"));
        iconBeans.add(new IconBean(60, R.drawable.ic_new_category_68, "ic_new_category_68"));
        iconBeans.add(new IconBean(61, R.drawable.ic_new_category_69, "ic_new_category_69"));
        iconBeans.add(new IconBean(62, R.drawable.ic_new_category_70, "ic_new_category_70"));
        iconBeans.add(new IconBean(63, R.drawable.ic_new_category_71, "ic_new_category_71"));
        iconBeans.add(new IconBean(64, R.drawable.ic_new_category_72, "ic_new_category_72"));
        iconBeans.add(new IconBean(65, R.drawable.ic_new_category_73, "ic_new_category_73"));
        iconBeans.add(new IconBean(66, R.drawable.ic_new_category_74, "ic_new_category_74"));
        iconBeans.add(new IconBean(67, R.drawable.ic_new_category_74_1, "ic_new_category_74_1"));
        iconBeans.add(new IconBean(68, R.drawable.ic_new_category_78, "ic_new_category_78"));
        iconBeans.add(new IconBean(69, R.drawable.ic_new_category_79, "ic_new_category_79"));
        iconBeans.add(new IconBean(70, R.drawable.ic_new_category_80, "ic_new_category_80"));
        iconBeans.add(new IconBean(71, R.drawable.ic_new_category_81, "ic_new_category_81"));
        iconBeans.add(new IconBean(72, R.drawable.ic_new_category_82, "ic_new_category_82"));
        iconBeans.add(new IconBean(73, R.drawable.ic_new_category_83, "ic_new_category_83"));
        iconBeans.add(new IconBean(74, R.drawable.ic_new_category_1, "ic_new_category_1"));
        iconBeans.add(new IconBean(75, R.drawable.ic_new_category_85, "ic_new_category_85"));
        iconBeans.add(new IconBean(76, R.drawable.ic_new_category_86, "ic_new_category_86"));




        return iconBeans;
    }

    public int getIdIconByCategoryBean(CategoryBean categoryBean) {
        int value;
        if(categoryBean.getTypeCategory() == TypeObjectBean.CATEGORY_OPEN_BALANCE) {
            value = R.drawable.ic_new_category_84;
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
        int number;
        int totMoneyOpeningBalance = 0;
        int totMoneyIncome = 0;
        if(accountBeanSelected == null) {
            for(AccountBean accountBean: accountBeans) {
                if(accountBean.getFlagViewTotalBalance() == TypeObjectBean.IS_TOTAL_BALANCE) {
                    totMoneyOpeningBalance = totMoneyOpeningBalance + accountBean.getOpeningBalance();
                    totMoneyIncome = totMoneyIncome + accountBean.getTotMoneyIncome();
                }
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
                if(accountBean.getFlagViewTotalBalance() == TypeObjectBean.IS_TOTAL_BALANCE)
                    totMoneyExpense = totMoneyExpense + accountBean.getTotMoneyExpense();
            }
        }else {
            totMoneyExpense = accountBeanSelected.getTotMoneyExpense();
        }
        result += formatNumberCurrency(convertIntInEditTextValue(totMoneyExpense).toString());
        return result;
    }

    public String getDateFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    public Date convertStringInDate(String d) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return format.parse(d);
    }

    public String getDateToShowInPage(String d) {
        try {
            Date date = convertStringInDate(d);
            d = new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(date);
        }catch (Exception e) {
            //TODO: Firebase
        }
        return d;
    }

    public String getTimeToShowInPage(String d) {
        try {
            Date date = convertStringInDate(d);
            d = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
        }catch (Exception e) {
            //TODO: Firebase
        }
        return d;
    }

    public void saveFilterTransactionBean(FilterTransactionBean filterTransactionBean, Activity activity){
        SharedPreferences.Editor editor = activity.getSharedPreferences(PREFS_NAME_FILE, Context.MODE_PRIVATE).edit();
        //editor.putInt("filterDate", filterTransactionBean.getFilterDate());
        //editor.putInt("filterTypeTransaction", filterTransactionBean.getFilterTypeTransaction());
        editor.putString("dateFrom", filterTransactionBean.getDateFrom());
        editor.putString("dateA", filterTransactionBean.getDateA());

        editor.apply();
    }

    public FilterTransactionBean getFilterTransactionBeanSaved(Activity activity){
        FilterTransactionBean bean = new FilterTransactionBean();
        SharedPreferences sharedPreferences = activity.getSharedPreferences(PREFS_NAME_FILE, Context.MODE_PRIVATE);
        //bean.setFilterDate(sharedPreferences.getInt("filterDate", 0));
        //bean.setFilterTypeTransaction(sharedPreferences.getInt("filterTypeTransaction", 3));
        bean.setDateFrom(sharedPreferences.getString("dateFrom", ""));
        bean.setDateA(sharedPreferences.getString("dateA", ""));

        return bean;
    }

    public List<String> getWeekToListTransaction(Calendar c, int weekStartDay, SimpleDateFormat dateFormat) {
        //TODO: Gestire inizio settimana dal bean delle impostazioni
        List<String> strings = new ArrayList<>();
        List<String> output = new ArrayList<>();
        if(weekStartDay == TypeObjectBean.WEEK_START_MONDAY) {
            switch(c.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    c.add(Calendar.DATE, -1);
                    break;
                case Calendar.TUESDAY:
                    c.add(Calendar.DATE, 1);
                    break;
                case Calendar.WEDNESDAY:
                    c.add(Calendar.DATE, -2);
                    break;
                case Calendar.THURSDAY:
                    c.add(Calendar.DATE, -3);
                    break;
                case Calendar.FRIDAY:
                    c.add(Calendar.DATE, -4);
                    break;
                case Calendar.SATURDAY:
                    c.add(Calendar.DATE, -5);
                    break;
                case Calendar.SUNDAY:
                    break;
            }
        }else if(weekStartDay == TypeObjectBean.WEEK_START_SUNDAY) {
            switch(c.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    c.add(Calendar.DATE, -1);
                    break;
                case Calendar.MONDAY:
                    break;
                case Calendar.TUESDAY:
                    c.add(Calendar.DATE,-2);
                    break;
                case Calendar.WEDNESDAY:
                    c.add(Calendar.DATE, -3);
                    break;
                case Calendar.THURSDAY:
                    c.add(Calendar.DATE,-4);
                    break;
                case Calendar.FRIDAY:
                    c.add(Calendar.DATE,-5);
                    break;
                case Calendar.SATURDAY:
                    c.add(Calendar.DATE,-6);
                    break;
            }
        }

        output.add(dateFormat.format(c.getTime()));
        for(int x = 1; x < 7; x++) {
            c.add(Calendar.DATE, 1);
            output.add(dateFormat.format(c.getTime()));
        }

        strings.add(output.get(0) + " 00:00");
        strings.add(output.get(output.size() - 1) + " 23:59");

        return strings;
    }

    public String getNameMonthYearByCalendar(Calendar calendar) {
        String s;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        s = dateFormat.format(calendar.getTime());
        return s;
    }

    public List<String> getShortNameMonth() {
        List<String> months = new ArrayList<>();
        String[] shortMonths = new DateFormatSymbols(Locale.getDefault()).getShortMonths();
        if(shortMonths != null && shortMonths.length > 0) {
            for(String s: shortMonths) {
                // get First letter of the string
                String firstLetStr = s.substring(0, 1);
                // Get remaining letter using substring
                String remLetStr = s.substring(1);
                // convert the first letter of String to uppercase
                firstLetStr = firstLetStr.toUpperCase();
                // concantenate the first letter and remaining string
                String firstLetterCapitalizedName = firstLetStr + remLetStr;

                months.add(firstLetterCapitalizedName);
            }
        }
        return months;
    }

}
