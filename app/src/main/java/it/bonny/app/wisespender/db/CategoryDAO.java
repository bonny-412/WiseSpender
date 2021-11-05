package it.bonny.app.wisespender.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;

public class CategoryDAO {

    public long insertCategory(CategoryBean categoryBean, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CategoryBean.KEY_NAME, categoryBean.getName());
        values.put(CategoryBean.KEY_ID_ICON, categoryBean.getIdIcon());
        values.put(CategoryBean.KEY_TYPE_CATEGORY, categoryBean.getTypeCategory());
        //values.put(CategoryBean.KEY_LIMIT_CASH, categoryBean.getLimitCash());
        return db.insert(CategoryBean.TABLE, null, values);
    }

    public long updateCategory(CategoryBean categoryBean, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(CategoryBean.KEY_NAME, categoryBean.getName());
        values.put(CategoryBean.KEY_ID_ICON, categoryBean.getIdIcon());
        values.put(CategoryBean.KEY_TYPE_CATEGORY, categoryBean.getTypeCategory());
        //values.put(CategoryBean.KEY_LIMIT_CASH, categoryBean.getLimitCash());
        return db.update(CategoryBean.TABLE, values, CategoryBean.KEY_ID + " = ?",
                new String[] {String.valueOf(categoryBean.getId())});
    }

    public boolean deleteCategory(long id, SQLiteDatabase db) {
        boolean result = false;
        try {
            result = db.delete(CategoryBean.TABLE, CategoryBean.KEY_ID + " = ?",
                    new String[] {String.valueOf(id)}) > 0;
        }catch (Exception e) {
            //TODO: Firebase
        }
        return result;
    }

    public CategoryBean getCategoryById(long id, SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM " + CategoryBean.TABLE + " WHERE " + CategoryBean.KEY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        CategoryBean categoryBean = new CategoryBean();
        if(c != null) {
            c.moveToFirst();

            categoryBean.setId(Long.parseLong(c.getString(c.getColumnIndex(CategoryBean.KEY_ID))));
            categoryBean.setName(c.getString(c.getColumnIndex(CategoryBean.KEY_NAME)));
            categoryBean.setIdIcon(c.getInt(c.getColumnIndex(CategoryBean.KEY_ID_ICON)));
            categoryBean.setTypeCategory(c.getInt(c.getColumnIndex(CategoryBean.KEY_TYPE_CATEGORY)));
            //categoryBean.setLimitCash(c.getInt(c.getColumnIndex(CategoryBean.KEY_LIMIT_CASH)));

        }
        if(c != null)
            c.close();
        return categoryBean;
    }

    public List<CategoryBean> getAllCategoryIncome(SQLiteDatabase db) {
        List<CategoryBean> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CategoryBean.TABLE + " c WHERE c." + CategoryBean.KEY_TYPE_CATEGORY + " = " + TypeObjectBean.INCOME;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setId(Long.parseLong(c.getString(c.getColumnIndex(CategoryBean.KEY_ID))));
                categoryBean.setName(c.getString(c.getColumnIndex(CategoryBean.KEY_NAME)));
                categoryBean.setIdIcon(c.getInt(c.getColumnIndex(CategoryBean.KEY_ID_ICON)));
                categoryBean.setTypeCategory(c.getInt(c.getColumnIndex(CategoryBean.KEY_TYPE_CATEGORY)));
                //categoryBean.setLimitCash(c.getInt(c.getColumnIndex(CategoryBean.KEY_LIMIT_CASH)));
                categories.add(categoryBean);
            }while (c.moveToNext());
        }
        c.close();
        return categories;
    }

    public List<CategoryBean> getAllCategoryExpense(SQLiteDatabase db) {
        List<CategoryBean> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CategoryBean.TABLE + " c WHERE c." + CategoryBean.KEY_TYPE_CATEGORY + " = " + TypeObjectBean.EXPENSE;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                CategoryBean categoryBean = new CategoryBean();
                categoryBean.setId(Long.parseLong(c.getString(c.getColumnIndex(CategoryBean.KEY_ID))));
                categoryBean.setName(c.getString(c.getColumnIndex(CategoryBean.KEY_NAME)));
                categoryBean.setIdIcon(c.getInt(c.getColumnIndex(CategoryBean.KEY_ID_ICON)));
                categoryBean.setTypeCategory(c.getInt(c.getColumnIndex(CategoryBean.KEY_TYPE_CATEGORY)));
                //categoryBean.setLimitCash(c.getInt(c.getColumnIndex(CategoryBean.KEY_LIMIT_CASH)));
                categories.add(categoryBean);
            }while (c.moveToNext());
        }
        c.close();
        return categories;
    }

}
