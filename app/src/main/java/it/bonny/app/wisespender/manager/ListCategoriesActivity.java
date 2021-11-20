package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;

public class ListCategoriesActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ListView listViewCategoriesIn, listViewCategoriesOut;
    private ConstraintLayout containerListCategoryIn, containerListCategoryOut, containerTitleCategoryIn, containerTitleCategoryOut;
    private MaterialButton buttonNewCategory;
    private AppCompatImageView iconCategoryIn, iconCategoryOut;
    private TextView textEmptyListIn, textEmptyListOut;
    private boolean isOpenListIn = false, isOpenListOut = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_categories);
        init();

        db = new DatabaseHelper(getApplicationContext());

        List<CategoryBean> categoryBeanListIn = db.getAllCategoryBeansToTypeCategory(TypeObjectBean.CATEGORY_INCOME);
        List<CategoryBean> categoryBeanListOut = db.getAllCategoryBeansToTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
        db.closeDB();

        if(categoryBeanListIn != null && categoryBeanListIn.size() > 0) {
            //TODO: Adapter
        }else {
            textEmptyListIn.setVisibility(View.VISIBLE);
        }
        if(categoryBeanListOut != null && categoryBeanListOut.size() > 0) {
            //TODO: Adapter
        }else {
            textEmptyListOut.setVisibility(View.VISIBLE);
        }

        containerTitleCategoryIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpenListIn) {
                    isOpenListIn = false;
                    iconCategoryIn.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_arrow_down_close_list));
                    containerListCategoryIn.setVisibility(View.GONE);
                }else {
                    isOpenListIn = true;
                    iconCategoryIn.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_arrow_up_open_list));
                    containerListCategoryIn.setVisibility(View.VISIBLE);
                }
            }
        });

        containerTitleCategoryOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOpenListOut) {
                    isOpenListOut = false;
                    iconCategoryOut.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_arrow_down_close_list));
                    containerListCategoryOut.setVisibility(View.GONE);
                }else {
                    isOpenListOut = true;
                    iconCategoryOut.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.ic_arrow_up_open_list));
                    containerListCategoryOut.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void init() {
        buttonNewCategory = findViewById(R.id.buttonNewCategory);
        containerListCategoryIn = findViewById(R.id.containerListCategoryIn);
        containerListCategoryOut = findViewById(R.id.containerListCategoryOut);

        iconCategoryIn = findViewById(R.id.iconCategoryIn);
        iconCategoryOut = findViewById(R.id.iconCategoryOut);

        listViewCategoriesIn = findViewById(R.id.listViewCategoriesIn);
        listViewCategoriesOut = findViewById(R.id.listViewCategoriesOut);

        textEmptyListIn = findViewById(R.id.textEmptyListIn);
        textEmptyListOut = findViewById(R.id.textEmptyListOut);

        containerTitleCategoryIn = findViewById(R.id.containerTitleCategoryIn);
        containerTitleCategoryOut = findViewById(R.id.containerTitleCategoryOut);

    }

}