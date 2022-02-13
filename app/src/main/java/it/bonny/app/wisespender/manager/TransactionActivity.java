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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.component.BottomSheetNewEditTransaction;
import it.bonny.app.wisespender.component.BottomSheetNewEditTransactionListener;
import it.bonny.app.wisespender.component.BottomSheetSearchTransaction;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.CurrencyEditText;
import it.bonny.app.wisespender.util.Utility;

public class TransactionActivity extends AppCompatActivity implements BottomSheetNewEditTransactionListener  {

    private final Calendar myCalendar = Calendar.getInstance();
    private final Context context = this;
    private final Utility utility = new Utility();
    private int oldAmount = 0;
    private long idAccountSelected, idCategorySelected;
    private boolean isExpense = true;

    private LinearLayout btnExpense, btnIncome;
    private TextView dateTransaction, countNameTransaction, countNoteTransaction;
    private TransactionBean transactionBean;
    private EditText nameTransaction, noteTransaction;
    private CurrencyEditText amountTransaction;
    private MaterialButton buttonSave;
    private MaterialButton btnReturn;
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

        transactionBean = getIntent().getParcelableExtra("transactionBean");
        AccountBean accountBeanSelected;
        if(transactionBean.getId() > 0) {
            CategoryBean categoryBean = db.getCategoryBean(transactionBean.getIdCategory());
            accountBeanSelected = db.getAccountBean(transactionBean.getIdAccount());
            oldAmount = transactionBean.getAmount();
            idCategorySelected = categoryBean.getId();

            iconAccount.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBeanSelected)));
            nameAccount.setText(accountBeanSelected.getName());
            changeTypeTransaction(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_EXPENSE, true);
            nameTransaction.setText(transactionBean.getTitle());
            amountTransaction.setText(String.valueOf(utility.convertIntInEditTextValue(transactionBean.getAmount())));
            noteTransaction.setText(transactionBean.getNote());
            countNameTransaction.setText(getCountCharacter(nameTransaction, 100));
            countNoteTransaction.setText(getCountCharacter(noteTransaction, 300));
            nameAccount.setText(accountBeanSelected.getName());
            iconAccount.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBeanSelected)));

            nameCategory.setText(categoryBean.getName());
            iconCategory.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByCategoryBean(categoryBean)));
            containerCategoryInfo.setVisibility(View.VISIBLE);
            containerCategoryInfo1.setVisibility(View.GONE);

            try {
                myCalendar.setTime(utility.convertStringInDate(transactionBean.getDateInsert()));
            } catch (ParseException e) {
                //TODO: Firebase
                e.printStackTrace();
            }
        }else {
            transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_EXPENSE);
            accountBeanSelected = db.getAccountBeanSelected();
            if(accountBeanSelected.getIsMaster() == TypeObjectBean.IS_MASTER) {
                accountBeanSelected = db.getFirstAccountBeanNoMaster().get(0);
            }
            iconAccount.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBeanSelected)));
            nameAccount.setText(accountBeanSelected.getName());
            transactionBean.setIdAccount(accountBeanSelected.getId());
            amountTransaction.setText("0");
            countNameTransaction.setText(getCountCharacter(nameTransaction, 100));
            countNoteTransaction.setText(getCountCharacter(noteTransaction, 300));
        }

        idAccountSelected = accountBeanSelected.getId();

        updateLabel();

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        btnExpense.setOnClickListener(view -> {
            if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_INCOME) {
                changeTypeTransaction(true, false);
                idCategorySelected = 0;
            }
        });
        btnIncome.setOnClickListener(view -> {
            if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_EXPENSE) {
                changeTypeTransaction(false, false);
                idCategorySelected = 0;
            }
        });

        dateTransaction.setOnClickListener(view -> new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

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
            BottomSheetNewEditTransaction bottomSheet = new BottomSheetNewEditTransaction(TransactionActivity.this, false, idAccountSelected, isExpense);
            bottomSheet.show(getSupportFragmentManager(), "NEW_EDIT_TRANSACTION");
        });

        btnCategory.setOnClickListener(view -> {
            BottomSheetNewEditTransaction bottomSheet = new BottomSheetNewEditTransaction(TransactionActivity.this, true, idCategorySelected, isExpense);
            bottomSheet.show(getSupportFragmentManager(), "NEW_EDIT_TRANSACTION");
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
                    AccountBean accountBean = db.getAccountBean(transactionBean.getIdAccount());
                    if(transactionBean.getId() > 0) {
                        db.updateTransactionBean(transactionBean);
                        if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_INCOME) {
                            int totMoneyIncome = (accountBean.getTotMoneyIncome() - oldAmount) + transactionBean.getAmount();
                            accountBean.setTotMoneyIncome(totMoneyIncome);
                        }else {
                            int totMoneyExpense = (accountBean.getTotMoneyExpense() - oldAmount) + transactionBean.getAmount();
                            accountBean.setTotMoneyExpense(totMoneyExpense);
                        }
                    }else {
                        db.insertTransactionBean(transactionBean);
                        if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_INCOME) {
                            int totMoneyIncome = accountBean.getTotMoneyIncome() + transactionBean.getAmount();
                            accountBean.setTotMoneyIncome(totMoneyIncome);
                        }else {
                            int totMoneyExpense = accountBean.getTotMoneyExpense() + transactionBean.getAmount();
                            accountBean.setTotMoneyExpense(totMoneyExpense);
                        }
                    }
                    db.updateAccountBean(accountBean);
                    result = getString(R.string.save_transaction_ok);
                }catch (Exception e){
                    //TODO: Firebase
                    result = getString(R.string.save_transaction_ko);
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("transactionBean", transactionBean);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }

        });
    }


    private void init() {
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
    }

    private void updateLabel() {
        transactionBean.setDateInsert(utility.getDateFormat(myCalendar.getTime()));
        dateTransaction.setText(new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(myCalendar.getTime()));
    }

    private String getCountCharacter(EditText editText, int length) {
        return editText.length() + "/" + length;
    }

    private void changeTypeTransaction(boolean isExpense, boolean isFirstLaunch) {
        if(isExpense) {
            transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_EXPENSE);
            btnExpense.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_change_view_checked_background));
            btnIncome.setBackgroundResource(0);
            btnExpense.setElevation(8);
            btnIncome.setElevation(0);
            this.isExpense = true;
        }else {
            transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_INCOME);
            btnIncome.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.button_change_view_checked_background));
            btnExpense.setBackgroundResource(0);
            btnIncome.setElevation(8);
            btnExpense.setElevation(0);
            this.isExpense = false;
        }
        if(!isFirstLaunch) {
            iconCategory.setImageDrawable(null);
            nameCategory.setText("");
            containerCategoryInfo1.setVisibility(View.VISIBLE);
            containerCategoryInfo.setVisibility(View.GONE);
            transactionBean.setIdCategory(0);
        }
    }

    @Override
    public void onItemClick(long idElement, boolean isCategory) {
        if (!isCategory) {
            AccountBean accountBean = db.getAccountBean(idElement);
            nameAccount.setText(accountBean.getName());
            iconAccount.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBean)));
            transactionBean.setIdAccount(accountBean.getId());
            idAccountSelected = idElement;
        }else {
            CategoryBean categoryBean = db.getCategoryBean(idElement);
            nameCategory.setText(categoryBean.getName());
            iconCategory.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByCategoryBean(categoryBean)));
            containerCategoryInfo.setVisibility(View.VISIBLE);
            containerCategoryInfo1.setVisibility(View.GONE);
            transactionBean.setIdCategory(categoryBean.getId());
            idCategorySelected = idElement;
        }
    }
}