package it.bonny.app.wisespender.bean;

import android.content.res.Resources;

import it.bonny.app.wisespender.R;

public class TypeObjectBean {
    public static final int CATEGORY_INCOME = 0;
    public static final int CATEGORY_EXPENSE = 1;
    public static final int CATEGORY_OPEN_BALANCE = 2;
    public static final int CATEGORY_TRANSFER = 3;

    public static final int SELECTED = 1;
    public static final int NO_SELECTED = 0;

    public static final int IS_MASTER = 1;
    public static final int NO_MASTER = 0;

    public static final int IS_INCLUDED_BALANCE = 1;
    public static final int NO_INCLUDED_BALANCE = 0;

    public static final int TRANSACTION_INCOME = 0;
    public static final int TRANSACTION_EXPENSE = 1;
    public static final int TRANSACTION_OPEN_BALANCE = 2;
    public static final int TRANSACTION_TRANSFER_IN = 3;
    public static final int TRANSACTION_TRANSFER_OUT = 4;

    public static final int FILTER_DATE_DAY = 0;
    public static final int FILTER_DATE_RANGE = 1;

    public static final int WEEK_START_MONDAY = 0;
    public static final int WEEK_START_SUNDAY = 1;

    public static final int RETURN_NORMAL = 0;
    public static final int RETURN_NEW = 1;
    public static final int RETURN_EDIT = 2;
    public static final int RETURN_DELETE = 3;

    public static final int SEARCH_TRANSACTION_TYPE = 0;
    public static final int SEARCH_TRANSACTION_DATE = 1;
    public static final int SEARCH_TRANSACTION_ACCOUNT = 2;
    public static final int SEARCH_TRANSACTION_CATEGORY = 3;

    public static final int FILTER_SEARCH_TRANSACTION_TYPE_ALL = 0;
    public static final int FILTER_SEARCH_TRANSACTION_TYPE_INCOME = 1;
    public static final int FILTER_SEARCH_TRANSACTION_TYPE_EXPENSE = 2;
    public static final int FILTER_SEARCH_TRANSACTION_TYPE_ALL_NO_CHECKED = 3;

    public TypeObjectBean() {}

}
