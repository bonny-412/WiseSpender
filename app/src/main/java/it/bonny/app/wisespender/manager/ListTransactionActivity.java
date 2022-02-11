package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.FilterTransactionBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.component.BottomSheetFilterSearchListener;
import it.bonny.app.wisespender.component.BottomSheetSearchTransaction;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.component.RecyclerViewClickInterface;
import it.bonny.app.wisespender.component.TransactionListAdapter;

public class ListTransactionActivity extends AppCompatActivity implements BottomSheetFilterSearchListener, RecyclerViewClickInterface {

    private MaterialButton dateButton, categoryButton, accountButton;
    Button typeButton;
    private final FilterTransactionBean filterTransactionBean = new FilterTransactionBean();
    private MaterialButton btnReturn;
    private DatabaseHelper db;
    private ImageView listEmpty;
    private RecyclerView listTransactions;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private TransactionListAdapter transactionListAdapter;
    private List<TransactionBean> transactionBeanList = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaction);

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        filterTransactionBean.setDateFrom(dateFormat.format(calendarStart.getTime()));
        filterTransactionBean.setDateA(dateFormat.format(calendarEnd.getTime()));
        filterTransactionBean.setIdCategories(new ArrayList<>());
        filterTransactionBean.setIdAccounts(new ArrayList<>());

        init();

        typeButton.setOnClickListener(view -> {
            FilterTransactionBean tCopy = new FilterTransactionBean();
            tCopy.copyElement(filterTransactionBean);
            BottomSheetSearchTransaction bottomSheetSearchTransaction = new BottomSheetSearchTransaction(TypeObjectBean.SEARCH_TRANSACTION_TYPE, ListTransactionActivity.this, tCopy);
            bottomSheetSearchTransaction.show(getSupportFragmentManager(), "SEARCH_TRANSACTION");
        });

        dateButton.setOnClickListener(view -> {
            FilterTransactionBean tCopy = new FilterTransactionBean();
            tCopy.copyElement(filterTransactionBean);
            BottomSheetSearchTransaction bottomSheetSearchTransaction = new BottomSheetSearchTransaction(TypeObjectBean.SEARCH_TRANSACTION_DATE, ListTransactionActivity.this, tCopy);
            bottomSheetSearchTransaction.setCancelable(false);
            bottomSheetSearchTransaction.show(getSupportFragmentManager(), "SEARCH_TRANSACTION");
        });

        categoryButton.setOnClickListener(view -> {
            FilterTransactionBean tCopy = new FilterTransactionBean();
            tCopy.copyElement(filterTransactionBean);
            BottomSheetSearchTransaction bottomSheetSearchTransaction = new BottomSheetSearchTransaction(TypeObjectBean.SEARCH_TRANSACTION_CATEGORY, ListTransactionActivity.this, tCopy);
            bottomSheetSearchTransaction.show(getSupportFragmentManager(), "SEARCH_TRANSACTION");
        });

        accountButton.setOnClickListener(view -> {
            FilterTransactionBean tCopy = new FilterTransactionBean();
            tCopy.copyElement(filterTransactionBean);
            BottomSheetSearchTransaction bottomSheetSearchTransaction = new BottomSheetSearchTransaction(TypeObjectBean.SEARCH_TRANSACTION_ACCOUNT, ListTransactionActivity.this, tCopy);
            bottomSheetSearchTransaction.show(getSupportFragmentManager(), "SEARCH_TRANSACTION");
        });

        btnReturn.setOnClickListener(view -> finish());

        callQuery();
        transactionListAdapter = new TransactionListAdapter(transactionBeanList, this, false, this);
        listTransactions.setHasFixedSize(true);
        listTransactions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        listTransactions.setAdapter(transactionListAdapter);

    }

    private void init() {
        db = new DatabaseHelper(getApplicationContext());
        btnReturn = findViewById(R.id.btnReturn);
        listEmpty = findViewById(R.id.listEmpty);
        listTransactions = findViewById(R.id.listTransactions);
        progressBar = findViewById(R.id.progressBar);

        typeButton = findViewById(R.id.type_button);
        dateButton = findViewById(R.id.date_button);
        categoryButton = findViewById(R.id.category_button);
        accountButton = findViewById(R.id.account_button);
    }

    private void callQuery() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        progressBar.setVisibility(View.VISIBLE);
        listTransactions.setVisibility(View.GONE);
        listEmpty.setVisibility(View.GONE);
        executorService.execute(() -> {

            transactionBeanList = db.getAllTransactionBeansByFilterBean(filterTransactionBean, 0);

            runOnUiThread(() -> {
                transactionListAdapter.insertTransactionBeanList(transactionBeanList);

                if(transactionBeanList != null && transactionBeanList.size() > 0) {
                    listTransactions.setVisibility(View.VISIBLE);
                    listEmpty.setVisibility(View.GONE);
                }else {
                    listTransactions.setVisibility(View.GONE);
                    listEmpty.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            });
        });

    }

    /**
     *METHOD INTERFACE
     */

    @Override
    public void onFilterClick(FilterTransactionBean filterTransactionBean) {
        if(this.filterTransactionBean.isChanged(filterTransactionBean)) {
            this.filterTransactionBean.copyElement(filterTransactionBean);
            callQuery();
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ListTransactionActivity.this, TransactionDetailActivity.class);
        TransactionBean transactionBean = transactionListAdapter.findTransactionBean(position);
        intent.putExtra("transactionBean", transactionBean);
        startActivity(intent);
    }

}