package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.FilterTransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.util.Utility;

public class ListTransactionActivity extends AppCompatActivity implements BottomSheetTransaction.BottomSheetListener {

    private LinearLayout btnAllTransaction, btnIncomeTransaction, btnExpenseTransaction, btnFilterDateTransaction;
    private TextView textViewAll, textViewIncome, textViewExpense;
    private int typeTransaction;
    private FilterTransactionBean filterTransactionBean;
    private BottomSheetTransaction bottomSheetTransaction;
    private final Utility utility = new Utility();
    private final Activity activity = this;
    private MaterialCardView btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_transaction);
        filterTransactionBean = utility.getFilterTransactionBeanSaved(activity);
        init();
        changeFilterTypeTransaction(filterTransactionBean.getFilterTypeTransaction());

        btnAllTransaction.setOnClickListener(view -> changeFilterTypeTransaction(3));
        btnIncomeTransaction.setOnClickListener(view -> changeFilterTypeTransaction(TypeObjectBean.TRANSACTION_INCOME));
        btnExpenseTransaction.setOnClickListener(view -> changeFilterTypeTransaction(TypeObjectBean.TRANSACTION_EXPENSE));

        btnFilterDateTransaction.setOnClickListener(view -> bottomSheetTransaction.show(getSupportFragmentManager(), "bottomSheetTransaction"));

        btnReturn.setOnClickListener(view -> finish());

    }

    private void init() {
        btnAllTransaction = findViewById(R.id.btnAllTransaction);
        btnIncomeTransaction = findViewById(R.id.btnIncomeTransaction);
        btnExpenseTransaction = findViewById(R.id.btnExpenseTransaction);
        btnFilterDateTransaction = findViewById(R.id.btnFilterDateTransaction);
        textViewAll = findViewById(R.id.textViewAll);
        textViewIncome = findViewById(R.id.textViewIncome);
        textViewExpense = findViewById(R.id.textViewExpense);
        bottomSheetTransaction = new BottomSheetTransaction(filterTransactionBean.getFilterDate());
        btnReturn = findViewById(R.id.btnReturn);

    }

    @Override
    public void onFilterClick(String filterType) {
        filterTransactionBean.setFilterDate(Integer.parseInt(filterType));
        utility.saveFilterTransactionBean(filterTransactionBean, activity);
        Toast.makeText(getApplicationContext(), "ciaoooo" + " ____ " + filterTransactionBean.getFilterDate(), Toast.LENGTH_SHORT).show();
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
    }


}