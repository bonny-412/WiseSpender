package it.bonny.app.wisespender.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.PeriodSelectedBean;
import it.bonny.app.wisespender.bean.SettingsBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.component.BottomSheetNewEditTransactionListener;
import it.bonny.app.wisespender.component.BottomSheetPeriod;
import it.bonny.app.wisespender.component.BottomSheetPeriodListener;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.component.RecyclerViewClickInterface;
import it.bonny.app.wisespender.component.TransactionListAdapter;
import it.bonny.app.wisespender.util.Utility;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickInterface, BottomSheetPeriodListener, BottomSheetNewEditTransactionListener {

    private long backPressedTime;
    private DatabaseHelper db;
    private final Utility utility = new Utility();
    private MaterialButton cardViewAccount, cardViewCategory, cardViewTransaction, showTransactionListBtn;
    private TextView accountName, moneyAccount, currencySymbol;
    private final Activity mActivity = this;
    private AppCompatTextView totalIncome, totalExpense;
    private AppCompatImageView iconAccount;
    private AccountBean accountBeanSelected;
    private BottomSheetAccount bottomSheetAccount;
    private RecyclerView listTransactions;
    private ImageView listTransactionsEmpty;
    private ExtendedFloatingActionButton btnNewTransaction;
    private List<TransactionBean> transactionBeanList = new ArrayList<>();
    private TransactionListAdapter transactionListAdapter;
    private Calendar calendar = Calendar.getInstance();
    private ProgressBar progressBar;
    private LinearLayout containerAccountName;
    private MaterialButton btnSettings, btnTransfer;
    private long idAccountSelected;

    private String totMoneyAccount = "", totMoneyAccountIncome, totMoneyAccountExpense;
    private int countAccount = 0;
    private SettingsBean settingsBean;
    private PeriodSelectedBean periodSelectedBean;
    private MaterialButton btnSelectPeriod;
    private TextView titlePeriodSelected;
    private MaterialButton lastPeriodSelected, nextPeriodSelected;
    private Calendar cal2 = Calendar.getInstance();

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);
        SettingsBean settingsBean = new Utility().getSettingsBeanSaved(this);
        if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_DARK_MODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_LIGHT_MODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        periodSelectedBean = new PeriodSelectedBean();
        periodSelectedBean.setPeriodSelectedMain(TypeObjectBean.PERIOD_SELECTED_MONTH);
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

        btnSelectPeriod.setOnClickListener(view -> {
            BottomSheetPeriod bottomSheetPeriod = new BottomSheetPeriod(periodSelectedBean.getPeriodSelectedMain(), MainActivity.this);
            bottomSheetPeriod.show(getSupportFragmentManager(), "SELECT_PERIOD");
        });

        btnTransfer.setOnClickListener(view -> {
            if(countAccount > 1) {
                Intent intent = new Intent(mActivity, TransferActivity.class);
                startActivity(intent);
            }else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanimation);
                btnTransfer.startAnimation(shake);
                Toast.makeText(mActivity, getString(R.string.transfer_count_account_problem), Toast.LENGTH_SHORT).show();
            }
        });

        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(mActivity, SettingsActivity.class);
            startActivity(intent);
        });

        listTransactions.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && btnNewTransaction.getVisibility() == View.VISIBLE) {
                    btnNewTransaction.hide();
                } else if (dy < 0 && btnNewTransaction.getVisibility() != View.VISIBLE) {
                    btnNewTransaction.show();
                }
            }
        });

        lastPeriodSelected.setOnClickListener(view -> {
            if(nextPeriodSelected.getVisibility() == View.INVISIBLE)
                nextPeriodSelected.setVisibility(View.VISIBLE);

            int periodSelected = periodSelectedBean.getPeriodSelectedMain();
            if(periodSelected == TypeObjectBean.PERIOD_SELECTED_DAY) {
                int numDay = calendar.get(Calendar.DAY_OF_MONTH);
                numDay = numDay - 1;
                calendar.set(Calendar.DAY_OF_MONTH, numDay);
                callDB();
            }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_MONTH) {
                int numMonth = calendar.get(Calendar.MONTH);
                numMonth = numMonth - 1;
                calendar.set(Calendar.MONTH, numMonth);
                if(numMonth == 0)
                    lastPeriodSelected.setVisibility(View.INVISIBLE);
                callDB();
            }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_YEAR) {
                int numYear = calendar.get(Calendar.YEAR);
                numYear = numYear - 1;
                calendar.set(Calendar.YEAR, numYear);
                callDB();
            }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_DATE){
                callDatePicker();
            }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_INTERVAL) {
                callDatePickerRange();
            }
        });

        nextPeriodSelected.setOnClickListener(view -> {
            if(lastPeriodSelected.getVisibility() == View.INVISIBLE)
                lastPeriodSelected.setVisibility(View.VISIBLE);

            int periodSelected = periodSelectedBean.getPeriodSelectedMain();
            if(periodSelected == TypeObjectBean.PERIOD_SELECTED_DAY) {
                int numDay = calendar.get(Calendar.DAY_OF_MONTH);
                numDay = numDay + 1;
                calendar.set(Calendar.DAY_OF_MONTH, numDay);
                callDB();
            }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_MONTH) {
                int numMonth = calendar.get(Calendar.MONTH);
                numMonth = numMonth + 1;
                calendar.set(Calendar.MONTH, numMonth);
                if(numMonth == 11)
                    nextPeriodSelected.setVisibility(View.INVISIBLE);
                callDB();
            }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_YEAR) {
                int numYear = calendar.get(Calendar.YEAR);
                numYear = numYear + 1;
                calendar.set(Calendar.YEAR, numYear);
                callDB();
            }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_DATE){
                callDatePicker();
            }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_INTERVAL) {
                callDatePickerRange();
            }
        });

    }

    private void init() {
        db = new DatabaseHelper(getApplicationContext());

        accountName = findViewById(R.id.accountName);
        cardViewAccount = findViewById(R.id.cardViewAccount);
        cardViewCategory = findViewById(R.id.cardViewCategory);
        moneyAccount = findViewById(R.id.moneyAccount);
        totalIncome = findViewById(R.id.totalIncome);
        totalExpense = findViewById(R.id.totalExpense);
        listTransactions = findViewById(R.id.listTransactions);
        iconAccount = findViewById(R.id.iconAccount);
        btnSelectPeriod = findViewById(R.id.btnSelectPeriod);
        btnSettings = findViewById(R.id.btnSettings);
        btnTransfer = findViewById(R.id.btnTransfer);
        titlePeriodSelected = findViewById(R.id.titlePeriodSelected);
        currencySymbol = findViewById(R.id.currencySymbol);

        String textDate = utility.getNameToPeriodSelectedToButton(periodSelectedBean.getPeriodSelectedMain(), mActivity);
        btnSelectPeriod.setText(textDate);

        listTransactionsEmpty = findViewById(R.id.listTransactionsEmpty);
        btnNewTransaction = findViewById(R.id.btnNewTransaction);
        cardViewTransaction = findViewById(R.id.cardViewTransaction);
        showTransactionListBtn = findViewById(R.id.showTransactionListBtn);
        progressBar = findViewById(R.id.progressBar);
        nextPeriodSelected = findViewById(R.id.nextPeriodSelected);
        lastPeriodSelected = findViewById(R.id.lastPeriodSelected);

        containerAccountName = findViewById(R.id.containerAccountName);

        accountName.setSelected(true);
        accountName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        accountName.setHorizontallyScrolling(true);
        accountName.setSingleLine(true);
        accountName.setLines(1);

        titlePeriodSelected.setSelected(true);
        titlePeriodSelected.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        titlePeriodSelected.setHorizontallyScrolling(true);
        titlePeriodSelected.setSingleLine(true);
        titlePeriodSelected.setLines(1);
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
        db.close();
        super.onDestroy();
    }

    @Override
    public void onResume(){
        settingsBean = utility.getSettingsBeanSaved(mActivity);
        currencySymbol.setText(settingsBean.getCurrency());
        callDB();
        super.onResume();
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
        callDB(null);
    }

    private void callDB(Calendar cal2) {
        progressBar.setVisibility(View.VISIBLE);
        listTransactions.setVisibility(View.GONE);
        listTransactionsEmpty.setVisibility(View.GONE);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {

            countAccount = db.countAccountNoMaster();

            accountBeanSelected = db.getAccountBeanSelected();
            idAccountSelected = accountBeanSelected.getId();

            utility.getDateFromToPeriodSelected(periodSelectedBean, calendar, cal2, getApplicationContext());

            if(accountBeanSelected.getIsMaster() == TypeObjectBean.IS_MASTER) {
                String ids = db.getAllIdAccountNoMaster();
                int totMoneyIncomeInt = db.getSumTransactionsByAccountAndType(0, ids, periodSelectedBean.getDateFrom(), periodSelectedBean.getDateTo(), TypeObjectBean.TRANSACTION_INCOME);
                int totMoneyExpenseInt = db.getSumTransactionsByAccountAndType(0, ids, periodSelectedBean.getDateFrom(), periodSelectedBean.getDateTo(), TypeObjectBean.TRANSACTION_EXPENSE);

                calculateAmount(totMoneyIncomeInt, totMoneyExpenseInt);

                transactionBeanList = db.getAllTransactionBeansToMainActivity(0, ids, periodSelectedBean.getDateFrom(), periodSelectedBean.getDateTo());
            }else {
                int totMoneyIncomeInt = db.getSumTransactionsByAccountAndType(accountBeanSelected.getId(), null, periodSelectedBean.getDateFrom(), periodSelectedBean.getDateTo(), TypeObjectBean.TRANSACTION_INCOME);
                int totMoneyExpenseInt = db.getSumTransactionsByAccountAndType(accountBeanSelected.getId(), null, periodSelectedBean.getDateFrom(), periodSelectedBean.getDateTo(), TypeObjectBean.TRANSACTION_EXPENSE);

                calculateAmount(totMoneyIncomeInt, totMoneyExpenseInt);
                transactionBeanList = db.getAllTransactionBeansToMainActivity(accountBeanSelected.getId(), null, periodSelectedBean.getDateFrom(), periodSelectedBean.getDateTo());
            }

            runOnUiThread(() -> {
                transactionListAdapter.insertTransactionBeanList(transactionBeanList);

                accountName.setText(accountBeanSelected.getName());
                iconAccount.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBeanSelected)));

                titlePeriodSelected.setText(periodSelectedBean.getTextPeriodSelected());

                moneyAccount.setText(totMoneyAccount);
                totalIncome.setText(totMoneyAccountIncome);
                totalExpense.setText(totMoneyAccountExpense);

                bottomSheetAccount = new BottomSheetAccount(idAccountSelected, mActivity);

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
    public void onReturnMonth(int periodSelected) {
        resetPeriodSelected(periodSelected);
    }

    private void resetPeriodSelected(int periodSelected) {
        calendar = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        periodSelectedBean.setPeriodSelectedMain(periodSelected);
        String text = utility.getNameToPeriodSelectedToButton(periodSelected, mActivity);
        btnSelectPeriod.setText(text);

        if(periodSelected == TypeObjectBean.PERIOD_SELECTED_ALL) {
            lastPeriodSelected.setVisibility(View.INVISIBLE);
            nextPeriodSelected.setVisibility(View.INVISIBLE);
        }else {
            if(lastPeriodSelected.getVisibility() == View.INVISIBLE)
                lastPeriodSelected.setVisibility(View.VISIBLE);
            if(nextPeriodSelected.getVisibility() == View.INVISIBLE)
                nextPeriodSelected.setVisibility(View.VISIBLE);
        }

        if(periodSelected == TypeObjectBean.PERIOD_SELECTED_INTERVAL) {
            callDatePickerRange();
        }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_DATE) {
            callDatePicker();
        }else {
            callDB();
        }
    }

    private void callDatePicker() {
        MaterialDatePicker.Builder<Long> materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        MaterialDatePicker<Long> materialDatePicker = materialDateBuilder.build();
        materialDatePicker.setCancelable(false);
        materialDateBuilder.setSelection(calendar.getTimeInMillis());
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            calendar.setTimeInMillis(selection);
            callDB();
        });
        materialDatePicker.addOnNegativeButtonClickListener(view -> resetPeriodSelected(TypeObjectBean.PERIOD_SELECTED_MONTH));
    }

    private void callDatePickerRange() {
        MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();
        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = materialDateBuilder.build();
        materialDatePicker.setCancelable(false);
        materialDateBuilder.setSelection(new Pair<>(calendar.getTimeInMillis(), cal2.getTimeInMillis()));
        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            calendar.setTimeInMillis(selection.first);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTimeInMillis(selection.second);
            callDB(cal2);
        });
        materialDatePicker.addOnNegativeButtonClickListener(view -> resetPeriodSelected(TypeObjectBean.PERIOD_SELECTED_MONTH));
    }

    @Override
    public void onItemClick(long idElement, boolean isCategory) {
        this.idAccountSelected = idElement;
        callDB();
    }
}