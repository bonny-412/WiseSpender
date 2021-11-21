package it.bonny.app.wisespender.manager;

import android.app.Activity;
import android.app.AlertDialog;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.IconBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.IconListAdapter;
import it.bonny.app.wisespender.util.Utility;

public class NewEditCategoryActivity extends AppCompatActivity implements TextWatcher {

    private MaterialCardView returnNewEditCategory, btnDeleteCategory;
    private GridView gridView;
    private EditText categoryName;
    private MaterialButton buttonSaveCategory;
    private TextView titleChooseIconCategory, titlePageCategory;
    private TextView textViewTypeCategory;
    private final Utility utility = new Utility();
    private final Activity activity = this;
    private int typeCategoryInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit_category);
        init();
        CategoryBean categoryBean;
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        int iconSelectedPosition;

        String idCategoryString = getIntent().getStringExtra("idCategory");
        String typeCategory = getIntent().getStringExtra("typeCategory");
        if(typeCategory != null && !"".equals(typeCategory)) {
            typeCategoryInt = Integer.parseInt(typeCategory);
            if(typeCategoryInt == TypeObjectBean.CATEGORY_INCOME)
                textViewTypeCategory.setText(getString(R.string.type_income));
            else
                textViewTypeCategory.setText(getString(R.string.type_expense));
        }

        if(idCategoryString != null && !"".equals(idCategoryString)) {//Sono in modifca
            titlePageCategory.setText(getString(R.string.title_page_edit_category));
            btnDeleteCategory.setVisibility(View.VISIBLE);
            long idCategory = Long.parseLong(idCategoryString);
            categoryBean = db.getCategoryBean(idCategory);
            db.closeDB();
            categoryName.setText(categoryBean.getName());
            IconBean iconBean = Utility.getListIconToCategoryBean().get(categoryBean.getIdIcon());
            iconSelectedPosition = iconBean.getId();
            if(categoryBean.getTypeCategory() == TypeObjectBean.CATEGORY_INCOME)
                textViewTypeCategory.setText(getString(R.string.type_income));
            else
                textViewTypeCategory.setText(getString(R.string.type_expense));
        }else {
            categoryBean = new CategoryBean();
            categoryBean.setIdIcon(-1);
            if(typeCategoryInt == TypeObjectBean.CATEGORY_INCOME)
                categoryBean.setTypeCategory(TypeObjectBean.CATEGORY_INCOME);
            else
                categoryBean.setTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
            iconSelectedPosition = -1;
        }

        IconListAdapter iconListAdapter = new IconListAdapter(Utility.getListIconToCategoryBean(),this);
        gridView.setAdapter(iconListAdapter);
        //Sono in modifca
        if(iconSelectedPosition != -1) {
            iconListAdapter.makeAllUnselect(iconSelectedPosition);
            iconListAdapter.notifyDataSetChanged();
        }

        returnNewEditCategory.setOnClickListener(view -> finish());

        gridView.setOnItemClickListener((adapterView, view, position, l) -> {
            iconListAdapter.makeAllUnselect(position);
            iconListAdapter.notifyDataSetChanged();
            categoryBean.setIdIcon(Utility.getListIconToCategoryBean().get(position).getId());
            titleChooseIconCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
        });

        buttonSaveCategory.setOnClickListener(view -> {
            boolean isError = false;
            if("".equals(categoryName.getText().toString().trim())) {
                categoryName.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
                isError = true;
            }else {
                categoryBean.setName(categoryName.getText().toString().trim());
            }

            if(categoryBean.getIdIcon() == -1) {
                titleChooseIconCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary));
                isError = true;
            }else {
                titleChooseIconCategory.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondary_text));
            }

            if(!isError) {
                try {
                    if(categoryBean.getId() != 0) {
                        db.updateCategoryBean(categoryBean);
                    }else {
                        db.insertCategoryBean(categoryBean);
                    }
                    db.closeDB();
                    Toast.makeText(getApplicationContext(), getString(R.string.saved_ok), Toast.LENGTH_SHORT).show();
                    finish();
                }catch (Exception e) {
                    //TODO: Firebase
                    Log.e("bonny", e.getMessage());
                    Toast.makeText(getApplicationContext(), getString(R.string.saved_ko), Toast.LENGTH_SHORT).show();
                }
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

        categoryName.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if("".equals(categoryName.getText().toString().trim())) {
            categoryName.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input_error));
        }else {
            categoryName.setBackground(AppCompatResources.getDrawable(getApplicationContext(), R.drawable.custom_input));
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
                db.closeDB();
                if(resultDelete){
                    Toast.makeText(this, getString(R.string.delete_ok), Toast.LENGTH_SHORT).show();
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