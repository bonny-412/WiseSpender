package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.Utility;

public class ListAccountBottomSheetAdapter extends RecyclerView.Adapter<ListAccountBottomSheetAdapter.ViewHolder>  {
    private final List<AccountBean> accountBeanList;
    private int selectedPosition = -1;
    private final Activity activity;
    private final DatabaseHelper db;
    private final Utility utility = new Utility();
    private long idElementSelected;
    private final RecyclerViewClickBottomSheetInterface accountInterface;

    public ListAccountBottomSheetAdapter(long idElementSelected, List<AccountBean> accountBeanList, Activity activity, RecyclerViewClickBottomSheetInterface accountInterface) {
        this.accountBeanList = accountBeanList;
        this.idElementSelected = idElementSelected;
        this.activity = activity;
        this.db = new DatabaseHelper(activity);
        this.accountInterface = accountInterface;
        findSelectedTimer();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_list_account_bottom_sheet, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AccountBean accountBean = accountBeanList.get(position);
        holder.itemListAccountName.setText(accountBean.getName());
        if(accountBeanList.get(position) != null) {
            try {
                holder.itemListAccountIcon.setImageResource(utility.getIdIconByAccountBean(accountBean));
            }catch (Exception e) {
                //TODO: Firebase
            }
        }

        if(idElementSelected == accountBean.getId()) {
            holder.radioButtonAccount.setVisibility(View.VISIBLE);
            accountBean.setIsSelected(TypeObjectBean.SELECTED);
        }else {
            holder.radioButtonAccount.setVisibility(View.GONE);
            accountBean.setIsSelected(TypeObjectBean.NO_SELECTED);
        }

        if(accountBeanList.size() > 0) {
            holder.itemListAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    accountBeanList.get(selectedPosition).setIsSelected(TypeObjectBean.NO_SELECTED);
                    db.updateAccountBean(accountBeanList.get(selectedPosition));

                    selectedPosition = holder.getAdapterPosition();
                    accountBeanList.get(holder.getAdapterPosition()).setIsSelected(TypeObjectBean.SELECTED);
                    db.updateAccountBean(accountBeanList.get(holder.getAdapterPosition()));

                    accountInterface.onItemClick(accountBean.getId(), false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return accountBeanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemListAccountName;
        private final ImageView radioButtonAccount;
        private final ConstraintLayout itemListAccount;
        private final AppCompatImageView itemListAccountIcon;

        public ViewHolder(View v) {
            super(v);
            this.radioButtonAccount = v.findViewById(R.id.radioButtonAccount);
            this.itemListAccountName = v.findViewById(R.id.itemListAccountName);
            this.itemListAccount = v.findViewById(R.id.itemListAccount);
            this.itemListAccountIcon = v.findViewById(R.id.itemListAccountIcon);
        }
    }

    private View.OnClickListener onStateChangedListener(final ImageView radioButtonAccount, final int position) {
        return v -> {
            if (accountBeanList.get(position).getIsSelected() == TypeObjectBean.NO_SELECTED) {
                notifyItemChanged(selectedPosition);
                accountBeanList.get(selectedPosition).setIsSelected(TypeObjectBean.NO_SELECTED);
                db.updateAccountBean(accountBeanList.get(selectedPosition));

                notifyItemChanged(position);
                selectedPosition = position;
                accountBeanList.get(position).setIsSelected(TypeObjectBean.SELECTED);
                radioButtonAccount.setVisibility(View.VISIBLE);
                db.updateAccountBean(accountBeanList.get(position));
            }
        };
    }

    private void findSelectedTimer() {
        for(int i = 0; i < accountBeanList.size(); i++){
            AccountBean accountBean = accountBeanList.get(i);
            if(accountBean.getIsSelected() == TypeObjectBean.SELECTED){
                selectedPosition = i;
            }
        }
    }

}
