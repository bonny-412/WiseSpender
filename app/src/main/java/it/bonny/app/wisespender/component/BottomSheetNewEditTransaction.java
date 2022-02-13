package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.MaterialDatePicker;

import org.w3c.dom.Text;

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
import it.bonny.app.wisespender.bean.FilterTransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;

public class BottomSheetNewEditTransaction extends BottomSheetDialogFragment implements RecyclerViewClickBottomSheetInterface {

    private final Activity mActivity;

    private long idElement;
    private final boolean isCategory;
    private final boolean isExpense;


    private BottomSheetNewEditTransactionListener bottomSheetNewEditTransactionListener;

    private final DatabaseHelper db;

    public BottomSheetNewEditTransaction(Activity activity, boolean isCategory, long idElement, boolean isExpense) {
        this.mActivity = activity;
        this.isCategory = isCategory;
        this.idElement = idElement;
        this.isExpense = isExpense;
        db = new DatabaseHelper(activity.getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_new_edit_transaction_category_account, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView titleBottomSheet = view.findViewById(R.id.titleBottomSheet);

        if(isCategory) {
            titleBottomSheet.setText(getString(R.string.categories));
            findAllCategories(recyclerView, progressBar, mActivity, db);
        }else {
            titleBottomSheet.setText(getString(R.string.accounts));
            findAllAccounts(recyclerView, progressBar, mActivity, db);
        }

        return view;
    }

    private void findAllCategories(RecyclerView recyclerView, ProgressBar progressBar1, Activity mActivity, DatabaseHelper db) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        recyclerView.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        service.execute(() -> {
            List<CategoryBean> categoryBeanList;
            if(isExpense) {
                categoryBeanList = db.getAllCategoryBeansToTypeCategory(TypeObjectBean.CATEGORY_EXPENSE);
            }else {
                categoryBeanList = db.getAllCategoryBeansToTypeCategory(TypeObjectBean.CATEGORY_INCOME);
            }
            ListBSNewEditTransactionAdapter adapter = new ListBSNewEditTransactionAdapter(null, categoryBeanList, idElement, BottomSheetNewEditTransaction.this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
            recyclerView.setAdapter(adapter);

            mActivity.runOnUiThread(() -> {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.GONE);
            });
        });
    }

    private void findAllAccounts(RecyclerView recyclerView, ProgressBar progressBar1, Activity mActivity, DatabaseHelper db) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        recyclerView.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        service.execute(() -> {
            List<AccountBean> accountBeans = db.getAllAccountBeansNoMaster();
            ListBSNewEditTransactionAdapter adapter = new ListBSNewEditTransactionAdapter(accountBeans, null, idElement, BottomSheetNewEditTransaction.this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
            recyclerView.setAdapter(adapter);

            mActivity.runOnUiThread(() -> {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.GONE);
            });
        });
    }

    /**
     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetNewEditTransactionListener = (BottomSheetNewEditTransactionListener) context;
        }catch (ClassCastException e) {
            //TODO: Firebase
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetNewEditTransactionListener");
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onItemClick(long idElement, boolean isCategory) {
        this.idElement = idElement;
        bottomSheetNewEditTransactionListener.onItemClick(idElement, isCategory);
        dismiss();
    }

}
