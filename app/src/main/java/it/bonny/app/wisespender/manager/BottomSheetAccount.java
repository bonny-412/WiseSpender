package it.bonny.app.wisespender.manager;

import android.app.Activity;
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
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.component.ListAccountBottomSheetAdapter;

public class BottomSheetAccount extends BottomSheetDialogFragment {
    private final Activity activity;
    private ProgressBar progressBar;
    private RecyclerView listViewAccount;
    private DatabaseHelper db;

    public BottomSheetAccount(Activity activity) {
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
            ListAccountBottomSheetAdapter listAccountBottomSheetAdapter = new ListAccountBottomSheetAdapter(accountBeanList, activity);
            listViewAccount.setHasFixedSize(true);
            listViewAccount.setLayoutManager(new LinearLayoutManager(getContext()));
            listViewAccount.setAdapter(listAccountBottomSheetAdapter);

            activity.runOnUiThread(() -> {
                listViewAccount.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            });
        });
    }

}
