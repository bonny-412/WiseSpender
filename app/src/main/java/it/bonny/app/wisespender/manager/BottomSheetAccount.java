package it.bonny.app.wisespender.manager;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.util.ListAccountBottomSheetAdapter;

public class BottomSheetAccount extends BottomSheetDialogFragment {
    private final Activity activity;
    private final List<AccountBean> accountBeanList;

    public BottomSheetAccount(List<AccountBean> accountBeanList, Activity activity) {
        this.accountBeanList = accountBeanList;
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_account, container, false);
        RecyclerView listViewAccount = view.findViewById(R.id.listViewAccount);
        ListAccountBottomSheetAdapter listAccountBottomSheetAdapter = new ListAccountBottomSheetAdapter(accountBeanList, activity);
        listViewAccount.setHasFixedSize(true);
        listViewAccount.setLayoutManager(new LinearLayoutManager(getContext()));
        listViewAccount.setAdapter(listAccountBottomSheetAdapter);
        return view;
    }

}
