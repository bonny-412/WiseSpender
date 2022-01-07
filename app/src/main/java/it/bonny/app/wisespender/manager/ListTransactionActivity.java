package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.FilterTransactionBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.ListTransactionsAdapter;
import it.bonny.app.wisespender.util.Utility;

public class ListTransactionActivity extends AppCompatActivity {

    private LinearLayout btnAllTransaction, btnIncomeTransaction, btnExpenseTransaction, btnFilterDateTransaction;
    private TextView textViewAll, textViewIncome, textViewExpense;
    private int typeTransaction;
    private FilterTransactionBean filterTransactionBean;
    private final Utility utility = new Utility();
    private final Activity activity = this;
    private MaterialCardView btnReturn;
    private DatabaseHelper db;
    private ImageView imgListEmpty;
    private ListView listViewCategoriesTransaction;
    private List<String> listDateFilters;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private Pair<Long, Long> selectionDates = null;
    private AccountBean accountBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaction);
        filterTransactionBean = utility.getFilterTransactionBeanSaved(activity);

        init();
        getDateMonthToDatePicker();
        accountBean = db.getAccountBeanSelected();
        db.closeDB();
        changeFilterTypeTransaction(filterTransactionBean.getFilterTypeTransaction());

        btnAllTransaction.setOnClickListener(view -> changeFilterTypeTransaction(3));
        btnIncomeTransaction.setOnClickListener(view -> changeFilterTypeTransaction(TypeObjectBean.TRANSACTION_INCOME));
        btnExpenseTransaction.setOnClickListener(view -> changeFilterTypeTransaction(TypeObjectBean.TRANSACTION_EXPENSE));

        btnFilterDateTransaction.setOnClickListener(view -> openDateRangePicker());

        btnReturn.setOnClickListener(view -> finish());

        callQuery();

    }

    private void init() {
        db = new DatabaseHelper(getApplicationContext());
        btnAllTransaction = findViewById(R.id.btnAllTransaction);
        btnIncomeTransaction = findViewById(R.id.btnIncomeTransaction);
        btnExpenseTransaction = findViewById(R.id.btnExpenseTransaction);
        btnFilterDateTransaction = findViewById(R.id.btnFilterDateTransaction);
        textViewAll = findViewById(R.id.textViewAll);
        textViewIncome = findViewById(R.id.textViewIncome);
        textViewExpense = findViewById(R.id.textViewExpense);
        btnReturn = findViewById(R.id.btnReturn);
        imgListEmpty = findViewById(R.id.imgListEmpty);
        listViewCategoriesTransaction = findViewById(R.id.listViewCategoriesTransaction);
    }

    private void changeFilterTypeTransaction(int val) {
        if(val == TypeObjectBean.TRANSACTION_INCOME) {
            filterTransactionBean.setFilterTypeTransaction(val);
            textViewAll.setTextColor(getColor(R.color.secondary_text));
            textViewIncome.setTextColor(getColor(R.color.primary));
            textViewExpense.setTextColor(getColor(R.color.secondary_text));
        }else if(val == TypeObjectBean.TRANSACTION_EXPENSE) {
            filterTransactionBean.setFilterTypeTransaction(val);
            textViewAll.setTextColor(getColor(R.color.secondary_text));
            textViewIncome.setTextColor(getColor(R.color.secondary_text));
            textViewExpense.setTextColor(getColor(R.color.primary));
        }else {
            filterTransactionBean.setFilterTypeTransaction(val);
            textViewAll.setTextColor(getColor(R.color.primary));
            textViewIncome.setTextColor(getColor(R.color.secondary_text));
            textViewExpense.setTextColor(getColor(R.color.secondary_text));
        }
        utility.saveFilterTransactionBean(filterTransactionBean, activity);

        callQuery();
    }

    private void callQuery() {
        FilterTransactionBean bean = utility.getFilterTransactionBeanSaved(activity);
        List<TransactionBean> transactionBeans = db.getAllTransactionBeansByFilterBean(accountBean, bean, listDateFilters.get(0) + " 00:00", listDateFilters.get(1) +  " 23:59");
        db.closeDB();

        if(transactionBeans != null && transactionBeans.size() > 0) {
            imgListEmpty.setVisibility(View.GONE);
            listViewCategoriesTransaction.setVisibility(View.VISIBLE);

            ListTransactionsAdapter listTransactionsAdapter = new ListTransactionsAdapter(transactionBeans, activity, false);
            listViewCategoriesTransaction.setAdapter(listTransactionsAdapter);
            listTransactionsAdapter.notifyDataSetChanged();
        }else {
            imgListEmpty.setVisibility(View.VISIBLE);
            listViewCategoriesTransaction.setVisibility(View.GONE);
        }

    }

    public void openDateRangePicker() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        builder.setCalendarConstraints(constraintsBuilder.build());
        builder.setTitleText(getString(R.string.title_date_range_picker));

        if(selectionDates != null) {
            builder.setSelection(selectionDates);
        }

        MaterialDatePicker<Pair<Long,Long>> picker = builder.build();
        picker.show(getSupportFragmentManager(), picker.toString());

        picker.addOnPositiveButtonClickListener(selection -> {
            long firstDateLong = selection.first;
            Date firstDate = new Date(firstDateLong);
            long endDateLong = selection.second;
            Date endDate = new Date(endDateLong);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            listDateFilters.set(0, dateFormat.format(firstDate));
            listDateFilters.set(1, dateFormat.format(endDate));
            selectionDates = selection;

            picker.dismiss();
            callQuery();
        });

    }

    private void getDateMonthToDatePicker() {
        Calendar cal = Calendar.getInstance();
        listDateFilters = new ArrayList<>();
        Date date;

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        date = cal.getTime();
        Long start = date.getTime();
        listDateFilters.add(dateFormat.format(date));

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        date = cal.getTime();
        Long end = date.getTime();
        listDateFilters.add(dateFormat.format(date));

        selectionDates = new Pair<>(start, end);
    }

}