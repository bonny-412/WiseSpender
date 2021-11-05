package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;

public class ListAccountAdapter extends ArrayAdapter<AccountBean>  {
    private final List<AccountBean> accountBeanList;
    private int selectedPosition = -1;

    public ListAccountAdapter(List<AccountBean> accountBeanList, Activity activity) {
        super(activity, R.layout.item_list_account, accountBeanList);
        this.accountBeanList = accountBeanList;
        findSelectedTimer();
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View rowView, @Nullable ViewGroup parent) {
        try {
            ViewHolder holder;
            if(rowView == null){
                rowView = View.inflate(getContext(), R.layout.item_list_account, null);
                holder = new ViewHolder(rowView);
                rowView.setTag(holder);
            }else
                holder = (ViewHolder) rowView.getTag();

            holder.itemListAccountName.setText(accountBeanList.get(position).getName());

            if(position == selectedPosition){
                holder.radioButtonAccount.setVisibility(View.VISIBLE);
                accountBeanList.get(position).setFlagSelected(TypeObjectBean.SELECTED);
            }else {
                holder.radioButtonAccount.setVisibility(View.GONE);
                accountBeanList.get(position).setFlagSelected(TypeObjectBean.NO_SELECTED);
            }

            if(accountBeanList.size() > 0)
                holder.itemListAccount.setOnClickListener(onStateChangedListener(holder.radioButtonAccount, position));

        }catch (Exception e) {
            //TODO: Firebase
        }
        return rowView != null ? rowView : View.inflate(getContext(), R.layout.item_list_account, null);
    }

    private static class ViewHolder {
        private final TextView itemListAccountName;
        private final ImageView radioButtonAccount;
        private final ConstraintLayout itemListAccount;

        ViewHolder(View v) {
            radioButtonAccount = v.findViewById(R.id.radioButtonAccount);
            itemListAccountName = v.findViewById(R.id.itemListAccountName);
            itemListAccount = v.findViewById(R.id.itemListAccount);
        }
    }

    private View.OnClickListener onStateChangedListener(final ImageView radioButtonAccount, final int position) {
        return v -> {
            if (accountBeanList.get(position).getFlagSelected() == TypeObjectBean.NO_SELECTED) {
                accountBeanList.get(selectedPosition).setFlagSelected(TypeObjectBean.NO_SELECTED);

                selectedPosition = position;
                accountBeanList.get(position).setFlagSelected(TypeObjectBean.SELECTED);
                radioButtonAccount.setVisibility(View.VISIBLE);
            }
            notifyDataSetChanged();
        };
    }

    private void findSelectedTimer(){
        for(int i = 0; i < accountBeanList.size(); i++){
            AccountBean accountBean = accountBeanList.get(i);
            if(accountBean.getFlagSelected() == TypeObjectBean.SELECTED){
                selectedPosition = i;
            }
        }
    }

}
