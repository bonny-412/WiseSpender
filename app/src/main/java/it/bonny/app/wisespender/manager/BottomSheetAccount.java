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

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.Account;
import it.bonny.app.wisespender.util.ListAccountAdapter;

public class BottomSheetAccount extends BottomSheetDialogFragment {
    FragmentActivity activity;
    public BottomSheetAccount(FragmentActivity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_account, container, false);

        MaterialButton buttonNewAccount = view.findViewById(R.id.buttonNewAccount);
        ListView listViewAccount = view.findViewById(R.id.listViewAccount);

        List<Account> accountList = new ArrayList<>();
        testAccountList(accountList);
        ListAccountAdapter listAccountAdapter = new ListAccountAdapter(accountList, activity);
        listViewAccount.setAdapter(listAccountAdapter);

        buttonNewAccount.setOnClickListener(view1 -> {
            Intent intent = new Intent(activity, NewAccountActivity.class);
            view1.getContext().startActivity(intent);
        });

        return view;
    }

    public void testAccountList(List<Account> accountList) {
        Account account = new Account();
        account.setId(1);
        account.setName("Principale");
        account.setChecked(1);
        account.setViewTotalBalance(1);

        Account account1 = new Account();
        account1.setId(2);
        account1.setName("Prova conto 2");
        account1.setChecked(0);
        account1.setViewTotalBalance(0);

        accountList.add(account);
        accountList.add(account1);
    }

}
