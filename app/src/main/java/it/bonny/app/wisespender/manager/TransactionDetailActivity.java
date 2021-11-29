package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.Utility;

public class TransactionDetailActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private final Utility utility = new Utility();
    private MaterialCardView btnReturn;
    private TextView titleTransaction, dateTransaction, typeTransaction,
            accountTransaction, categoryTransaction, noteTransaction, amountTransaction;
    private ExtendedFloatingActionButton btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        init();

        long idTransaction = getIntent().getLongExtra("idTransaction", 0);
        TransactionBean transactionBean = db.getTransactionBean(idTransaction);
        if(transactionBean != null) {
            CategoryBean categoryBean = db.getCategoryBean(transactionBean.getIdCategory());
            AccountBean accountBean = db.getAccountBean(transactionBean.getIdAccount());
            db.closeDB();

            AppCompatImageView iconTypeTransaction = findViewById(R.id.iconTypeTransaction);
            titleTransaction = findViewById(R.id.titleTransaction);
            dateTransaction = findViewById(R.id.dateTransaction);
            typeTransaction = findViewById(R.id.typeTransaction);
            accountTransaction = findViewById(R.id.accountTransaction);
            categoryTransaction = findViewById(R.id.categoryTransaction);
            noteTransaction = findViewById(R.id.noteTransaction);
            amountTransaction = findViewById(R.id.amountTransaction);

            iconTypeTransaction.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByCategoryBean(categoryBean)));
            titleTransaction.setText(transactionBean.getTitle());
            dateTransaction.setText(utility.getDateToShowInPage(transactionBean.getDateInsert(), this));
            String type;
            String amount;
            if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_INCOME) {
                type = getString(R.string.type_income);
                amount = "+ ";
            } else {
                type = getString(R.string.type_expense);
                amount = "- ";
            }

            typeTransaction.setText(type);

            accountTransaction.setText(accountBean.getName());
            categoryTransaction.setText(categoryBean.getName());
            noteTransaction.setText(transactionBean.getNote());
            amount += utility.formatNumberCurrency(utility.convertIntInEditTextValue(transactionBean.getAmount()).toString());
            amountTransaction.setText(amount);

            if(categoryBean.getTypeCategory() == TypeObjectBean.CATEGORY_OPEN_BALANCE)
                btnEdit.setVisibility(View.GONE);

        }

        btnReturn.setOnClickListener(view -> finish());

        btnEdit.setOnClickListener(view -> {
            Intent intent = new Intent(TransactionDetailActivity.this, TransactionActivity.class);
            intent.putExtra("idTransaction", idTransaction);
            startActivity(intent);
        });

    }

    private void init() {
        db = new DatabaseHelper(this);
        btnReturn = findViewById(R.id.btnReturn);
        titleTransaction = findViewById(R.id.titleTransaction);
        dateTransaction = findViewById(R.id.dateTransaction);
        typeTransaction = findViewById(R.id.typeTransaction);
        accountTransaction = findViewById(R.id.accountTransaction);
        categoryTransaction = findViewById( R.id.categoryTransaction);
        noteTransaction = findViewById(R.id.noteTransaction);
        amountTransaction = findViewById(R.id.amountTransaction);
        btnEdit = findViewById(R.id.btnEdit);
    }

}