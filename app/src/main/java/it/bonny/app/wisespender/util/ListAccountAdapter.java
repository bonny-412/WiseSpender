package it.bonny.app.wisespender.util;

import android.app.Activity;
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

public class ListAccountAdapter extends RecyclerView.Adapter<ListAccountAdapter.ViewHolder>  {
    private final List<AccountBean> accountBeanList;
    private int selectedPosition = -1;
    private final Activity activity;
    private final DatabaseHelper db;

    public ListAccountAdapter(List<AccountBean> accountBeanList, Activity activity) {
        this.accountBeanList = accountBeanList;
        this.activity = activity;
        this.db = new DatabaseHelper(activity);
        findSelectedTimer();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_list_account, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AccountBean accountBean = accountBeanList.get(position);
        holder.itemListAccountName.setText(accountBean.getName());
        if(accountBeanList.get(position) != null && accountBeanList.get(position).getIdIcon() != null && !"".equals(accountBeanList.get(position).getIdIcon())) {
            try {
                int id_icon = Integer.parseInt(accountBeanList.get(position).getIdIcon());
                holder.itemListAccountIcon.setImageResource(id_icon);
            }catch (Exception e) {
                //TODO: Firebase
            }
        }
        if(position == selectedPosition){
            holder.radioButtonAccount.setVisibility(View.VISIBLE);
            accountBeanList.get(position).setFlagSelected(TypeObjectBean.SELECTED);
        }else {
            holder.radioButtonAccount.setVisibility(View.GONE);
            accountBeanList.get(position).setFlagSelected(TypeObjectBean.NO_SELECTED);
        }
        if(accountBeanList.size() > 0) {
            holder.itemListAccount.setOnClickListener(onStateChangedListener(holder.radioButtonAccount, position));
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
            if (accountBeanList.get(position).getFlagSelected() == TypeObjectBean.NO_SELECTED) {
                notifyItemChanged(selectedPosition);
                accountBeanList.get(selectedPosition).setFlagSelected(TypeObjectBean.NO_SELECTED);
                db.updateAccountBean(accountBeanList.get(selectedPosition));

                notifyItemChanged(position);
                selectedPosition = position;
                accountBeanList.get(position).setFlagSelected(TypeObjectBean.SELECTED);
                radioButtonAccount.setVisibility(View.VISIBLE);
                db.updateAccountBean(accountBeanList.get(position));

                db.closeDB();
                activity.finish();
                activity.startActivity(activity.getIntent());
            }
        };
    }

    private void findSelectedTimer() {
        for(int i = 0; i < accountBeanList.size(); i++){
            AccountBean accountBean = accountBeanList.get(i);
            if(accountBean.getFlagSelected() == TypeObjectBean.SELECTED){
                selectedPosition = i;
            }
        }
    }

}
