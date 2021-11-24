package it.bonny.app.wisespender.manager;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.CurrencyEditText;
import it.bonny.app.wisespender.util.Utility;

public class TransactionActivity extends AppCompatActivity {

    private final Calendar myCalendar = Calendar.getInstance();
    private final Context context = this;
    private final Utility utility = new Utility();
    private int accountSelectedPos, categorySelectedPos = -1;

    private LinearLayout btnExpense, btnIncome;
    private TextView dateTransaction, countNameTransaction, countNoteTransaction;
    private TransactionBean transactionBean;
    private EditText nameTransaction, noteTransaction;
    private CurrencyEditText amountTransaction;
    private ExtendedFloatingActionButton buttonSave;
    private MaterialCardView btnReturn;
    private MaterialCardView btnAccount, btnCategory;
    private AppCompatImageView iconAccount, iconCategory;
    private TextView nameAccount, nameCategory;
    private DatabaseHelper db;
    private LinearLayout containerCategoryInfo, containerCategoryInfo1;

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
                iconCategory.setImageDrawable(null);
                nameCategory.setText("");
                containerCategoryInfo1.setVisibility(View.VISIBLE);
                containerCategoryInfo.setVisibility(View.GONE);
                categorySelectedPos = -1;
                transactionBean.setIdCategory(0);
            }
        });
        btnIncome.setOnClickListener(view -> {
            if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_EXPENSE) {
                transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_INCOME);
                btnIncome.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_change_view_checked_background));
                btnExpense.setBackgroundResource(0);
                btnIncome.setElevation(8);
                btnExpense.setElevation(0);
                iconCategory.setImageDrawable(null);
                nameCategory.setText("");
                containerCategoryInfo1.setVisibility(View.VISIBLE);
                containerCategoryInfo.setVisibility(View.GONE);
                categorySelectedPos = -1;
                transactionBean.setIdCategory(0);
            }
        });

        dateTransaction.setOnClickListener(view -> new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        amountTransaction.setText("0");
        countNameTransaction.setText(getCountCharacter(nameTransaction, 100));
        countNoteTransaction.setText(getCountCharacter(noteTransaction, 300));

        nameTransaction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countNameTransaction.setText(getCountCharacter(nameTransaction, 100));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(nameTransaction.length() > 0) {
                    nameTransaction.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input));
                }else {
                    nameTransaction.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                }
            }
        });

        noteTransaction.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                countNoteTransaction.setText(getCountCharacter(noteTransaction, 300));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(noteTransaction.length() > 0) {
                    noteTransaction.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input));
                }else {
                    noteTransaction.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                }
            }
        });

        btnAccount.setOnClickListener(view -> {
            Intent intent = new Intent(TransactionActivity.this, ListViewActivity.class);
            intent.putExtra("typeList", 0);
            intent.putExtra("selectedPos", accountSelectedPos);
            openSomeActivityForResult(intent);
        });

        btnCategory.setOnClickListener(view -> {
            Intent intent = new Intent(TransactionActivity.this, ListViewActivity.class);
            intent.putExtra("typeList", 1);
            intent.putExtra("selectedPos", categorySelectedPos);
            intent.putExtra("typeCategory", transactionBean.getTypeTransaction());
            openSomeActivityForResult(intent);
        });

        btnReturn.setOnClickListener(view -> finish());

        buttonSave.setOnClickListener(view -> {
            boolean isError = false;
            if(nameTransaction.getText() != null && !"".equals(nameTransaction.getText().toString().trim())) {
                transactionBean.setTitle(nameTransaction.getText().toString().trim());
                nameTransaction.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input));
            }else {
                nameTransaction.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                isError = true;
            }

            if(amountTransaction.getText() != null && !"".equals(amountTransaction.getText().toString().trim())) {
                try {
                    int amount = utility.convertEditTextValueInInt(amountTransaction.getText().toString().trim());
                    if(amount > 0) {
                        transactionBean.setAmount(amount);
                        amountTransaction.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input));
                    }else {
                        amountTransaction.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                        isError = true;
                    }
                }catch (Exception e) {
                    //TODO: Firebase
                }
            }

            if(noteTransaction.getText() != null && !"".equals(noteTransaction.getText().toString().trim())) {
                transactionBean.setNote(noteTransaction.getText().toString());
            }

            if(transactionBean.getIdCategory() == 0) {
                containerCategoryInfo1.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                isError = true;
            }

            if(!isError) {
                String result;
                try {
                    db.insertTransactionBean(transactionBean);
                    AccountBean accountBean = db.getAccountBean(transactionBean.getIdAccount());
                    if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_INCOME) {
                        int totMoneyIncome = accountBean.getTotMoneyIncome() + transactionBean.getAmount();
                        accountBean.setTotMoneyIncome(totMoneyIncome);
                    }else {
                        int totMoneyExpense = accountBean.getTotMoneyExpense() + transactionBean.getAmount();
                        accountBean.setTotMoneyExpense(totMoneyExpense);
                    }
                    db.updateAccountBean(accountBean);
                    db.closeDB();
                    result = getString(R.string.save_transaction_ok);
                }catch (Exception e){
                    //TODO: Firebase
                    result = getString(R.string.save_transaction_ko);
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                if(data != null) {
                    int typeList = data.getIntExtra("typeList", -1);
                    long id = data.getLongExtra("_id", 0);
                    if (typeList == 0) {
                        accountSelectedPos = data.getIntExtra("position", 0);
                        AccountBean accountBean = db.getAccountBean(id);
                        nameAccount.setText(accountBean.getName());
                        iconAccount.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBean)));
                        transactionBean.setIdAccount(accountBean.getId());
                    }else if(typeList == 1) {
                        categorySelectedPos = data.getIntExtra("position", 0);
                        CategoryBean categoryBean = db.getCategoryBean(id);
                        nameCategory.setText(categoryBean.getName());
                        iconCategory.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByCategoryBean(categoryBean)));
                        containerCategoryInfo.setVisibility(View.VISIBLE);
                        containerCategoryInfo1.setVisibility(View.GONE);
                        transactionBean.setIdCategory(categoryBean.getId());
                    }
                }

            }
        }
    });

    private void init() {
        transactionBean = new TransactionBean();
        transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_EXPENSE);
        db = new DatabaseHelper(getApplicationContext());

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
        btnAccount = findViewById(R.id.btnAccount);
        btnCategory = findViewById(R.id.btnCategory);
        nameAccount = findViewById(R.id.nameAccount);
        iconAccount = findViewById(R.id.iconAccount);
        iconCategory = findViewById(R.id.iconCategory);
        nameCategory = findViewById(R.id.nameCategory);
        containerCategoryInfo = findViewById(R.id.containerCategoryInfo);
        containerCategoryInfo1 = findViewById(R.id.containerCategoryInfo1);

        AccountBean accountBeanSelected = db.getAccountBeanSelected();
        if(accountBeanSelected.getIsMaster() == TypeObjectBean.IS_MASTER) {
            accountBeanSelected = db.getFirstAccountBeanNoMaster().get(0);
        }
        iconAccount.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBeanSelected)));
        nameAccount.setText(accountBeanSelected.getName());
        transactionBean.setIdAccount(accountBeanSelected.getId());
    }

    private void updateLabel() {
        transactionBean.setDateInsert(utility.getDateFormat(myCalendar.getTime()));
        dateTransaction.setText(new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(myCalendar.getTime()));
    }

    private String getCountCharacter(EditText editText, int length) {
        return editText.length() + "/" + length;
    }

    public void openSomeActivityForResult(Intent intent) {
        someActivityResultLauncher.launch(intent);
    }

}