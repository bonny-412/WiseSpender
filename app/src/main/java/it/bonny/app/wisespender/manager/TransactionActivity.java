package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.util.CurrencyEditText;

public class TransactionActivity extends AppCompatActivity {

    private LinearLayout btnExpense, btnIncome;
    private TextView dateTransaction, countNameTransaction, countNoteTransaction;
    private final Calendar myCalendar = Calendar.getInstance();
    private final Context context = this;
    private TransactionBean transactionBean;
    private EditText nameTransaction, noteTransaction;
    private CurrencyEditText amountTransaction;
    private ExtendedFloatingActionButton buttonSave;
    private MaterialCardView btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        init();
        updateLabel();

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        btnExpense.setOnClickListener(view -> {
            if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_INCOME) {
                transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_EXPENSE);
                btnExpense.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_change_view_checked_background));
                btnIncome.setBackgroundResource(0);
                btnExpense.setElevation(8);
                btnIncome.setElevation(0);
            }
        });
        btnIncome.setOnClickListener(view -> {
            if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_EXPENSE) {
                transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_INCOME);
                btnIncome.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_change_view_checked_background));
                btnExpense.setBackgroundResource(0);
                btnIncome.setElevation(8);
                btnExpense.setElevation(0);
            }
        });

        dateTransaction.setOnClickListener(view -> new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        amountTransaction.setText("0");

        countNameTransaction.setText(getCountCharacter(nameTransaction, 20));
        countNoteTransaction.setText(getCountCharacter(noteTransaction, 150));

        nameTransaction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countNameTransaction.setText(getCountCharacter(nameTransaction, 20));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        noteTransaction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countNoteTransaction.setText(getCountCharacter(noteTransaction, 150));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnReturn.setOnClickListener(view -> finish());

    }

    private void init() {
        transactionBean = new TransactionBean();
        transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_EXPENSE);
        btnExpense = findViewById(R.id.btnExpense);
        btnIncome = findViewById(R.id.btnIncome);
        dateTransaction = findViewById(R.id.dateTransaction);
        nameTransaction = findViewById(R.id.nameTransaction);
        amountTransaction = findViewById(R.id.amountTransaction);
        buttonSave = findViewById(R.id.buttonSave);
        countNameTransaction = findViewById(R.id.countNameTransaction);
        noteTransaction = findViewById(R.id.noteTransaction);
        countNoteTransaction = findViewById(R.id.countNoteTransaction);
        btnReturn = findViewById(R.id.btnReturn);
    }

    private void updateLabel() {
        transactionBean.setDateInsert(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(myCalendar.getTime()));
        dateTransaction.setText(new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(myCalendar.getTime()));
    }

    private String getCountCharacter(EditText editText, int length) {
        return editText.length() + "/" + length;
    }

}