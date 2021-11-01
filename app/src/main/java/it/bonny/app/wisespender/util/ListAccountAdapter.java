package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.Account;

public class ListAccountAdapter extends ArrayAdapter<Account>  {
    private final List<Account> accountList;
    private int selectedPosition = -1;

    public ListAccountAdapter(List<Account> accountList, Activity activity) {
        super(activity, R.layout.item_list_account, accountList);
        this.accountList = accountList;
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

            holder.itemListAccountName.setText(accountList.get(position).getName());

            if(position == selectedPosition){
                holder.radioButtonAccount.setVisibility(View.VISIBLE);
                accountList.get(position).setChecked(1);
            }else {
                holder.radioButtonAccount.setVisibility(View.GONE);
                accountList.get(position).setChecked(0);
            }

            if(accountList.size() > 0)
                holder.itemListAccount.setOnClickListener(onStateChangedListener(holder.radioButtonAccount, position));

        }catch (Exception e) {}
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
            if (accountList.get(position).getChecked() == 0) {
                accountList.get(selectedPosition).setChecked(0);

                selectedPosition = position;
                accountList.get(position).setChecked(1);
                radioButtonAccount.setVisibility(View.VISIBLE);
            }
            notifyDataSetChanged();
        };
    }

    private void findSelectedTimer(){
        for(int i = 0; i < accountList.size(); i++){
            Account account = accountList.get(i);
            if(account.getChecked() == 1){
                selectedPosition = i;
            }
        }
    }

}
