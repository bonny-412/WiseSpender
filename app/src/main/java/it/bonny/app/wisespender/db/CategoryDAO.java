package it.bonny.app.wisespender.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.bean.Category;
import it.bonny.app.wisespender.bean.CategoryType;

public class CategoryDAO {

    public long insertCategory(Category category, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Category.KEY_NAME, category.getName());
        values.put(Category.KEY_ID_CATEGORY_ASSOCIATED, category.getIdCategoryAssociated());
        values.put(Category.KEY_ID_ICON, category.getIdIcon());
        values.put(Category.KEY_TYPE_CATEGORY, category.getTypeCategory());
        values.put(Category.KEY_LIMIT_CASH, category.getLimitCash());
        return db.insert(Category.TABLE, null, values);
    }

    public int updateCategory(Category category, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(Category.KEY_NAME, category.getName());
        values.put(Category.KEY_ID_CATEGORY_ASSOCIATED, category.getIdCategoryAssociated());
        values.put(Category.KEY_ID_ICON, category.getIdIcon());
        values.put(Category.KEY_TYPE_CATEGORY, category.getTypeCategory());
        values.put(Category.KEY_LIMIT_CASH, category.getLimitCash());
        return db.update(Category.TABLE, values, Category.KEY_ID + " = ?",
                new String[] {String.valueOf(category.getId())});
    }

    public void deleteCategory(long id, SQLiteDatabase db) {
        db.delete(Category.TABLE, Category.KEY_ID + " = ?",
                new String[] {String.valueOf(id)});
    }

    public Category getCategoryById(long id, SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM " + Category.TABLE + " WHERE " + Category.KEY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        Category category = new Category();
        if(c != null) {
            c.moveToFirst();

            category.setId(c.getInt(c.getColumnIndex(Category.KEY_ID)));
            category.setName(c.getString(c.getColumnIndex(Category.KEY_NAME)));
            category.setIdCategoryAssociated(c.getInt(c.getColumnIndex(Category.KEY_ID_CATEGORY_ASSOCIATED)));
            category.setIdIcon(c.getInt(c.getColumnIndex(Category.KEY_ID_ICON)));
            category.setTypeCategory(c.getInt(c.getColumnIndex(Category.KEY_TYPE_CATEGORY)));
            category.setLimitCash(c.getInt(c.getColumnIndex(Category.KEY_LIMIT_CASH)));

        }
        if(c != null)
            c.close();
        return category;
    }

    public List<Category> getAllCategoryIncome(SQLiteDatabase db) {
        List<Category> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Category.TABLE + " c WHERE c." + Category.KEY_TYPE_CATEGORY + " = " + CategoryType.INCOME;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(Category.KEY_ID)));
                category.setName(c.getString(c.getColumnIndex(Category.KEY_NAME)));
                category.setIdCategoryAssociated(c.getInt(c.getColumnIndex(Category.KEY_ID_CATEGORY_ASSOCIATED)));
                category.setIdIcon(c.getInt(c.getColumnIndex(Category.KEY_ID_ICON)));
                category.setTypeCategory(c.getInt(c.getColumnIndex(Category.KEY_TYPE_CATEGORY)));
                category.setLimitCash(c.getInt(c.getColumnIndex(Category.KEY_LIMIT_CASH)));
                categories.add(category);
            }while (c.moveToNext());
        }
        c.close();
        return categories;
    }

    public List<Category> getAllCategoryExpense(SQLiteDatabase db) {
        List<Category> categories = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Category.TABLE + " c WHERE c." + Category.KEY_TYPE_CATEGORY + " = " + CategoryType.EXPENSES;
        Cursor c = db.rawQuery(selectQuery, null);
        if(c.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(c.getInt(c.getColumnIndex(Category.KEY_ID)));
                category.setName(c.getString(c.getColumnIndex(Category.KEY_NAME)));
                category.setIdCategoryAssociated(c.getInt(c.getColumnIndex(Category.KEY_ID_CATEGORY_ASSOCIATED)));
                category.setIdIcon(c.getInt(c.getColumnIndex(Category.KEY_ID_ICON)));
                category.setTypeCategory(c.getInt(c.getColumnIndex(Category.KEY_TYPE_CATEGORY)));
                category.setLimitCash(c.getInt(c.getColumnIndex(Category.KEY_LIMIT_CASH)));
                categories.add(category);
            }while (c.moveToNext());
        }
        c.close();
        return categories;
    }

}
