package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.Utility;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private DatabaseHelper db;
    private final Utility utility = new Utility();
    private MaterialCardView btnAccounts, btnCategories;
    private TextView accountName, showAccountListBtn, moneyAccount;
    private final Activity activity = this;
    private LinearLayout buttonTransactions, buttonActivity;
    private boolean isCheckedButtonTransactions = true;
    private ConstraintLayout containerActivity, containerTransactions;
    private AppCompatTextView totalIncome, totalExpense;
    private List<AccountBean> accountBeanList;
    private AccountBean accountBeanSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        showWelcomeAlert();
        getMoney();

        btnAccounts.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ListAccountsActivity.class);
            startActivity(intent);
        });

        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ListCategoriesActivity.class);
                startActivity(intent);
            }
        });

        BottomSheetAccount bottomSheetAccount = new BottomSheetAccount(accountBeanList, activity);
        accountName.setText(accountBeanSelected.getName());
        showAccountListBtn.setOnClickListener(view -> bottomSheetAccount.show(getSupportFragmentManager(), "TAG"));

        buttonTransactions.setOnClickListener(view -> {
            if(!isCheckedButtonTransactions) {
                isCheckedButtonTransactions = true;
                buttonTransactions.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_change_view_checked_background));
                buttonActivity.setBackgroundResource(0);
                buttonTransactions.setElevation(8);
                buttonActivity.setElevation(0);
                containerActivity.setVisibility(View.GONE);
                containerTransactions.setVisibility(View.VISIBLE);
            }
        });

        buttonActivity.setOnClickListener(view -> {
            if(isCheckedButtonTransactions) {
                isCheckedButtonTransactions = false;
                buttonActivity.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_change_view_checked_background));
                buttonTransactions.setBackgroundResource(0);
                buttonActivity.setElevation(8);
                buttonTransactions.setElevation(0);
                containerActivity.setVisibility(View.VISIBLE);
                containerTransactions.setVisibility(View.GONE);
            }
        });

    }

    private void init() {
        db = new DatabaseHelper(getApplicationContext());
        btnAccounts = findViewById(R.id.btnAccounts);
        showAccountListBtn = findViewById(R.id.showAccountListBtn);
        accountName = findViewById(R.id.accountName);
        btnCategories = findViewById(R.id.btnCategories);
        buttonTransactions = findViewById(R.id.buttonTransactions);
        buttonTransactions.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_change_view_checked_background));
        buttonActivity = findViewById(R.id.buttonActivity);
        containerActivity = findViewById(R.id.containerActivity);
        containerTransactions = findViewById(R.id.containerTransactions);
        moneyAccount = findViewById(R.id.moneyAccount);
        totalIncome = findViewById(R.id.totalIncome);
        totalExpense = findViewById(R.id.totalExpense);
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
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStart(){
        super.onStart();
        getMoney();
    }

    private void getMoney() {
        accountBeanList = db.getAllAccountBeans();
        accountBeanSelected = db.getAccountBeanSelected();
        db.closeDB();

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
    }

}