package it.bonny.app.wisespender.manager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.AccountListAdapter;
import it.bonny.app.wisespender.util.RecyclerViewClickAccountInterface;

public class ListAccountsActivity extends AppCompatActivity implements RecyclerViewClickAccountInterface {
    private DatabaseHelper db;
    private RecyclerView listView;
    private ImageView imageView;
    private ProgressBar progressBar;
    private MaterialButton buttonNewAccount;
    private MaterialCardView returnAccount;
    private AccountListAdapter accountListAdapter;
    private List<AccountBean> accountBeanList = new ArrayList<>();
    private int pos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_accounts);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        init();
        callDB();
        accountListAdapter = new AccountListAdapter(accountBeanList, ListAccountsActivity.this, this);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listView.setAdapter(accountListAdapter);

        returnAccount.setOnClickListener(view -> finish());

        buttonNewAccount.setOnClickListener(view1 -> {
            Intent intent = new Intent(getApplicationContext(), NewEditAccountActivity.class);
            intent.putExtra("accountBean", new AccountBean());
            openSomeActivityForResult(intent);
        });

    }

    private void init() {
        db = new DatabaseHelper(getApplicationContext());
        progressBar = findViewById(R.id.progressBar);
        listView = findViewById(R.id.listViewAccounts);
        buttonNewAccount = findViewById(R.id.buttonNewAccount);
        imageView = findViewById(R.id.listEmpty);
        returnAccount = findViewById(R.id.returnAccount);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void callDB() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);

        service.execute(() -> {
            accountBeanList = db.getAllAccountBeansNoMaster();

            runOnUiThread(() -> {
                accountListAdapter.insertAccountBeanList(accountBeanList);
                if(accountBeanList != null && accountBeanList.size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                }else {
                    imageView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                progressBar.setVisibility(View.GONE);
            });

        });
    }

    @Override
    public void onItemClick(int position, AccountBean accountBean) {
        pos = position;
        Intent intent = new Intent(ListAccountsActivity.this, NewEditAccountActivity.class);
        intent.putExtra("accountBean", accountListAdapter.findAccountBean(pos));
        openSomeActivityForResult(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data != null) {
                    int typeList = data.getIntExtra("typeAction", -1);
                    AccountBean accountBean = data.getParcelableExtra("accountBean");
                    if(typeList == TypeObjectBean.RETURN_DELETE) {
                        accountListAdapter.deleteAccountBean(pos);
                    }else if(typeList == TypeObjectBean.RETURN_EDIT) {
                        accountListAdapter.updateAccountBean(accountBean, pos);
                    }else if(typeList == TypeObjectBean.RETURN_NEW) {
                        accountListAdapter.insertAccountBean(accountBean);
                    }
                }

            }
        }
    });

    public void openSomeActivityForResult(Intent intent) {
        someActivityResultLauncher.launch(intent);
    }

}