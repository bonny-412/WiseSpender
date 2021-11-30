package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.w3c.dom.Text;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.ListTransactionsAdapter;
import it.bonny.app.wisespender.util.Utility;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private DatabaseHelper db;
    private final Utility utility = new Utility();
    private MaterialCardView cardViewAccount, cardViewCategory, cardViewTransaction;
    private TextView accountName, showAccountListBtn, moneyAccount, showTransactionListBtn;
    private final Activity activity = this;
    private AppCompatTextView totalIncome, totalExpense;
    private AccountBean accountBeanSelected;
    private BottomSheetAccount bottomSheetAccount;
    private ListView listTransactions;
    private TextView listTransactionsEmpty;
    private ExtendedFloatingActionButton btnNewTransaction;
    private List<TransactionBean> transactionBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        showWelcomeAlert();
        callDB();

        cardViewAccount.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ListAccountsActivity.class);
            startActivity(intent);
        });

        cardViewCategory.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ListCategoriesActivity.class);
            startActivity(intent);
        });

        cardViewTransaction.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ListTransactionActivity.class);
            startActivity(intent);
        });

        accountName.setText(accountBeanSelected.getName());
        showAccountListBtn.setOnClickListener(view -> bottomSheetAccount.show(getSupportFragmentManager(), "TAG"));

        showTransactionListBtn.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ListTransactionActivity.class);
            startActivity(intent);
        });

        btnNewTransaction.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
            startActivity(intent);
        });

        listTransactions.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(activity, TransactionDetailActivity.class);
            intent.putExtra("idTransaction", transactionBeanList.get(i).getId());
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
        listTransactions.setDivider(null);
        listTransactions.setDividerHeight(0);
        listTransactionsEmpty = findViewById(R.id.listTransactionsEmpty);
        btnNewTransaction = findViewById(R.id.btnNewTransaction);
        cardViewTransaction = findViewById(R.id.cardViewTransaction);
        showTransactionListBtn = findViewById(R.id.showTransactionListBtn);
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
            if(activity != null) {
                utility.insertAccountDefault(db, activity);
                utility.insertCategoryDefault(db, activity);
                SharedPreferences.Editor editor = activity.getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE).edit();
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
        List<AccountBean> accountBeanList = db.getAllAccountBeansNoMaster();
        accountBeanSelected = db.getAccountBeanSelected();
        transactionBeanList = db.getAllTransactionBeansToMainActivity(accountBeanSelected);
        db.closeDB();

        if(transactionBeanList != null && transactionBeanList.size() > 0) {
            listTransactions.setVisibility(View.VISIBLE);
            listTransactionsEmpty.setVisibility(View.GONE);
            ListTransactionsAdapter listTransactionsAdapter = new ListTransactionsAdapter(transactionBeanList, activity);
            listTransactions.setAdapter(listTransactionsAdapter);
            listTransactionsAdapter.notifyDataSetChanged();
        }else {
            listTransactions.setVisibility(View.GONE);
            listTransactionsEmpty.setVisibility(View.VISIBLE);
        }

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
        moneyAccount.setText(totMoneyAccount);
        totalIncome.setText(totMoneyAccountIncome);
        totalExpense.setText(totMoneyAccountExpense);

        bottomSheetAccount = new BottomSheetAccount(activity);
    }

}