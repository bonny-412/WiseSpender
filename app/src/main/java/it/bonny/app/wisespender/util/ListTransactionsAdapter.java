package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.card.MaterialCardView;

import java.lang.reflect.Type;
import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.manager.NewEditAccountActivity;

public class ListTransactionsAdapter extends ArrayAdapter<TransactionBean>  {
    private final List<TransactionBean> transactionBeanList;
    private final Activity activity;
    private final Utility utility = new Utility();
    public ListTransactionsAdapter(List<TransactionBean> transactionBeanList, Activity activity) {
        super(activity, R.layout.item_list_transactions,transactionBeanList);
        this.transactionBeanList = transactionBeanList;
        this.activity = activity;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View rowView, @Nullable ViewGroup parent) {
        try {
            ViewHolder holder;
            if(rowView == null){
                rowView = View.inflate(getContext(), R.layout.item_list_transactions, null);
                holder = new ViewHolder(rowView);
                rowView.setTag(holder);
            }else {
                holder = (ViewHolder) rowView.getTag();
            }
            DatabaseHelper db = new DatabaseHelper(activity.getApplicationContext());
            CategoryBean categoryBean = db.getCategoryBean(transactionBeanList.get(position).getIdCategory());

            holder.iconCategory.setImageDrawable(AppCompatResources.getDrawable(activity, utility.getIdIconByCategoryBean(categoryBean)));
            holder.nameCategory.setText(categoryBean.getName());
            holder.titleTransaction.setText(transactionBeanList.get(position).getTitle());
            holder.dateTransaction.setText(utility.getDateToShowInPage(transactionBeanList.get(position).getDateInsert(), activity));
            String amount;
            if(transactionBeanList.get(position).getTypeTransaction() == TypeObjectBean.TRANSACTION_EXPENSE) {
                amount = "- " + utility.formatNumberCurrency(utility.convertIntInEditTextValue(transactionBeanList.get(position).getAmount()).toString());
                holder.amountTransaction.setTextColor(activity.getColor(R.color.secondary));
            }else {
                amount = "+ " + utility.formatNumberCurrency(utility.convertIntInEditTextValue(transactionBeanList.get(position).getAmount()).toString());
                holder.amountTransaction.setTextColor(activity.getColor(R.color.green));
            }
            holder.amountTransaction.setText(amount);

        } catch (Exception e) {
            //TODO: Firebase
            Log.e("ciaooo", e.toString());
        }

        return rowView != null ? rowView : View.inflate(getContext(), R.layout.item_list_accounts, null);
    }

    private static class ViewHolder {
        private final AppCompatImageView iconCategory;
        private final TextView nameCategory, titleTransaction, amountTransaction, dateTransaction;

        ViewHolder(View v) {
            iconCategory = v.findViewById(R.id.iconCategory);
            nameCategory = v.findViewById(R.id.nameCategory);
            titleTransaction = v.findViewById(R.id.titleTransaction);
            amountTransaction = v.findViewById(R.id.amountTransaction);
            dateTransaction = v.findViewById(R.id.dateTransaction);
        }
    }
}
