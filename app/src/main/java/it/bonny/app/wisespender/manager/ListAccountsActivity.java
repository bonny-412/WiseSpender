package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.ListAccountsAdapter;

public class ListAccountsActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_accounts);

        db = new DatabaseHelper(getApplicationContext());
        listView = findViewById(R.id.listViewAccounts);
        MaterialButton buttonNewAccount = findViewById(R.id.buttonNewAccount);
        List<AccountBean> accountBeanList = db.getAllAccountBeansNoMaster();
        db.closeDB();
        if(accountBeanList != null && accountBeanList.size() > 0) {
            ListAccountsAdapter listAccountsAdapter = new ListAccountsAdapter(accountBeanList, this);
            listView.setAdapter(listAccountsAdapter);
        }else {
            TextView textEmptyList = findViewById(R.id.textEmptyList);
            if(textEmptyList != null)
                textEmptyList.setVisibility(View.VISIBLE);
        }

        MaterialButton returnAccount = findViewById(R.id.returnAccount);
        returnAccount.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);
        });

        buttonNewAccount.setOnClickListener(view1 -> {
            Intent intent = new Intent(getApplicationContext(), NewEditAccountActivity.class);
            intent.putExtra("idAccount", "");
            view1.getContext().startActivity(intent);
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        List<AccountBean> accountBeanList = db.getAllAccountBeansNoMaster();
        db.closeDB();
        if(accountBeanList != null && accountBeanList.size() > 0) {
            ListAccountsAdapter listAccountsAdapter = new ListAccountsAdapter(accountBeanList, this);
            listView.setAdapter(listAccountsAdapter);
        }else {
            TextView textEmptyList = findViewById(R.id.textEmptyList);
            if(textEmptyList != null)
                textEmptyList.setVisibility(View.VISIBLE);
        }
    }
}