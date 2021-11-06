package it.bonny.app.wisespender.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.ListAccountAdapter;

public class BottomSheetAccount extends BottomSheetDialogFragment {
    private final FragmentActivity activity;
    private final List<AccountBean> accountBeanList;

    public BottomSheetAccount(List<AccountBean> accountBeanList, FragmentActivity activity) {
        this.accountBeanList = accountBeanList;
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_account, container, false);

        MaterialButton buttonNewAccount = view.findViewById(R.id.buttonNewAccount);
        RecyclerView listViewAccount = view.findViewById(R.id.listViewAccount);

        ListAccountAdapter listAccountAdapter = new ListAccountAdapter(accountBeanList, activity);
        listViewAccount.setHasFixedSize(true);
        listViewAccount.setLayoutManager(new LinearLayoutManager(getContext()));
        listViewAccount.setAdapter(listAccountAdapter);

        buttonNewAccount.setOnClickListener(view1 -> {
            Intent intent = new Intent(activity, NewAccountActivity.class);
            view1.getContext().startActivity(intent);
        });

        return view;
    }

}
