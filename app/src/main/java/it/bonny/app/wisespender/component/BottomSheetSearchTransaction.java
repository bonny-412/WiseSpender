package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.FilterTransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;

public class BottomSheetSearchTransaction extends BottomSheetDialogFragment {

    private final int type;
    private final Activity mActivity;
    private View view;

    private ConstraintLayout containerIncome, containerExpense;
    private MaterialCheckBox checkBoxIncome, checkBoxExpense;

    private MaterialButton btnOk, btnCancel;

    private BottomSheetListener bottomSheetListener;
    private final FilterTransactionBean filterTransactionBean;

    public BottomSheetSearchTransaction(int type, Activity activity, FilterTransactionBean filterTransactionBean) {
        this.type = type;
        this.mActivity = activity;
        this.filterTransactionBean = filterTransactionBean;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        if(type == TypeObjectBean.SEARCH_TRANSACTION_TYPE) {
            view = inflater.inflate(R.layout.bottom_sheet_search_transaction_type, container, false);
            filterTransactionBean.setTypeBottomSheet(TypeObjectBean.SEARCH_TRANSACTION_TYPE);
            initTransactionType();

            containerIncome.setOnClickListener(view -> {
                if(checkBoxIncome.isChecked()) {
                    if(checkBoxExpense.isChecked())
                        checkBoxIncome.setChecked(false);
                }else {
                    checkBoxIncome.setChecked(true);
                }
            });

            containerExpense.setOnClickListener(view -> {
                if(checkBoxExpense.isChecked()) {
                    if(checkBoxIncome.isChecked())
                        checkBoxExpense.setChecked(false);
                }else {
                    checkBoxExpense.setChecked(true);
                }
            });

            checkBoxIncome.setOnClickListener(view -> {
                if(checkBoxIncome.isChecked()) {
                    if(checkBoxExpense.isChecked())
                        checkBoxIncome.setChecked(false);
                }else {
                    checkBoxIncome.setChecked(true);
                }
            });

            checkBoxExpense.setOnClickListener(view -> {
                if(checkBoxExpense.isChecked()) {
                    if(checkBoxIncome.isChecked())
                        checkBoxExpense.setChecked(false);
                }else {
                    checkBoxExpense.setChecked(true);
                }
            });

            btnCancel.setOnClickListener(view -> dismiss());

            btnOk.setOnClickListener(view -> {
                if((checkBoxIncome.isChecked() && checkBoxExpense.isChecked()) || (!checkBoxIncome.isChecked() && !checkBoxExpense.isChecked())) {
                    filterTransactionBean.setTypeFilter(TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_ALL);
                }else if(checkBoxIncome.isChecked()) {
                    filterTransactionBean.setTypeFilter((TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_INCOME));
                }else if(checkBoxExpense.isChecked()) {
                    filterTransactionBean.setTypeFilter((TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_EXPENSE));
                }
                closeBottomSheet();
            });
        }

        return view;
    }

    private void closeBottomSheet() {
        bottomSheetListener.onFilterClick(filterTransactionBean);
        dismiss();
    }

    /*@Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetListener = (BottomSheetListener) context;
        }catch (ClassCastException e) {
            //TODO: Firebase
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }*/

    private void initTransactionType() {
        containerIncome = view.findViewById(R.id.containerIncome);
        containerExpense = view.findViewById(R.id.containerExpense);
        checkBoxIncome = view.findViewById(R.id.checkBoxIncome);
        checkBoxExpense = view.findViewById(R.id.checkBoxExpense);
        btnOk = view.findViewById(R.id.btnOk);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

}
