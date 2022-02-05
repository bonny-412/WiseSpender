package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.FilterTransactionBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.component.BottomSheetListener;
import it.bonny.app.wisespender.component.BottomSheetSearchTransaction;
import it.bonny.app.wisespender.component.ListAccountSearchFilterAdapter;
import it.bonny.app.wisespender.component.ListCategoryBottomSheetAdapter;
import it.bonny.app.wisespender.component.RecyclerViewClickBottomSheetInterface;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.component.RecyclerViewClickInterface;
import it.bonny.app.wisespender.component.TransactionListAdapter;
import it.bonny.app.wisespender.util.Utility;

public class ListTransactionActivity extends AppCompatActivity implements BottomSheetListener, RecyclerViewClickInterface, RecyclerViewClickBottomSheetInterface {

    private MaterialButton dateButton, categoryButton, accountButton;
    Button typeButton;
    private FilterTransactionBean filterTransactionBean;
    private final Utility utility = new Utility();
    private final Activity activity = this;
    private MaterialCardView btnReturn;
    private DatabaseHelper db;
    private ImageView listEmpty;
    private RecyclerView listTransactions;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private AccountBean accountBean;
    private TransactionListAdapter transactionListAdapter;
    private List<TransactionBean> transactionBeanList = new ArrayList<>();
    private ProgressBar progressBar;
    private final List<Long> idCategories = new ArrayList<>();
    private final List<Long> idAccounts = new ArrayList<>();
    private List<CategoryBean> categoryBeanList = null;
    private List<AccountBean> accountBeans = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaction);
        filterTransactionBean = utility.getFilterTransactionBeanSaved(activity);

        init();
        accountBean = db.getAccountBeanSelected();

        typeButton.setOnClickListener(view -> {
            BottomSheetSearchTransaction bottomSheetSearchTransaction = new BottomSheetSearchTransaction(TypeObjectBean.SEARCH_TRANSACTION_TYPE, ListTransactionActivity.this, filterTransactionBean);
            bottomSheetSearchTransaction.show(getSupportFragmentManager(), "SEARCH_TRANSACTION");
        });

        dateButton.setOnClickListener(view -> {
            //BottomSheetSearchTransaction bottomSheetSearchTransaction = new BottomSheetSearchTransaction(TypeObjectBean.SEARCH_TRANSACTION_DATE, ListTransactionActivity.this, filterTransactionBean);
            //bottomSheetSearchTransaction.show(getSupportFragmentManager(), "SEARCH_TRANSACTION");
        });

        categoryButton.setOnClickListener(view -> showAlertCategories());

        accountButton.setOnClickListener(view -> showAlertAccounts());

        btnReturn.setOnClickListener(view -> finish());

        //callQuery();
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

            FilterTransactionBean bean = utility.getFilterTransactionBeanSaved(activity);
           // transactionBeanList = db.getAllTransactionBeansByFilterBean(accountBean, bean, listDateFilters.get(0) + " 00:00", listDateFilters.get(1) +  " 23:59", 0);

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

    private void showAlertCategories() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInfoDialog = View.inflate(this, R.layout.bottom_sheet_search_transaction_category, null);
        builder.setCancelable(false);
        builder.setView(viewInfoDialog);
        RecyclerView recyclerViewCategory = viewInfoDialog.findViewById(R.id.recyclerViewCategory);
        ProgressBar progressBar1 = viewInfoDialog.findViewById(R.id.progressBar);

        builder.setTitle(getString(R.string.category_title_detail_transaction));
        builder.setPositiveButton("OK", (dialogInterface, i) -> filterTransactionBean.setIdCategories(idCategories));

        final AlertDialog dialog = builder.create();
        if(dialog != null){
            if(dialog.getWindow() != null){
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
        }

        if(dialog != null)
            dialog.show();

        findAllCategories(recyclerViewCategory, progressBar1, activity, db);
    }

    private void showAlertAccounts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInfoDialog = View.inflate(this, R.layout.bottom_sheet_search_transaction_account, null);
        builder.setCancelable(false);
        builder.setView(viewInfoDialog);
        RecyclerView recyclerViewAccount = viewInfoDialog.findViewById(R.id.recyclerViewAccount);
        ProgressBar progressBar1 = viewInfoDialog.findViewById(R.id.progressBar);

        builder.setTitle(getString(R.string.account_title_detail_transaction));
        builder.setPositiveButton("OK", (dialogInterface, i) -> filterTransactionBean.setIdCategories(idCategories));

        final AlertDialog dialog = builder.create();
        if(dialog != null){
            if(dialog.getWindow() != null){
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
        }

        if(dialog != null)
            dialog.show();

        findAllAccounts(recyclerViewAccount, progressBar1, activity, db);
    }

    private void findAllCategories(RecyclerView recyclerView, ProgressBar progressBar1, Activity mActivity, DatabaseHelper db) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        recyclerView.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        service.execute(() -> {
            if(categoryBeanList == null || categoryBeanList.size() > 0)
                categoryBeanList = db.getAllCategoryBeans();
            ListCategoryBottomSheetAdapter listCategoryBottomSheetAdapter = new ListCategoryBottomSheetAdapter(categoryBeanList, idCategories, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
            recyclerView.setAdapter(listCategoryBottomSheetAdapter);

            mActivity.runOnUiThread(() -> {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.GONE);
            });
        });
    }

    private void findAllAccounts(RecyclerView recyclerView, ProgressBar progressBar1, Activity mActivity, DatabaseHelper db) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        recyclerView.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        service.execute(() -> {
            if(accountBeans == null || accountBeans.size() > 0)
                accountBeans = db.getAllAccountBeansNoMaster();
            ListAccountSearchFilterAdapter listAccountSearchFilterAdapter = new ListAccountSearchFilterAdapter(accountBeans, idAccounts, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
            recyclerView.setAdapter(listAccountSearchFilterAdapter);

            mActivity.runOnUiThread(() -> {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.GONE);
            });
        });
    }

    /**
     *METHOD INTERFACE
     */

    @Override
    public void onItemClick(long idElement, boolean isCategory) {
        if(isCategory) {
            if(idCategories.contains(idElement)) {
                idCategories.remove(idElement);
            }else {
                idCategories.add(idElement);
            }
        }else {
            if(idAccounts.contains(idElement)) {
                idAccounts.remove(idElement);
            }else {
                idAccounts.add(idElement);
            }
        }
    }

    @Override
    public void onFilterClick(FilterTransactionBean filterTransactionBean) {
        this.filterTransactionBean.copyElement(filterTransactionBean);
    }

    @Override
    public void onItemClick(int position) {

    }

}