package it.bonny.app.wisespender.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.Utility;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder>  {
    private final List<TransactionBean> transactionBeanList;
    private final Activity activity;
    private final Utility utility = new Utility();
    private final boolean isMainActivity;
    private final RecyclerViewClickInterface transactionInterface;
    Animation animation;

    public TransactionListAdapter(List<TransactionBean> transactionBeanList, Activity activity, boolean isMainActivity, RecyclerViewClickInterface transactionInterface) {
        this.transactionBeanList = transactionBeanList;
        this.activity = activity;
        this.isMainActivity = isMainActivity;
        this.transactionInterface = transactionInterface;
    }

    @NonNull
    @Override
    public TransactionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem;
        if(!isMainActivity) {
            listItem = layoutInflater.inflate(R.layout.item_list_transactions_no_main, parent, false);
        }else {
            listItem = layoutInflater.inflate(R.layout.item_list_transactions, parent, false);
        }
        return new TransactionListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionListAdapter.ViewHolder holder, int position) {
        final TransactionBean transactionBean = transactionBeanList.get(position);
        try {
            DatabaseHelper db = new DatabaseHelper(activity.getApplicationContext());
            CategoryBean categoryBean = db.getCategoryBean(transactionBean.getIdCategory());

            holder.iconCategory.setImageDrawable(AppCompatResources.getDrawable(activity, utility.getIdIconByCategoryBean(categoryBean)));
            holder.nameCategory.setText(categoryBean.getName());
            holder.titleTransaction.setText(transactionBean.getTitle());
            holder.dateTransaction.setText(utility.getDateToShowInPage(transactionBean.getDateInsert()));
            String amount;
            if(transactionBean.getTypeTransaction() == TypeObjectBean.TRANSACTION_EXPENSE) {
                amount = "- " + utility.formatNumberCurrency(utility.convertIntInEditTextValue(transactionBean.getAmount()).toString());
                holder.amountTransaction.setTextColor(activity.getColor(R.color.secondary));
            }else {
                amount = "+ " + utility.formatNumberCurrency(utility.convertIntInEditTextValue(transactionBean.getAmount()).toString());
                holder.amountTransaction.setTextColor(activity.getColor(R.color.primary));
            }
            holder.amountTransaction.setText(amount);

            holder.btnElement.setOnClickListener(view -> transactionInterface.onItemClick(holder.getAdapterPosition()));
        } catch (Exception e) {
            //TODO: Firebase
            Log.e("bonny", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return transactionBeanList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void insertTransactionBeanList(List<TransactionBean> transactionBeanList) {
        this.transactionBeanList.clear();
        this.transactionBeanList.addAll(transactionBeanList);
        notifyDataSetChanged();
    }

    public void insertTransactionBean(TransactionBean transactionBean) {
        this.transactionBeanList.add(0, transactionBean);
        notifyItemInserted(0);

    }

    public void updateTransactionBean(TransactionBean transactionBean, int position) {
        this.transactionBeanList.set(position, transactionBean);
        notifyItemChanged(position);
    }

    public void deleteTransactionBean(int position) {
        this.transactionBeanList.remove(position);
        notifyItemRemoved(position);
    }

    public TransactionBean findTransactionBean(int position) {
        return this.transactionBeanList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView iconCategory;
        TextView nameCategory, titleTransaction, amountTransaction, dateTransaction;
        LinearLayout cardViewIcon;
        LinearLayout mainLayout;
        MaterialCardView btnElement;

        public ViewHolder(View v) {
            super(v);
            iconCategory = v.findViewById(R.id.iconCategory);
            nameCategory = v.findViewById(R.id.nameCategory);
            titleTransaction = v.findViewById(R.id.titleTransaction);
            amountTransaction = v.findViewById(R.id.amountTransaction);
            dateTransaction = v.findViewById(R.id.dateTransaction);
            cardViewIcon = v.findViewById(R.id.cardViewIcon);
            mainLayout = v.findViewById(R.id.mainLayout);
            btnElement = v.findViewById(R.id.btnElement);

            animation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.translate_anim);
            mainLayout.setAnimation(animation);
        }
    }
}
