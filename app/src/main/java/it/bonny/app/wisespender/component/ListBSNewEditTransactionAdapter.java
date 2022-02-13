package it.bonny.app.wisespender.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.util.Utility;

public class ListBSNewEditTransactionAdapter extends RecyclerView.Adapter<ListBSNewEditTransactionAdapter.ViewHolder>  {
    private final long idElementSelected;
    private final Utility utility = new Utility();
    private final RecyclerViewClickBottomSheetInterface accountInterface;
    private final List<AccountBean> accountBeans;
    private final List<CategoryBean> categoryBeans;

    public ListBSNewEditTransactionAdapter(List<AccountBean> accountBeans, List<CategoryBean> categoryBeans, long idElementSelected, RecyclerViewClickBottomSheetInterface accountInterface) {
        this.accountInterface = accountInterface;
        this.idElementSelected = idElementSelected;
        this.accountBeans = accountBeans;
        this.categoryBeans = categoryBeans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_list_account_bottom_sheet, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(accountBeans != null && accountBeans.size() > 0) {
            holder.itemListAccountName.setText(accountBeans.get(position).getName());
            holder.itemListAccountIcon.setImageResource(utility.getIdIconByAccountBean(accountBeans.get(position)));
            holder.radioButtonAccount.setVisibility(View.GONE);
            holder.itemListAccount.setOnClickListener(view -> {
                holder.radioButtonAccount.setVisibility(View.VISIBLE);
                accountInterface.onItemClick(accountBeans.get(position).getId(), false);
            });
            if(accountBeans.get(position).getId() == idElementSelected) {
                holder.radioButtonAccount.setVisibility(View.VISIBLE);
            }
        }else if(categoryBeans != null && categoryBeans.size() > 0) {
            holder.itemListAccountName.setText(categoryBeans.get(position).getName());
            holder.itemListAccountIcon.setImageResource(utility.getIdIconByCategoryBean(categoryBeans.get(position)));
            holder.radioButtonAccount.setVisibility(View.GONE);
            holder.itemListAccount.setOnClickListener(view -> {
                holder.radioButtonAccount.setVisibility(View.VISIBLE);
                accountInterface.onItemClick(categoryBeans.get(position).getId(), true);
            });
            if(categoryBeans.get(position).getId() == idElementSelected) {
                holder.radioButtonAccount.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        int s = 0;
        if(accountBeans != null && accountBeans.size() > 0) {
            s = accountBeans.size();
        }else if(categoryBeans != null && categoryBeans.size() > 0){
            s = categoryBeans.size();
        }
        return s;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final AppCompatImageView itemListAccountIcon;
        private final TextView itemListAccountName;
        private final ImageView radioButtonAccount;
        private final ConstraintLayout itemListAccount;

        public ViewHolder(View v) {
            super(v);
            this.itemListAccountIcon = v.findViewById(R.id.itemListAccountIcon);
            this.itemListAccountName = v.findViewById(R.id.itemListAccountName);
            this.radioButtonAccount = v.findViewById(R.id.radioButtonAccount);
            this.itemListAccount = v.findViewById(R.id.itemListAccount);
        }
    }


}
