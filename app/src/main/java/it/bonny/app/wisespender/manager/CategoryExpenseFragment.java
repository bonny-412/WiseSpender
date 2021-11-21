package it.bonny.app.wisespender.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.CategoryListViewAdapter;

public class CategoryExpenseFragment extends Fragment {
    private View root;
    private List<CategoryBean> categoryBeanList;
    private ListView listViewCategory;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_category_expense, container, false);

        callQuery();

        listViewCategory = root.findViewById(R.id.listViewCategory);
        listViewCategory.setAdapter(new CategoryListViewAdapter(categoryBeanList, root.getContext()));
        listViewCategory.setDivider(null);
        listViewCategory.setDividerHeight(0);

        MaterialButton buttonNewCategory = root.findViewById(R.id.buttonNewCategory);
        buttonNewCategory.setOnClickListener(view -> {
            Intent intent = new Intent(root.getContext(), NewEditCategoryActivity.class);
            intent.putExtra("idCategory", "");
            intent.putExtra("typeCategory", "" + TypeObjectBean.CATEGORY_EXPENSE);
            root.getContext().startActivity(intent);
        });

        return root;
    }

    @Override
    public void onResume(){
        super.onResume();
        callQuery();
        listViewCategory.setAdapter(new CategoryListViewAdapter(categoryBeanList, root.getContext()));
    }

    private void callQuery() {
        DatabaseHelper db = new DatabaseHelper(root.getContext());
        categoryBeanList = db.getAllCategoryBeansToTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
        db.closeDB();
    }

}
