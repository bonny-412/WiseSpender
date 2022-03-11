package it.bonny.app.wisespender.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.IconBean;
import it.bonny.app.wisespender.bean.SettingsBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.component.IconListAdapter;
import it.bonny.app.wisespender.util.Utility;

public class NewEditCategoryActivity extends AppCompatActivity implements TextWatcher {

    private MaterialCardView btnDeleteCategory;
    private GridView gridView;
    private TextInputLayout categoryName;
    private MaterialButton buttonSaveCategory, returnNewEditCategory;
    private TextView titleChooseIconCategory, titlePageCategory;
    private TextView textViewTypeCategory;
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_category);
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

        CategoryBean categoryBean = getIntent().getParcelableExtra("categoryBean");
        if(categoryBean.getTypeCategory() == TypeObjectBean.CATEGORY_INCOME)
            textViewTypeCategory.setText(getString(R.string.type_income));
        else
            textViewTypeCategory.setText(getString(R.string.type_expense));

        if(categoryBean.getId() > 0) {
            //Edit CategoryBean
            titlePageCategory.setText(getString(R.string.title_page_edit_category));
            btnDeleteCategory.setVisibility(View.VISIBLE);
            Objects.requireNonNull(categoryName.getEditText()).setText(categoryBean.getName());
            IconBean iconBean = Utility.getListIconToCategoryBean().get(categoryBean.getIdIcon());
            iconSelectedPosition = iconBean.getId();
            if(categoryBean.getTypeCategory() == TypeObjectBean.CATEGORY_INCOME)
                textViewTypeCategory.setText(getString(R.string.type_income));
            else
                textViewTypeCategory.setText(getString(R.string.type_expense));
        }else {
            //New CategoryBean
            categoryBean.setIdIcon(-1);
            iconSelectedPosition = -1;
        }

        IconListAdapter iconListAdapter = new IconListAdapter(Utility.getListIconToCategoryBean(),this);
        gridView.setAdapter(iconListAdapter);
        //Edit CategoryBean
        if(iconSelectedPosition != -1) {
            iconListAdapter.makeAllUnselect(iconSelectedPosition);
            iconListAdapter.notifyDataSetChanged();
        }

        returnNewEditCategory.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("typeAction", TypeObjectBean.RETURN_NORMAL);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });

        gridView.setOnItemClickListener((adapterView, view, position, l) -> {
            iconListAdapter.makeAllUnselect(position);
            iconListAdapter.notifyDataSetChanged();
            categoryBean.setIdIcon(Utility.getListIconToCategoryBean().get(position).getId());
            titleChooseIconCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
        });

        buttonSaveCategory.setOnClickListener(view -> {
            boolean isError = false;
            if("".equals(Objects.requireNonNull(categoryName.getEditText()).getText().toString().trim())) {
                categoryName.setError(getText(R.string.required_field));
                isError = true;
            }else {
                categoryBean.setName(categoryName.getEditText().getText().toString().trim());
                categoryName.setError(null);
            }

            if(categoryBean.getIdIcon() == -1) {
                titleChooseIconCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error));
                isError = true;
            }else {
                titleChooseIconCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
            }

            if(!isError) {
                try {
                    Intent intent = new Intent();
                    long id = 0;
                    if(categoryBean.getId() != 0) {
                        db.updateCategoryBean(categoryBean);
                        intent.putExtra("typeAction", TypeObjectBean.RETURN_EDIT);
                    }else {
                        id = db.insertCategoryBean(categoryBean);
                        intent.putExtra("typeAction", TypeObjectBean.RETURN_NEW);
                    }
                    categoryBean.setId(id);
                    intent.putExtra("categoryBean", categoryBean);
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
                buttonSaveCategory.startAnimation(shake);
            }

        });

        btnDeleteCategory.setOnClickListener(view -> getAlertDialogDeleteListAccount(categoryBean.getId()));


    }

    public void init() {
        returnNewEditCategory = findViewById(R.id.returnNewEditCategory);
        gridView = findViewById(R.id.gridView);
        categoryName = findViewById(R.id.categoryName);
        buttonSaveCategory = findViewById(R.id.buttonSaveCategory);
        titleChooseIconCategory = findViewById(R.id.titleChooseIconCategory);
        btnDeleteCategory = findViewById(R.id.btnDeleteCategory);
        titlePageCategory = findViewById(R.id.titlePageCategory);
        textViewTypeCategory = findViewById(R.id.textViewTypeCategory);

        Objects.requireNonNull(categoryName.getEditText()).addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if("".equals(Objects.requireNonNull(categoryName.getEditText()).getText().toString().trim())) {
            categoryName.setError(getText(R.string.required_field));
        }else {
            categoryName.setError(null);
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
        textAlert.setText(getString(R.string.alert_delete_category_title));
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
            CategoryBean categoryBean = db.getCategoryBean(id);
            if(categoryBean != null) {
                List<TransactionBean> transactionBeanList = db.getAllTransactionBeansByCategory(categoryBean.getId());
                if(transactionBeanList != null && transactionBeanList.size() > 0) {
                    for(TransactionBean transactionBean: transactionBeanList) {
                        db.deleteTransactionBean(transactionBean.getId());
                    }
                }
                boolean resultDelete = db.deleteCategoryBean(categoryBean.getId());
                if(resultDelete){
                    Toast.makeText(this, getString(R.string.delete_ok), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("typeAction", TypeObjectBean.RETURN_DELETE);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }else {
                    Toast.makeText(this, getString(R.string.delete_ko), Toast.LENGTH_SHORT).show();
                }
            }
            if(dialog != null)
                dialog.dismiss();
        });
        if(dialog != null)
            dialog.show();
    }

}