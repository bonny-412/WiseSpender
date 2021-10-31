package it.bonny.app.wisespender.bean;

import android.content.res.Resources;

import it.bonny.app.wisespender.R;

public class CategoryType {
    public static final Integer INCOME = 0;
    public static final Integer EXPENSES = 1;

    public CategoryType() {}

    public String getCategoryTypeName(Integer catTypeNum) {
        String categoryTypeName = "";
        if(catTypeNum == 0)
            categoryTypeName = Resources.getSystem().getString(R.string.category_type_income);
        else if(catTypeNum == 1)
            categoryTypeName = Resources.getSystem().getString(R.string.category_type_expenses);

        return categoryTypeName;
    }

}
