package it.bonny.app.wisespender.bean;

import android.content.res.Resources;

import it.bonny.app.wisespender.R;

public class TypeObjectBean {
    public static final Integer INCOME = 0;
    public static final Integer EXPENSE = 1;
    public static final Integer SELECTED = 1;
    public static final Integer NO_SELECTED = 0;

    public TypeObjectBean() {}

    public String getTypeName(Integer catTypeNum) {
        String typeName = "";
        if(catTypeNum == 0)
            typeName = Resources.getSystem().getString(R.string.type_income);
        else if(catTypeNum == 1)
            typeName = Resources.getSystem().getString(R.string.type_expense);

        return typeName;
    }

}
