package it.bonny.app.wisespender.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.util.Utility;

public class ListAccountSearchFilterAdapter extends RecyclerView.Adapter<ListAccountSearchFilterAdapter.ViewHolder>  {
    private final List<AccountBean> accountBeans;
    private final Utility utility = new Utility();
    private final RecyclerViewClickBottomSheetInterface accountInterface;
    private final List<Long> idAccountSelected;

    public ListAccountSearchFilterAdapter(List<AccountBean> accountBeans, List<Long> idAccountSelected, RecyclerViewClickBottomSheetInterface accountInterface) {
        this.accountBeans = accountBeans;
        this.accountInterface = accountInterface;
        this.idAccountSelected = idAccountSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_bottom_sheet_search_transaction_account, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AccountBean accountBean = accountBeans.get(position);
        holder.textView.setText(accountBean.getName());
        holder.iconAccount.setImageResource(utility.getIdIconByAccountBean(accountBean));

        holder.checkBox.setChecked(idAccountSelected.contains(accountBean.getId()));

        holder.mainLayout.setOnClickListener(view -> {
            holder.checkBox.setChecked(!holder.checkBox.isChecked());
            accountInterface.onItemClick(accountBean.getId(), false);
        });

        holder.checkBox.setOnClickListener(view -> accountInterface.onItemClick(accountBean.getId(), false));

    }

    @Override
    public int getItemCount() {
        return accountBeans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView iconAccount;
        private final ConstraintLayout mainLayout;
        private final MaterialCheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            this.textView = v.findViewById(R.id.textView);
            this.iconAccount = v.findViewById(R.id.iconAccount);
            this.mainLayout = v.findViewById(R.id.mainLayout);
            this.checkBox = v.findViewById(R.id.checkBox);
        }
    }


}
