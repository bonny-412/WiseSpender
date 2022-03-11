package it.bonny.app.wisespender.manager;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.component.BottomSheetNewEditTransactionListener;
import it.bonny.app.wisespender.component.RecyclerViewClickBottomSheetInterface;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.component.ListAccountBottomSheetAdapter;

public class BottomSheetAccount extends BottomSheetDialogFragment implements RecyclerViewClickBottomSheetInterface  {
    private final Activity activity;
    private ProgressBar progressBar;
    private RecyclerView listViewAccount;
    private DatabaseHelper db;
    private long idAccountSelected;

    private BottomSheetNewEditTransactionListener bottomSheetNewEditTransactionListener;

    public BottomSheetAccount(long idAccountSelected, Activity activity) {
        this.idAccountSelected = idAccountSelected;
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_account, container, false);
        listViewAccount = view.findViewById(R.id.listViewAccount);
        progressBar = view.findViewById(R.id.progressBar);
        db = new DatabaseHelper(activity.getApplicationContext());

        myTask();

        return view;
    }

    private void myTask() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        listViewAccount.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        service.execute(() -> {
            List<AccountBean> accountBeanList = db.getAllAccountBeans();
            ListAccountBottomSheetAdapter listAccountBottomSheetAdapter = new ListAccountBottomSheetAdapter(idAccountSelected, accountBeanList, activity, BottomSheetAccount.this);
            listViewAccount.setHasFixedSize(true);
            listViewAccount.setLayoutManager(new LinearLayoutManager(getContext()));
            listViewAccount.setAdapter(listAccountBottomSheetAdapter);

            activity.runOnUiThread(() -> {
                listViewAccount.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
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
        this.idAccountSelected = idElement;
        bottomSheetNewEditTransactionListener.onItemClick(idElement, false);
        dismiss();
    }
}
