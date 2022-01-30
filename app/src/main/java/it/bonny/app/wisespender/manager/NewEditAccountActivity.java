package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Date;
import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.IconBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.CurrencyEditText;
import it.bonny.app.wisespender.component.IconListAdapter;
import it.bonny.app.wisespender.util.Utility;

public class NewEditAccountActivity extends AppCompatActivity implements TextWatcher {

    private MaterialCardView returnNewEditAccount;
    private MaterialCardView btnDeleteAccount;
    private GridView gridView;
    private EditText accountName;
    private CurrencyEditText accountOpeningBalance;
    private MaterialButton buttonSaveNewAccount;
    private TextView titleChooseIconNewAccount, titlePageNewAccount;
    private SwitchMaterial flagViewTotalBalance;
    private final Utility utility = new Utility();
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_account);
        init();

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        int iconSelectedPosition;

        AccountBean accountBean = getIntent().getParcelableExtra("accountBean");
        if(accountBean.getId() > 0) {
            //Edit account
            titlePageNewAccount.setText(getString(R.string.title_page_edit_account));

            if(accountBean.getFlagSelected() == TypeObjectBean.NO_SELECTED)
                btnDeleteAccount.setVisibility(View.VISIBLE);

            accountName.setText(accountBean.getName());
            String openingBalance = utility.convertIntInEditTextValue(accountBean.getOpeningBalance()).toString();
            accountOpeningBalance.setText(openingBalance);
            boolean flag = false;
            if(accountBean.getFlagViewTotalBalance() == TypeObjectBean.NO_TOTAL_BALANCE)
                flag = true;
            flagViewTotalBalance.setChecked(flag);
            IconBean iconBean = Utility.getListIconToAccountBean().get(accountBean.getIdIcon());
            iconSelectedPosition = iconBean.getId();
        }else {
            //New account
            accountBean.setIsMaster(TypeObjectBean.NO_MASTER);
            accountBean.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
            accountBean.setFlagSelected(TypeObjectBean.NO_SELECTED);
            accountBean.setIdIcon(-1);
            iconSelectedPosition = -1;
            accountOpeningBalance.setText("0");
        }

        IconListAdapter iconListAdapter = new IconListAdapter(Utility.getListIconToAccountBean(),this);
        gridView.setAdapter(iconListAdapter);
        //Edit account
        if(iconSelectedPosition != -1) {
            iconListAdapter.makeAllUnselect(iconSelectedPosition);
            iconListAdapter.notifyDataSetChanged();
        }

        returnNewEditAccount.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("typeAction", TypeObjectBean.RETURN_NORMAL);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

        gridView.setOnItemClickListener((adapterView, view, position, l) -> {
            iconListAdapter.makeAllUnselect(position);
            iconListAdapter.notifyDataSetChanged();
            accountBean.setIdIcon(Utility.getListIconToAccountBean().get(position).getId());
            titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
        });

        flagViewTotalBalance.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                accountBean.setFlagViewTotalBalance(TypeObjectBean.NO_TOTAL_BALANCE);
            }else {
                accountBean.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
            }
        });

        buttonSaveNewAccount.setOnClickListener(view -> {
            boolean isError = false;
            if("".equals(accountName.getText().toString().trim())) {
                accountName.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                isError = true;
            }else {
                accountBean.setName(accountName.getText().toString().trim());
            }

            if(accountBean.getIdIcon() == -1) {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary));
                isError = true;
            }else {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
            }

            int openingBalance = 0;
            if(accountOpeningBalance.getText() != null &&
                    !"".equals(accountOpeningBalance.getText().toString().trim())) {
                try {
                    openingBalance = utility.convertEditTextValueInInt(accountOpeningBalance.getText().toString().trim());
                }catch (Exception e) {
                    //TODO: Firebase
                }
            }

            accountBean.setOpeningBalance(openingBalance);
            if(!isError) {
                try {
                    Intent intent = new Intent();
                    if(accountBean.getId() != 0) {
                        TransactionBean transactionBean = db.getAllTransactionBeansByTypeTransactionIdAccount(TypeObjectBean.TRANSACTION_OPEN_BALANCE, accountBean.getId());
                        if(transactionBean == null) {
                            CategoryBean categoryBean = db.getCategoryBeanOpeningBalance();
                            transactionBean = new TransactionBean();
                            transactionBean.setTitle(getString(R.string.opening_balance_new_account_input));
                            transactionBean.setAmount(accountBean.getOpeningBalance());
                            transactionBean.setDateInsert(utility.getDateFormat(new Date()));
                            transactionBean.setNote("");
                            transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_OPEN_BALANCE);
                            transactionBean.setIdAccount(accountBean.getId());
                            transactionBean.setIdCategory(categoryBean.getId());
                            db.insertTransactionBean(transactionBean);
                        }else {
                            transactionBean.setAmount(accountBean.getOpeningBalance());
                            db.updateTransactionBean(transactionBean);
                        }
                        db.updateAccountBean(accountBean);
                        intent.putExtra("typeAction", TypeObjectBean.RETURN_EDIT);
                    }else {
                        long idAccount = db.insertAccountBean(accountBean);
                        if(accountBean.getOpeningBalance() > 0 && idAccount > 0) {
                            CategoryBean categoryBean = db.getCategoryBeanOpeningBalance();
                            TransactionBean transactionBean = new TransactionBean();
                            transactionBean.setTitle(getString(R.string.opening_balance_new_account_input));
                            transactionBean.setAmount(accountBean.getOpeningBalance());
                            transactionBean.setDateInsert(utility.getDateFormat(new Date()));
                            transactionBean.setNote("");
                            transactionBean.setTypeTransaction(TypeObjectBean.TRANSACTION_OPEN_BALANCE);
                            transactionBean.setIdAccount(idAccount);
                            transactionBean.setIdCategory(categoryBean.getId());
                            db.insertTransactionBean(transactionBean);
                        }
                        intent.putExtra("typeAction", TypeObjectBean.RETURN_NEW);
                        accountBean.setId(idAccount);
                    }
                    intent.putExtra("accountBean", accountBean);
                    Toast.makeText(getApplicationContext(), getString(R.string.saved_ok), Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }catch (Exception e) {
                    //TODO: Firebase
                    Log.e("bonny", e.getMessage());
                    Toast.makeText(getApplicationContext(), getString(R.string.saved_ko), Toast.LENGTH_SHORT).show();
                }
            }

        });

        btnDeleteAccount.setOnClickListener(view -> getAlertDialogDeleteListAccount(accountBean.getId()));


    }

    public void init() {
        returnNewEditAccount = findViewById(R.id.returnNewEditAccount);
        gridView = findViewById(R.id.gridViewNewAccount);
        accountName = findViewById(R.id.accountName);
        buttonSaveNewAccount = findViewById(R.id.buttonSaveNewAccount);
        titleChooseIconNewAccount = findViewById(R.id.titleChooseIconNewAccount);
        flagViewTotalBalance = findViewById(R.id.flag_view_total_balance);
        accountOpeningBalance = findViewById(R.id.accountOpeningBalance);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        titlePageNewAccount = findViewById(R.id.titlePageNewAccount);

        accountName.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if("".equals(accountName.getText().toString().trim())) {
            accountName.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
        }else {
            accountName.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input));
        }
    }

    private void getAlertDialogDeleteListAccount(final long id){
        final DatabaseHelper db = new DatabaseHelper(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View viewInfoDialog = View.inflate(activity, R.layout.alert_delete, null);
        builder.setCancelable(false);
        builder.setView(viewInfoDialog);
        MaterialButton btnCancel = viewInfoDialog.findViewById(R.id.btnCancel);
        MaterialButton btnDelete = viewInfoDialog.findViewById(R.id.btnDelete);
        TextView textAlert = viewInfoDialog.findViewById(R.id.textAlert);
        textAlert.setText(getString(R.string.alert_delete_account_title));
        final AlertDialog dialog = builder.create();
        if(dialog != null){
            if(dialog.getWindow() != null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getColor(R.color.transparent)));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
        }
        btnCancel.setOnClickListener(v -> {
            if(dialog != null)
                dialog.dismiss();
        });
        btnDelete.setOnClickListener(v -> {
            AccountBean accountBean = db.getAccountBean(id);
            if(accountBean.getFlagSelected() == TypeObjectBean.SELECTED) {
                AccountBean master = db.getAccountBean(1);//Id Master Account
                master.setFlagSelected(TypeObjectBean.SELECTED);
                db.updateAccountBean(master);
            }
            List<TransactionBean> transactionBeanList = db.getAllTransactionBeansByAccount(id);
            if(transactionBeanList != null && transactionBeanList.size() > 0) {
                for(TransactionBean transactionBean: transactionBeanList) {
                    db.deleteTransactionBean(transactionBean.getId());
                }
            }
            boolean resultDelete = db.deleteAccountBean(id);
            if(resultDelete){
                Toast.makeText(this, getString(R.string.delete_ok), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("typeAction", TypeObjectBean.RETURN_DELETE);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }else {
                Toast.makeText(this, getString(R.string.delete_ko), Toast.LENGTH_SHORT).show();
            }
            if(dialog != null)
                dialog.dismiss();
        });
        if(dialog != null) {
            dialog.show();
        }
    }

}