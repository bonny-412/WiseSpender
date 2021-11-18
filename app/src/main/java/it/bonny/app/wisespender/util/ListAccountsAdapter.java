package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.manager.NewEditAccountActivity;

public class ListAccountsAdapter extends ArrayAdapter<AccountBean>  {
    private final List<AccountBean> accountBeanList;
    private final Activity activity;
    private final Utility utility = new Utility();
    public ListAccountsAdapter(List<AccountBean> accountBeanList, Activity activity) {
        super(activity, R.layout.item_list_accounts,accountBeanList);
        this.accountBeanList = accountBeanList;
        this.activity = activity;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View rowView, @Nullable ViewGroup parent) {
        try {
            ViewHolder holder;
            if(rowView == null){
                rowView = View.inflate(getContext(), R.layout.item_list_accounts, null);
                holder = new ViewHolder(rowView);
                rowView.setTag(holder);
            }else {
                holder = (ViewHolder) rowView.getTag();
            }

            holder.iconAccount.setImageDrawable(AppCompatResources.getDrawable(activity, utility.getIdIconByAccountBean(accountBeanList.get(position))));
            holder.titleAccount.setText(accountBeanList.get(position).getName());
            String totAccountString = "" + utility.convertIntInEditTextValue(accountBeanList.get(position).getTotMoneyIncome() - accountBeanList.get(position).getTotMoneyExpense());
            holder.totMoneyAccount.setText(totAccountString);
           holder.btnElement.setOnClickListener(view -> {
                Intent intent = new Intent(activity, NewEditAccountActivity.class);
                String id = "" + accountBeanList.get(position).getId();
                intent.putExtra("idAccount", id);
                activity.startActivity(intent);
            });

        } catch (Exception e) {
            //TODO: Firebase
        }

        return rowView != null ? rowView : View.inflate(getContext(), R.layout.item_list_accounts, null);
    }

    private static class ViewHolder {
        private final AppCompatImageView iconAccount;
        private final TextView titleAccount, totMoneyAccount;
        private final MaterialCardView btnElement;

        ViewHolder(View v) {
            iconAccount = v.findViewById(R.id.iconAccount);
            titleAccount = v.findViewById(R.id.titleAccount);
            btnElement = v.findViewById(R.id.btnElement);
            totMoneyAccount = v.findViewById(R.id.totMoneyAccount);
        }
    }
}
