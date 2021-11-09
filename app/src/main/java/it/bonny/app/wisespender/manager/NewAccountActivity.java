package it.bonny.app.wisespender.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.IconNewAccountAdapter;
import it.bonny.app.wisespender.util.Utility;

public class NewAccountActivity extends AppCompatActivity implements TextWatcher {

    private MaterialButton returnNewAccount;
    private GridView gridView;
    private EditText accountName, accountOpeningBalance;
    private MaterialButton buttonSaveNewAccount;
    private TextView titleChooseIconNewAccount;
    private SwitchMaterial flagViewTotalBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        init();
        AccountBean accountBean;
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        int iconSelectedPosition;

        String idAccountString = getIntent().getStringExtra("idAccount");
        if(idAccountString != null && !"".equals(idAccountString)) {//Sono in modifca
            long idAccount = Long.parseLong(idAccountString);
            accountBean = db.getAccountBean(idAccount);
            db.closeDB();
            accountName.setText(accountBean.getName());
            //accountOpeningBalance.setText(accountBean.getOpeningBalance());
            boolean flag = false;
            if(accountBean.getFlagViewTotalBalance() == TypeObjectBean.IS_TOTAL_BALANCE)
                flag = true;
            flagViewTotalBalance.setChecked(flag);
            iconSelectedPosition = Utility.getPositionIconToNewAccount(accountBean.getIdIcon());
        }else {
            accountBean = new AccountBean();
            accountBean.setIsMaster(TypeObjectBean.NO_MASTER);
            accountBean.setFlagViewTotalBalance(TypeObjectBean.NO_TOTAL_BALANCE);
            accountBean.setFlagSelected(TypeObjectBean.NO_SELECTED);
            iconSelectedPosition = -1;
        }

        IconNewAccountAdapter iconNewAccountAdapter = new IconNewAccountAdapter(this);
        gridView.setAdapter(iconNewAccountAdapter);
        //Sono in modifca
        if(iconSelectedPosition != -1) {
            iconNewAccountAdapter.makeAllUnselect(iconSelectedPosition);
            iconNewAccountAdapter.notifyDataSetChanged();
        }

        returnNewAccount.setOnClickListener(view -> finish());

        gridView.setOnItemClickListener((adapterView, view, position, l) -> {
            iconNewAccountAdapter.makeAllUnselect(position);
            iconNewAccountAdapter.notifyDataSetChanged();
            accountBean.setIdIcon(Utility.getListIconToNewAccount().get(position).getName());
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
            if(!"".equals(accountOpeningBalance.getText().toString().trim())) {
                try {
                    openingBalance = Integer.parseInt(accountOpeningBalance.getText().toString().trim());
                }catch (Exception e) {
                    //TODO: Firebase
                }
            }
            accountBean.setOpeningBalance(openingBalance);

            if(!isError) {
                try {
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


    }

    public void init() {
        returnNewAccount = findViewById(R.id.returnNewAccount);
        gridView = findViewById(R.id.gridViewNewAccount);
        accountName = findViewById(R.id.accountName);
        buttonSaveNewAccount = findViewById(R.id.buttonSaveNewAccount);
        titleChooseIconNewAccount = findViewById(R.id.titleChooseIconNewAccount);
        flagViewTotalBalance = findViewById(R.id.flag_view_total_balance);
        accountOpeningBalance = findViewById(R.id.accountOpeningBalance);

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
}