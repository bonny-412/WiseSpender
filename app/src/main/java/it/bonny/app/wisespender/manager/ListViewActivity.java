package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.ChooseAccountCategoryAdapter;

public class ListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        int typeList = getIntent().getIntExtra("typeList", 0);//0 --> account, 1 --> category
        int typeCategory = getIntent().getIntExtra("typeCategory", -1);
        int selectedPos = getIntent().getIntExtra("selectedPos", -1);
        long id = getIntent().getLongExtra("id", 0);

        MaterialCardView btnReturn = findViewById(R.id.btnReturn);
        btnReturn.setOnClickListener(view -> finish());

        final long[] _id = {0};

        DatabaseHelper db = new DatabaseHelper(this);
        TextView titleAlert = findViewById(R.id.titleAlert);
        ListView listView = findViewById(R.id.listView);
        ChooseAccountCategoryAdapter myAdapter;
        List<AccountBean> accountBeans = null;
        List<CategoryBean> categoryBeans = null;

        if(typeList == 0) {
            titleAlert.setText(getString(R.string.choose_account));
            accountBeans = db.getAllAccountBeansNoMaster();
            myAdapter = new ChooseAccountCategoryAdapter(accountBeans, null, selectedPos, id, this);
        }else {
            titleAlert.setText(getString(R.string.choose_category));
            if(typeCategory == 0)
                categoryBeans = db.getAllCategoryBeansToTypeCategory(TypeObjectBean.CATEGORY_INCOME);
            else
                categoryBeans = db.getAllCategoryBeansToTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
            myAdapter = new ChooseAccountCategoryAdapter(null, categoryBeans, selectedPos, id,this);
        }
        listView.setAdapter(myAdapter);
        listView.setDividerHeight(0);
        listView.setDivider(null);

        List<AccountBean> finalAccountBeans = accountBeans;
        List<CategoryBean> finalCategoryBeans = categoryBeans;
        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            if(typeList == 0)
                _id[0] = finalAccountBeans.get(position).getId();
            else
                _id[0] = finalCategoryBeans.get(position).getId();

            Intent intent = new Intent();
            intent.putExtra("typeList", typeList);
            intent.putExtra("_id", _id[0]);
            intent.putExtra("position", position);
            setResult(Activity.RESULT_OK, intent);
            finish();

        });

    }
}