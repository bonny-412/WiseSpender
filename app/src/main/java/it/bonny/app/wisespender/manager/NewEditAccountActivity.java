package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.CurrencyEditText;
import it.bonny.app.wisespender.util.IconNewEditAccountAdapter;
import it.bonny.app.wisespender.util.Utility;

public class NewEditAccountActivity extends AppCompatActivity implements TextWatcher {

    private AppCompatImageView returnNewAccount, btnDeleteAccount;
    private GridView gridView;
    private EditText accountName;
    private CurrencyEditText accountOpeningBalance;
    private MaterialButton buttonSaveNewAccount;
    private TextView titleChooseIconNewAccount;
    private SwitchMaterial flagViewTotalBalance;
    private final Utility utility = new Utility();
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_account);
        init();
        AccountBean accountBean;
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        int iconSelectedPosition;

        String idAccountString = getIntent().getStringExtra("idAccount");
        if(idAccountString != null && !"".equals(idAccountString)) {//Sono in modifca
            btnDeleteAccount.setVisibility(View.VISIBLE);
            long idAccount = Long.parseLong(idAccountString);
            accountBean = db.getAccountBean(idAccount);
            db.closeDB();
            accountName.setText(accountBean.getName());
            String openingBalance = utility.convertIntInEditTextValue(accountBean.getOpeningBalance()).toString();
            accountOpeningBalance.setText(openingBalance);
            boolean flag = false;
            if(accountBean.getFlagViewTotalBalance() == TypeObjectBean.IS_TOTAL_BALANCE)
                flag = true;
            flagViewTotalBalance.setChecked(flag);
            iconSelectedPosition = Utility.getPositionIconToAccountBean(accountBean.getIdIcon());
        }else {
            accountBean = new AccountBean();
            accountBean.setIsMaster(TypeObjectBean.NO_MASTER);
            accountBean.setFlagViewTotalBalance(TypeObjectBean.NO_TOTAL_BALANCE);
            accountBean.setFlagSelected(TypeObjectBean.NO_SELECTED);
            iconSelectedPosition = -1;
            accountOpeningBalance.setText("0");
        }

        IconNewEditAccountAdapter iconNewEditAccountAdapter = new IconNewEditAccountAdapter(this);
        gridView.setAdapter(iconNewEditAccountAdapter);
        //Sono in modifca
        if(iconSelectedPosition != -1) {
            iconNewEditAccountAdapter.makeAllUnselect(iconSelectedPosition);
            iconNewEditAccountAdapter.notifyDataSetChanged();
        }

        returnNewAccount.setOnClickListener(view -> finish());

        gridView.setOnItemClickListener((adapterView, view, position, l) -> {
            iconNewEditAccountAdapter.makeAllUnselect(position);
            iconNewEditAccountAdapter.notifyDataSetChanged();
            accountBean.setIdIcon(Utility.getListIconToAccountBean().get(position).getName());
            titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
        });

        flagViewTotalBalance.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                accountBean.setFlagViewTotalBalance(TypeObjectBean.IS_TOTAL_BALANCE);
            }else {
                accountBean.setFlagViewTotalBalance(TypeObjectBean.NO_TOTAL_BALANCE);
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

            if(accountBean.getIdIcon() == null || "".equals(accountBean.getIdIcon())) {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary));
                isError = true;
            }else {
                titleChooseIconNewAccount.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
            }

            int openingBalance = 0;
            if(accountOpeningBalance.getText() != null &&
                    !"".equals(accountOpeningBalance.getText().toString().trim())) {
                try {
                    openingBalance = utility.convertEditTextValueInInt(new BigDecimal(accountOpeningBalance.getText().toString()));
                }catch (Exception e) {
                    //TODO: Firebase
                }
            }

            accountBean.setOpeningBalance(openingBalance);
            if(!isError) {
                try {
                    if(accountBean.getId() != 0)
                        db.updateAccountBean(accountBean);
                    else
                        db.insertAccountBean(accountBean);
                    db.closeDB();
                    Toast.makeText(getApplicationContext(), getString(R.string.saved_ok), Toast.LENGTH_LONG).show();
                    finish();
                }catch (Exception e) {
                    //TODO: Firebase
                    Toast.makeText(getApplicationContext(), getString(R.string.saved_ko), Toast.LENGTH_LONG).show();
                }
            }

        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAlertDialogDeleteListAccount(accountBean.getId());
            }
        });


    }

    public void init() {
        returnNewAccount = findViewById(R.id.returnNewAccount);
        gridView = findViewById(R.id.gridViewNewAccount);
        accountName = findViewById(R.id.accountName);
        buttonSaveNewAccount = findViewById(R.id.buttonSaveNewAccount);
        titleChooseIconNewAccount = findViewById(R.id.titleChooseIconNewAccount);
        flagViewTotalBalance = findViewById(R.id.flag_view_total_balance);
        accountOpeningBalance = findViewById(R.id.accountOpeningBalance);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);

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
            boolean resultDelete = db.deleteAccountBean(id);
            //TODO: Cancellare tutte le transazioni collegate al conto
            db.closeDB();
            if(resultDelete){
                Toast.makeText(this, getString(R.string.delete_ok), Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, getString(R.string.delete_ko), Toast.LENGTH_SHORT).show();
            }
            if(dialog != null)
                dialog.dismiss();
        });
        if(dialog != null) {
            //dialog.create();
            dialog.show();
        }
    }

}