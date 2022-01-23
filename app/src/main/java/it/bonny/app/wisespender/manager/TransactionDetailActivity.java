package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
            accountTransaction, categoryTransaction, noteTransaction, amountTransaction, timeTransaction;
    private ExtendedFloatingActionButton fabViewPlus, fabViewDelete, fabViewEdit;
    private TransactionBean transactionBean;
    private boolean isOpen;
    private Animation fab_open, fab_close, fab_rotate_antiClock, fab_rotate_clock;
    long idTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);
        init();

        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        idTransaction = getIntent().getLongExtra("idTransaction", 0);

        btnReturn.setOnClickListener(view -> supportFinishAfterTransition());

        fabViewEdit.setOnClickListener(view -> {
            Intent intent = new Intent(TransactionDetailActivity.this, TransactionActivity.class);
            intent.putExtra("idTransaction", idTransaction);
            startActivity(intent);
        });

        fabViewDelete.setOnClickListener(view -> getAlertDialogDeleteListAccount(transactionBean.getId()));

        fabViewPlus.setOnClickListener(v -> {
            if(isOpen){
                closeFloatList();
            }else {
                openFloatList();
            }
        });

    }

    private void setElements() {
        if(transactionBean != null) {
            CategoryBean categoryBean = db.getCategoryBean(transactionBean.getIdCategory());
            AccountBean accountBean = db.getAccountBean(transactionBean.getIdAccount());

            AppCompatImageView iconTypeTransaction = findViewById(R.id.iconTypeTransaction);
            iconTypeTransaction.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(), utility.getIdIconByCategoryBean(categoryBean)));
            titleTransaction.setText(transactionBean.getTitle());
            dateTransaction.setText(utility.getDateToShowInPage(transactionBean.getDateInsert()));
            timeTransaction.setText(utility.getTimeToShowInPage(transactionBean.getDateInsert()));
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

            if(categoryBean.getTypeCategory() == TypeObjectBean.CATEGORY_OPEN_BALANCE) {
                fabViewPlus.setVisibility(View.GONE);
            }
        }
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
        timeTransaction = findViewById(R.id.timeTransaction);

        fabViewPlus = findViewById(R.id.fabViewPlus);
        fabViewEdit = findViewById(R.id.fabViewEdit);
        fabViewDelete = findViewById(R.id.fabViewDelete);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_rotate_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_rotate_antiClock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);
    }

    private void getAlertDialogDeleteListAccount(final long id){
        final DatabaseHelper db = new DatabaseHelper(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInfoDialog = View.inflate(this, R.layout.alert_delete, null);
        builder.setCancelable(false);
        builder.setView(viewInfoDialog);
        MaterialButton btnCancel = viewInfoDialog.findViewById(R.id.btnCancel);
        MaterialButton btnDelete = viewInfoDialog.findViewById(R.id.btnDelete);
        TextView textAlert = viewInfoDialog.findViewById(R.id.textAlert);
        textAlert.setText(getString(R.string.alert_delete_transaction_title));
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
            AccountBean accountBean = db.getAccountBean(transactionBean.getIdAccount());
            if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_INCOME) {
                int totMoneyIncome = accountBean.getTotMoneyIncome() - transactionBean.getAmount();
                accountBean.setTotMoneyIncome(totMoneyIncome);
            }else {
                int totMoneyExpense = accountBean.getTotMoneyExpense() - transactionBean.getAmount();
                accountBean.setTotMoneyExpense(totMoneyExpense);
            }
            boolean resultDelete = db.deleteTransactionBean(id);
            db.updateAccountBean(accountBean);
            if(resultDelete){
                Toast.makeText(this, getString(R.string.delete_ok), Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, getString(R.string.delete_ko), Toast.LENGTH_SHORT).show();
            }
            if(dialog != null)
                dialog.dismiss();
        });
        if(dialog != null)
            dialog.show();
    }

    private void openFloatList(){
        fabViewDelete.setVisibility(View.VISIBLE);
        fabViewEdit.setVisibility(View.VISIBLE);
        fabViewDelete.startAnimation(fab_open);
        fabViewEdit.startAnimation(fab_open);
        fabViewPlus.startAnimation(fab_rotate_clock);
        fabViewDelete.setClickable(true);
        fabViewEdit.setClickable(true);
        isOpen = true;
    }

    private void closeFloatList(){
        fabViewDelete.startAnimation(fab_close);
        fabViewEdit.startAnimation(fab_close);
        fabViewPlus.startAnimation(fab_rotate_antiClock);
        fabViewDelete.setClickable(false);
        fabViewEdit.setClickable(false);
        isOpen = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        transactionBean = db.getTransactionBean(idTransaction);
        if(isOpen)
            closeFloatList();
        setElements();
    }
}