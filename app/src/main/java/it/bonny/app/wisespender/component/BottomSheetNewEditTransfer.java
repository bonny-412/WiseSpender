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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;

public class BottomSheetNewEditTransfer extends BottomSheetDialogFragment implements RecyclerViewClickBottomSheetInterface {

    private final Activity mActivity;
    private long idElement;
    private final List<AccountBean> accountBeans;
    private final boolean isTransferIn;

    private BottomSheetNewEditTransactionListener bottomSheetNewEditTransactionListener;

    public BottomSheetNewEditTransfer(Activity activity, long idElement, List<AccountBean> accountBeans, boolean isTransferIn) {
        this.mActivity = activity;
        this.idElement = idElement;
        this.accountBeans = accountBeans;
        this.isTransferIn = isTransferIn;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_new_edit_transaction_category_account, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        TextView titleBottomSheet = view.findViewById(R.id.titleBottomSheet);

        if(isTransferIn) {
            titleBottomSheet.setText("Trasferimento da");
        }else {
            titleBottomSheet.setText("Trasferimento a");
        }

        findAllAccounts(recyclerView, progressBar, mActivity);

        return view;
    }

    private void findAllAccounts(RecyclerView recyclerView, ProgressBar progressBar1, Activity mActivity) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        recyclerView.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        service.execute(() -> {
            ListBSNewEditTransferAdapter adapter = new ListBSNewEditTransferAdapter(accountBeans, idElement, isTransferIn, BottomSheetNewEditTransfer.this);
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
