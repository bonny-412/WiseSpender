package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.IconBean;
import it.bonny.app.wisespender.bean.SettingsBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.CurrencyEditText;
import it.bonny.app.wisespender.component.IconListAdapter;
import it.bonny.app.wisespender.util.DecimalDigitsInputFilter;
import it.bonny.app.wisespender.util.Utility;

public class NewEditAccountActivity extends AppCompatActivity implements TextWatcher {

    private MaterialButton returnNewEditAccount;
    private MaterialCardView btnDeleteAccount;
    private GridView gridView;
    private TextInputLayout accountName;
    private TextInputLayout accountOpeningBalance;
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
        Utility utility = new Utility();
        SettingsBean settingsBean = utility.getSettingsBeanSaved(this);
        if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_DARK_MODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_LIGHT_MODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        int iconSelectedPosition;

        AccountBean accountBean = getIntent().getParcelableExtra("accountBean");
        if(accountBean.getId() > 0) {
            //Edit account
            titlePageNewAccount.setText(getString(R.string.title_page_edit_account));

            if(accountBean.getIsSelected() == TypeObjectBean.NO_SELECTED)
                btnDeleteAccount.setVisibility(View.VISIBLE);

            Objects.requireNonNull(accountName.getEditText()).setText(accountBean.getName());
            String openingBalance = utility.convertIntInEditTextValue(accountBean.getOpeningBalance()).toString();
            Objects.requireNonNull(accountOpeningBalance.getEditText()).setText(openingBalance);
            boolean flag = accountBean.getIsIncludedBalance() == TypeObjectBean.IS_INCLUDED_BALANCE;
            flagViewTotalBalance.setChecked(flag);
            IconBean iconBean = Utility.getListIconToAccountBean().get(accountBean.getIdIcon());
            iconSelectedPosition = iconBean.getId();
        }else {
            //New account
            accountBean.setIsMaster(TypeObjectBean.NO_MASTER);
            accountBean.setIsIncludedBalance(TypeObjectBean.IS_INCLUDED_BALANCE);
            accountBean.setIsSelected(TypeObjectBean.NO_SELECTED);
            accountBean.setIdIcon(-1);
            iconSelectedPosition = -1;
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
                accountBean.setIsIncludedBalance(TypeObjectBean.IS_INCLUDED_BALANCE);
            }else {
                accountBean.setIsIncludedBalance(TypeObjectBean.NO_INCLUDED_BALANCE);
            }
        });

        buttonSaveNewAccount.setOnClickListener(view -> {
            boolean isError = false;
            if("".equals(Objects.requireNonNull(accountName.getEditText()).getText().toString().trim())) {
                accountName.setError(getString(R.string.required_field));
                isError = true;
            }else {
                accountBean.setName(accountName.getEditText().getText().toString().trim());
                accountName.setError(null);
            }

            if(accountBean.getIdIcon() == -1) {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
                isError = true;
            }else {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
            }

            int openingBalance = 0;
            if(Objects.requireNonNull(accountOpeningBalance.getEditText()).getText() != null &&
                    !"".equals(accountOpeningBalance.getEditText().getText().toString().trim())) {
                try {
                    openingBalance = utility.convertEditTextValueInInt(accountOpeningBalance.getEditText().getText().toString().trim());
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
            }else {
                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shakeanimation);
                buttonSaveNewAccount.startAnimation(shake);
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

        Objects.requireNonNull(accountName.getEditText()).addTextChangedListener(this);
        Objects.requireNonNull(accountOpeningBalance.getEditText()).setFilters(new InputFilter[]{new DecimalDigitsInputFilter(10, 2)});
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if("".equals(Objects.requireNonNull(accountName.getEditText()).getText().toString().trim())) {
            accountName.setError(getString(R.string.required_field));
        }else {
            accountName.setError(null);
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
            if(accountBean.getIsSelected() == TypeObjectBean.SELECTED) {
                AccountBean master = db.getAccountBean(1);//Id Master Account
                master.setIsSelected(TypeObjectBean.SELECTED);
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