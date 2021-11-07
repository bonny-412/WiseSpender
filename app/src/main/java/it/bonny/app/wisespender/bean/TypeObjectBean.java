package it.bonny.app.wisespender.bean;

import android.content.res.Resources;

import it.bonny.app.wisespender.R;

public class TypeObjectBean {
    public static final int INCOME = 0;
    public static final int EXPENSE = 1;
    public static final int SELECTED = 1;
    public static final int NO_SELECTED = 0;
    public static final int IS_MASTER = 1;
    public static final int NO_MASTER = 0;
    public static final int IS_TOTAL_BALANCE = 1;
    public static final int NO_TOTAL_BALANCE = 0;

    public TypeObjectBean() {}

    public String getTypeName(int catTypeNum) {
        String typeName = "";
        if(catTypeNum == 0)
            typeName = Resources.getSystem().getString(R.string.type_income);
        else if(catTypeNum == 1)
            typeName = Resources.getSystem().getString(R.string.type_expense);

        return typeName;
    }

}
