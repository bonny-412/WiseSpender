package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.component.RecyclerViewClickInterface;
import it.bonny.app.wisespender.component.TransactionListAdapter;
import it.bonny.app.wisespender.component.LoadingDialog;
import it.bonny.app.wisespender.util.Utility;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface  {

    private long backPressedTime;
    private DatabaseHelper db;
    private final Utility utility = new Utility();
    private MaterialCardView cardViewAccount, cardViewCategory, cardViewTransaction;
    private TextView accountName, showAccountListBtn, moneyAccount, showTransactionListBtn;
    private final Activity mActivity = this;
    private AppCompatTextView totalIncome, totalExpense;
    private AccountBean accountBeanSelected;
    private BottomSheetAccount bottomSheetAccount;
    private RecyclerView listTransactions;
    private ImageView listTransactionsEmpty;
    private ExtendedFloatingActionButton btnNewTransaction;
    private List<TransactionBean> transactionBeanList = new ArrayList<>();
    private AppCompatImageView imageViewTransactions, imageViewAccounts, imageViewCategory;
    private LoadingDialog loadingDialog;
    private TransactionListAdapter transactionListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        showWelcomeAlert();

        transactionListAdapter = new TransactionListAdapter(transactionBeanList, mActivity, true, this);
        listTransactions.setHasFixedSize(true);
        listTransactions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listTransactions.setAdapter(transactionListAdapter);

        cardViewAccount.setOnClickListener(view -> {
            if(imageViewAccounts == null)
                imageViewAccounts = findViewById(R.id.imageViewAccounts);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageViewAccounts, "transition_account_icon");
            Intent intent = new Intent(mActivity, ListAccountsActivity.class);
            startActivity(intent, options.toBundle());
        });

        cardViewCategory.setOnClickListener(view -> {
            if(imageViewCategory == null)
                imageViewCategory = findViewById(R.id.imageViewCategory);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageViewCategory, "transition_category_icon");
            Intent intent = new Intent(mActivity, ListCategoriesActivity.class);
            startActivity(intent, options.toBundle());
        });

        cardViewTransaction.setOnClickListener(view -> {
            if(imageViewTransactions == null)
                imageViewTransactions = findViewById(R.id.imageViewTransactions);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, imageViewTransactions, "transition_transaction_icon");
            Intent intent = new Intent(mActivity, ListTransactionActivity.class);
            startActivity(intent, options.toBundle());
        });

        showAccountListBtn.setOnClickListener(view -> bottomSheetAccount.show(getSupportFragmentManager(), "TAG"));

        showTransactionListBtn.setOnClickListener(view -> {
            Intent intent = new Intent(mActivity, ListTransactionActivity.class);
            startActivity(intent);
        });

        btnNewTransaction.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
            intent.putExtra("transactionBean", new TransactionBean());
            startActivity(intent);
        });

    }

    private void init() {
        db = new DatabaseHelper(getApplicationContext());
        showAccountListBtn = findViewById(R.id.showAccountListBtn);
        accountName = findViewById(R.id.accountName);
        cardViewAccount = findViewById(R.id.cardViewAccount);
        cardViewCategory = findViewById(R.id.cardViewCategory);
        moneyAccount = findViewById(R.id.moneyAccount);
        totalIncome = findViewById(R.id.totalIncome);
        totalExpense = findViewById(R.id.totalExpense);
        listTransactions = findViewById(R.id.listTransactions);

        listTransactionsEmpty = findViewById(R.id.listTransactionsEmpty);
        btnNewTransaction = findViewById(R.id.btnNewTransaction);
        cardViewTransaction = findViewById(R.id.cardViewTransaction);
        showTransactionListBtn = findViewById(R.id.showTransactionListBtn);
        loadingDialog = new LoadingDialog(MainActivity.this);

    }

    //Shows the welcome alert
    private void showWelcomeAlert(){
        boolean firstStart = false;
        SharedPreferences prefs;
        if(getApplicationContext() != null) {
            try {
                prefs = getApplicationContext().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE);
                firstStart = prefs.getBoolean("firstStart", true);
            }catch (Exception e) {
                //TODO: Firebase
                Log.e("HOME_FRAGMENT", e.toString());
            }
        }
        if(firstStart) {
            if(mActivity != null) {
                utility.insertAccountDefault(db, mActivity);
                utility.insertCategoryDefault(db, mActivity);
                SharedPreferences.Editor editor = mActivity.getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE).edit();
                editor.putBoolean("firstStart", false);
                editor.apply();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(a);
        } else
            Toast.makeText(this, getString(R.string.pressToExit), Toast.LENGTH_SHORT).show();

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        callDB();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void callDB() {
        loadingDialog.setTitle(getString(R.string.loading_alert_loading));
        loadingDialog.startDialog();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            List<AccountBean> accountBeanList = db.getAllAccountBeansNoMaster();
            accountBeanSelected = db.getAccountBeanSelected();
            transactionBeanList = db.getAllTransactionBeansToMainActivity(accountBeanSelected);

            String totMoneyAccount, totMoneyAccountIncome, totMoneyAccountExpense;
            if(accountBeanSelected.getIsMaster() == TypeObjectBean.IS_MASTER) {
                totMoneyAccount = utility.getTotMoneyAccountByAllAccounts(accountBeanList, null);
                totMoneyAccountIncome = utility.getTotMoneyIncomeAccountByAllAccounts(accountBeanList, null);
                totMoneyAccountExpense = utility.getTotMoneyExpenseAccountByAllAccounts(accountBeanList, null);
            }else {
                totMoneyAccount = utility.getTotMoneyAccountByAllAccounts(null, accountBeanSelected);
                totMoneyAccountIncome = utility.getTotMoneyIncomeAccountByAllAccounts(null, accountBeanSelected);
                totMoneyAccountExpense = utility.getTotMoneyExpenseAccountByAllAccounts(null, accountBeanSelected);
            }

            runOnUiThread(() -> {
                transactionListAdapter.insertTransactionBeanList(transactionBeanList);

                accountName.setText(accountBeanSelected.getName());

                if(transactionBeanList != null && transactionBeanList.size() > 0) {
                    listTransactions.setVisibility(View.VISIBLE);
                    listTransactionsEmpty.setVisibility(View.GONE);
                }else {
                    listTransactions.setVisibility(View.GONE);
                    listTransactionsEmpty.setVisibility(View.VISIBLE);
                }

                moneyAccount.setText(totMoneyAccount);
                totalIncome.setText(totMoneyAccountIncome);
                totalExpense.setText(totMoneyAccountExpense);

                bottomSheetAccount = new BottomSheetAccount(mActivity);
                loadingDialog.dismissDialog();
            });
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(mActivity, TransactionDetailActivity.class);
        intent.putExtra("transactionBean", transactionListAdapter.findTransactionBean(position).getId());
        startActivity(intent);
    }
}