package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.component.BottomSheetPeriod;
import it.bonny.app.wisespender.component.BottomSheetPeriodListener;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.component.RecyclerViewClickInterface;
import it.bonny.app.wisespender.component.TransactionListAdapter;
import it.bonny.app.wisespender.util.Utility;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface, BottomSheetPeriodListener {

    private long backPressedTime;
    private DatabaseHelper db;
    private final Utility utility = new Utility();
    private MaterialButton cardViewAccount, cardViewCategory, cardViewTransaction, showTransactionListBtn, btnDate;
    private TextView accountName, moneyAccount;
    private final Activity mActivity = this;
    private AppCompatTextView totalIncome, totalExpense;
    private AppCompatImageView iconAccount;
    private AccountBean accountBeanSelected;
    private BottomSheetAccount bottomSheetAccount;
    private RecyclerView listTransactions;
    private ImageView listTransactionsEmpty;
    private ExtendedFloatingActionButton btnNewTransaction;
    private List<TransactionBean> transactionBeanList = new ArrayList<>();
    private AppCompatImageView imageViewTransactions, imageViewAccounts, imageViewCategory;
    private TransactionListAdapter transactionListAdapter;
    private final Calendar calendar = Calendar.getInstance();
    private int yearSelected, monthSelected;
    private List<String> months;
    private ProgressBar progressBar;
    private LinearLayout containerAccountName;

    private String totMoneyAccount = "", totMoneyAccountIncome, totMoneyAccountExpense;

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
            Intent intent = new Intent(mActivity, ListAccountsActivity.class);
            startActivity(intent);
        });

        cardViewCategory.setOnClickListener(view -> {
            Intent intent = new Intent(mActivity, ListCategoriesActivity.class);
            startActivity(intent);
        });

        cardViewTransaction.setOnClickListener(view -> {
            Intent intent = new Intent(mActivity, ListTransactionActivity.class);
            startActivity(intent);
        });

        containerAccountName.setOnClickListener(view -> bottomSheetAccount.show(getSupportFragmentManager(), "CHANGE_ACCOUNT"));

        showTransactionListBtn.setOnClickListener(view -> {
            Intent intent = new Intent(mActivity, ListTransactionActivity.class);
            startActivity(intent);
        });

        btnNewTransaction.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
            intent.putExtra("transactionBean", new TransactionBean());
            startActivity(intent);
        });

        btnDate.setOnClickListener(view -> {
            BottomSheetPeriod bottomSheetPeriod = new BottomSheetPeriod(monthSelected, yearSelected, months, MainActivity.this);
            bottomSheetPeriod.show(getSupportFragmentManager(), "SELECT_PERIOD");
        });

    }

    private void init() {
        db = new DatabaseHelper(getApplicationContext());
        months = utility.getShortNameMonth();

        accountName = findViewById(R.id.accountName);
        cardViewAccount = findViewById(R.id.cardViewAccount);
        cardViewCategory = findViewById(R.id.cardViewCategory);
        moneyAccount = findViewById(R.id.moneyAccount);
        totalIncome = findViewById(R.id.totalIncome);
        totalExpense = findViewById(R.id.totalExpense);
        listTransactions = findViewById(R.id.listTransactions);
        iconAccount = findViewById(R.id.iconAccount);
        btnDate = findViewById(R.id.btnDate);

        String textDate = utility.getNameMonthYearByCalendar(calendar);
        btnDate.setText(textDate);
        yearSelected = calendar.get(Calendar.YEAR);
        monthSelected = calendar.get(Calendar.MONTH);

        listTransactionsEmpty = findViewById(R.id.listTransactionsEmpty);
        btnNewTransaction = findViewById(R.id.btnNewTransaction);
        cardViewTransaction = findViewById(R.id.cardViewTransaction);
        showTransactionListBtn = findViewById(R.id.showTransactionListBtn);
        progressBar = findViewById(R.id.progressBar);

        containerAccountName = findViewById(R.id.containerAccountName);

        accountName.setSelected(true);
        accountName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        accountName.setHorizontallyScrolling(true);
        accountName.setSingleLine(true);
        accountName.setLines(1);

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
        progressBar.setVisibility(View.VISIBLE);
        listTransactions.setVisibility(View.GONE);
        listTransactionsEmpty.setVisibility(View.GONE);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            List<AccountBean> accountBeanList = db.getAllAccountBeansNoMaster();
            accountBeanSelected = db.getAccountBeanSelected();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            Calendar firstDayMonth = Calendar.getInstance();
            firstDayMonth.set(Calendar.DAY_OF_MONTH, 1);
            firstDayMonth.set(Calendar.MONTH, monthSelected);
            firstDayMonth.set(Calendar.YEAR, yearSelected);

            Calendar lastDayMonth = Calendar.getInstance();
            lastDayMonth.set(Calendar.MONTH, monthSelected);
            lastDayMonth.set(Calendar.YEAR, yearSelected);
            lastDayMonth.set(Calendar.DAY_OF_MONTH, lastDayMonth.getActualMaximum(Calendar.DAY_OF_MONTH));

            String from = dateFormat.format(firstDayMonth.getTime()) + " 00:00";
            String a = dateFormat.format(lastDayMonth.getTime()) + " 23:59";
            transactionBeanList = db.getAllTransactionBeansToMainActivity(accountBeanSelected, from, a);


            if(accountBeanSelected.getIsMaster() == TypeObjectBean.IS_MASTER) {
                String ids = db.getAllIdAccountNoMaster();
                int totMoneyIncomeInt = db.getSumTransactionsByAccountAndType(0, ids, from, a, TypeObjectBean.TRANSACTION_INCOME);
                int totMoneyExpenseInt = db.getSumTransactionsByAccountAndType(0, ids, from, a, TypeObjectBean.TRANSACTION_EXPENSE);

                calculateAmount(totMoneyIncomeInt, totMoneyExpenseInt);
            }else {
                int totMoneyIncomeInt = db.getSumTransactionsByAccountAndType(accountBeanSelected.getId(), null, from, a, TypeObjectBean.TRANSACTION_INCOME);
                int totMoneyExpenseInt = db.getSumTransactionsByAccountAndType(accountBeanSelected.getId(), null, from, a, TypeObjectBean.TRANSACTION_EXPENSE);

                calculateAmount(totMoneyIncomeInt, totMoneyExpenseInt);
            }

            runOnUiThread(() -> {
                transactionListAdapter.insertTransactionBeanList(transactionBeanList);

                accountName.setText(accountBeanSelected.getName());
                iconAccount.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBeanSelected)));


                moneyAccount.setText(totMoneyAccount);
                totalIncome.setText(totMoneyAccountIncome);
                totalExpense.setText(totMoneyAccountExpense);

                bottomSheetAccount = new BottomSheetAccount(mActivity);

                progressBar.setVisibility(View.GONE);
                if(transactionBeanList != null && transactionBeanList.size() > 0) {
                    listTransactions.setVisibility(View.VISIBLE);
                    listTransactionsEmpty.setVisibility(View.GONE);
                }else {
                    listTransactions.setVisibility(View.GONE);
                    listTransactionsEmpty.setVisibility(View.VISIBLE);
                }
            });
        });
    }

    private void calculateAmount(int totMoneyIncomeInt, int totMoneyExpenseInt) {
        int totMoney;
        if(totMoneyExpenseInt > totMoneyIncomeInt) {
            totMoneyAccount = "- ";
            totMoney = totMoneyExpenseInt - totMoneyIncomeInt;
        }else {
            totMoneyAccount = "";
            totMoney = totMoneyIncomeInt - totMoneyExpenseInt;
        }

        totMoneyAccount += utility.formatNumberCurrency(utility.convertIntInEditTextValue(totMoney).toString());
        totMoneyAccountIncome = utility.formatNumberCurrency(utility.convertIntInEditTextValue(totMoneyIncomeInt).toString());
        totMoneyAccountExpense = utility.formatNumberCurrency(utility.convertIntInEditTextValue(totMoneyExpenseInt).toString());
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(mActivity, TransactionDetailActivity.class);
        TransactionBean transactionBean = transactionListAdapter.findTransactionBean(position);
        intent.putExtra("transactionBean", transactionBean);
        startActivity(intent);
    }

    @Override
    public void onReturnMonth(int month, int year) {
        monthSelected = month;
        yearSelected = year;
        String monthText = months.get(month);
        String textButton = monthText + " " + year;
        btnDate.setText(textButton);
        callDB();
    }
}