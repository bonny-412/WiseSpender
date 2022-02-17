package it.bonny.app.wisespender.manager;

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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.component.BottomSheetNewEditTransactionListener;
import it.bonny.app.wisespender.component.BottomSheetNewEditTransfer;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.CurrencyEditText;
import it.bonny.app.wisespender.util.Utility;

public class TransferActivity extends AppCompatActivity implements BottomSheetNewEditTransactionListener  {

    private final Calendar myCalendar = Calendar.getInstance();
    private final Context context = this;
    private final Utility utility = new Utility();
    private long idAccountSelectedTransferOut, idAccountSelectedTransferIn;
    private String nameAccountSelectedTransferOut = "", nameAccountSelectedTransferIn = "", dateInsert = "";
    private TextView dateTransfer, nameAccountTransferOut, nameAccountTransferIn;
    private TransactionBean transferIn, transferOut;
    private EditText noteTransfer;
    private CurrencyEditText amountTransfer;
    private MaterialButton buttonSave;
    private MaterialButton btnReturn;
    private DatabaseHelper db;
    private LinearLayout containerDateTransfer;
    private MaterialCardView btnAccountTransferOut, btnAccountTransferIn;
    private List<AccountBean> accountBeanList = new ArrayList<>();
    private AppCompatImageView iconAccountTransferOut, iconAccountTransferIn;
    private boolean isNewTransfer = true;
    private TransactionBean beanApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        init();
        findAllAccounts();

        beanApp = getIntent().getParcelableExtra("transactionBean");

        if(beanApp != null && beanApp.getId() > 0) {
            isNewTransfer = false;
            AccountBean accountTransferOut;
            AccountBean accountTransferIn;
            if(beanApp.getTypeTransaction() == TypeObjectBean.TRANSACTION_TRANSFER_OUT) {
                transferOut = db.getTransactionBean(beanApp.getId());
                transferIn = db.getTransactionBean(beanApp.getIdTransactionTransfer());
            }else {
                transferIn = db.getTransactionBean(beanApp.getId());
                transferOut = db.getTransactionBean(beanApp.getIdTransactionTransfer());
            }
            noteTransfer.setText(transferIn.getNote());
            amountTransfer.setText(String.valueOf(utility.convertIntInEditTextValue(transferIn.getAmount())));

            accountTransferOut = db.getAccountBean(transferOut.getIdAccount());
            nameAccountTransferOut.setText(accountTransferOut.getName());
            iconAccountTransferOut.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountTransferOut)));
            idAccountSelectedTransferOut = accountTransferOut.getId();
            nameAccountSelectedTransferOut = accountTransferOut.getName();

            accountTransferIn = db.getAccountBean(transferIn.getIdAccount());
            nameAccountTransferIn.setText(accountTransferIn.getName());
            iconAccountTransferIn.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountTransferIn)));
            idAccountSelectedTransferIn = accountTransferIn.getId();
            nameAccountSelectedTransferIn = accountTransferIn.getName();

            try {
                myCalendar.setTime(utility.convertStringInDate(transferIn.getDateInsert()));
            } catch (ParseException e) {
                //TODO: Firebase
                e.printStackTrace();
            }
        }else {
            transferOut = new TransactionBean();
            transferIn = new TransactionBean();

            transferOut.setTypeTransaction(TypeObjectBean.TRANSACTION_TRANSFER_OUT);
            transferIn.setTypeTransaction(TypeObjectBean.TRANSACTION_TRANSFER_IN);
            amountTransfer.setText("0");
        }

        updateLabel();

        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        containerDateTransfer.setOnClickListener(view -> new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        btnReturn.setOnClickListener(view -> finish());

        noteTransfer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                if(noteTransfer.length() > 0) {
                    noteTransfer.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input));
                }else {
                    noteTransfer.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                }
            }
        });

        btnAccountTransferOut.setOnClickListener(view -> {
            BottomSheetNewEditTransfer bottomSheet = new BottomSheetNewEditTransfer(TransferActivity.this, idAccountSelectedTransferOut, accountBeanList, true);
            bottomSheet.show(getSupportFragmentManager(), "NEW_EDIT_TRANSACTION");
        });

        btnAccountTransferIn.setOnClickListener(view -> {
            BottomSheetNewEditTransfer bottomSheet = new BottomSheetNewEditTransfer(TransferActivity.this, idAccountSelectedTransferIn, accountBeanList, false);
            bottomSheet.show(getSupportFragmentManager(), "NEW_EDIT_TRANSACTION");
        });

        buttonSave.setOnClickListener(view -> {
            boolean isError = false;
            int amount = 0;
            String noteTransferTxt = "";

            if(amountTransfer.getText() != null && !"".equals(amountTransfer.getText().toString().trim())) {
                try {
                    amount = utility.convertEditTextValueInInt(amountTransfer.getText().toString().trim());
                    if(amount > 0) {
                        amountTransfer.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input));
                    }else {
                        amountTransfer.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                        isError = true;
                    }
                }catch (Exception e) {
                    //TODO: Firebase
                }
            }

            if(noteTransfer.getText() != null && !"".equals(noteTransfer.getText().toString().trim())) {
                noteTransferTxt = noteTransfer.getText().toString();
            }

            if(idAccountSelectedTransferOut == idAccountSelectedTransferIn) {
                isError = true;
                Toast.makeText(getApplicationContext(), R.string.transfer_error_equal_account, Toast.LENGTH_SHORT).show();
            }

            if(!isError) {
                String result;
                try {
                    CategoryBean categoryBean = db.getCategoryBeanTransfer();

                    if(transferIn.getId() > 0 && transferOut.getId() > 0) {
                        transferOut.setTypeTransaction(TypeObjectBean.TRANSACTION_TRANSFER_OUT);
                        transferOut.setDateInsert(dateInsert);
                        transferOut.setTitle(getString(R.string.transfer_to_short) + " '" + nameAccountSelectedTransferIn + "'");
                        transferOut.setAmount(amount);
                        transferOut.setNote(noteTransferTxt);
                        transferOut.setIdCategory(categoryBean.getId()); //Transfer category
                        transferOut.setIdAccount(idAccountSelectedTransferOut);
                        transferOut.setIdTransactionTransfer(transferIn.getId());

                        transferIn.setTypeTransaction(TypeObjectBean.TRANSACTION_TRANSFER_IN);
                        transferIn.setDateInsert(dateInsert);
                        transferIn.setTitle(getString(R.string.transfer_from_short) + " '" + nameAccountSelectedTransferOut + "'");
                        transferIn.setAmount(amount);
                        transferIn.setNote(noteTransferTxt);
                        transferIn.setIdCategory(categoryBean.getId()); //Transfer category
                        transferIn.setIdAccount(idAccountSelectedTransferIn);
                        transferIn.setIdTransactionTransfer(transferOut.getId());

                        db.updateTransactionBean(transferOut);
                        db.updateTransactionBean(transferIn);
                    }else {
                        transferOut.setTypeTransaction(TypeObjectBean.TRANSACTION_TRANSFER_OUT);
                        transferOut.setDateInsert(dateInsert);
                        transferOut.setTitle(getString(R.string.transfer_to_short) + " '" + nameAccountSelectedTransferIn + "'");
                        transferOut.setAmount(amount);
                        transferOut.setNote(noteTransferTxt);
                        transferOut.setIdCategory(categoryBean.getId()); //Transfer category
                        transferOut.setIdAccount(idAccountSelectedTransferOut);

                        long idTransferOut = db.insertTransactionBean(transferOut);
                        transferOut.setId(idTransferOut);

                        transferIn.setTypeTransaction(TypeObjectBean.TRANSACTION_TRANSFER_IN);
                        transferIn.setDateInsert(dateInsert);
                        transferIn.setTitle(getString(R.string.transfer_from_short) + " '" + nameAccountSelectedTransferOut + "'");
                        transferIn.setAmount(amount);
                        transferIn.setNote(noteTransferTxt);
                        transferIn.setIdCategory(categoryBean.getId()); //Transfer category
                        transferIn.setIdAccount(idAccountSelectedTransferIn);
                        transferIn.setIdTransactionTransfer(idTransferOut);

                        long idTransferIn = db.insertTransactionBean(transferIn);
                        transferOut.setIdTransactionTransfer(idTransferIn);
                        db.updateTransactionBean(transferOut);
                    }

                    result = getString(R.string.save_transaction_ok);
                }catch(Exception e) {
                    //TODO: Firebase
                    result = getString(R.string.save_transaction_ko);
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                if(beanApp.getTypeTransaction() == TypeObjectBean.TRANSACTION_TRANSFER_OUT) {
                    intent.putExtra("transactionBean", transferOut);
                }else {
                    intent.putExtra("transactionBean", transferIn);
                }
                setResult(Activity.RESULT_OK, intent);
                finish();
            }else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanimation);
                buttonSave.startAnimation(shake);
            }
        });

    }

    public void init() {
        db = new DatabaseHelper(getApplicationContext());
        dateTransfer = findViewById(R.id.dateTransfer);
        btnReturn = findViewById(R.id.btnReturn);
        buttonSave = findViewById(R.id.buttonSave);
        containerDateTransfer = findViewById(R.id.containerDateTransfer);
        noteTransfer = findViewById(R.id.noteTransfer);
        amountTransfer = findViewById(R.id.amountTransfer);

        btnAccountTransferOut = findViewById(R.id.btnAccountTransferOut);
        nameAccountTransferOut = findViewById(R.id.nameAccountTransferOut);
        iconAccountTransferOut = findViewById(R.id.iconAccountTransferOut);

        btnAccountTransferIn = findViewById(R.id.btnAccountTransferIn);
        nameAccountTransferIn = findViewById(R.id.nameAccountTransferIn);
        iconAccountTransferIn = findViewById(R.id.iconAccountTransferIn);
    }

    private void updateLabel() {
        dateInsert = utility.getDateFormat(myCalendar.getTime());
        dateTransfer.setText(new SimpleDateFormat("EEEE, d MMM", Locale.getDefault()).format(myCalendar.getTime()));
    }

    private void findAllAccounts() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            accountBeanList = db.getAllAccountBeansNoMaster();
            runOnUiThread(() -> {
                if(isNewTransfer) {
                    if(accountBeanList != null && accountBeanList.size() > 1) {
                        idAccountSelectedTransferOut = accountBeanList.get(0).getId();
                        nameAccountSelectedTransferOut = accountBeanList.get(0).getName();
                        nameAccountTransferOut.setText(accountBeanList.get(0).getName());
                        iconAccountTransferOut.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBeanList.get(0))));

                        nameAccountTransferIn.setText(accountBeanList.get(1).getName());
                        idAccountSelectedTransferIn = accountBeanList.get(1).getId();
                        nameAccountSelectedTransferIn = accountBeanList.get(1).getName();
                        iconAccountTransferIn.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBeanList.get(1))));
                    }
                }
            });
        });

    }

    @Override
    public void onItemClick(long idElement, boolean isCategory) {
        //isCategory == true --> transfer_in
        AccountBean accountBean = db.getAccountBean(idElement);
        if(isCategory) {
            idAccountSelectedTransferOut = idElement;
            nameAccountSelectedTransferOut = accountBean.getName();
            nameAccountTransferOut.setText(accountBean.getName());
            iconAccountTransferOut.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBean)));
        }else {
            idAccountSelectedTransferIn = idElement;
            nameAccountSelectedTransferIn = accountBean.getName();
            nameAccountTransferIn.setText(accountBean.getName());
            iconAccountTransferIn.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByAccountBean(accountBean)));
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }

}