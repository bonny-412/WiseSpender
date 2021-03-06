package it.bonny.app.wisespender.component;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.util.Utility;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder>  {
    private final List<AccountBean> accountBeanList;
    private final Activity activity;
    private final Utility utility = new Utility();
    private final RecyclerViewClickInterface recyclerViewClickAccountInterface;
    private final String currencyTxt;

    public AccountListAdapter(List<AccountBean> accountBeanList, Activity activity, String currencyTxt, RecyclerViewClickInterface recyclerViewClickAccountInterface) {
        this.accountBeanList = accountBeanList;
        this.activity = activity;
        this.recyclerViewClickAccountInterface = recyclerViewClickAccountInterface;
        this.currencyTxt = currencyTxt;
    }

    @NonNull
    @Override
    public AccountListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_list_accounts, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountListAdapter.ViewHolder holder, int position) {
        final AccountBean accountBean = accountBeanList.get(position);
        try {
            holder.iconAccount.setImageDrawable(AppCompatResources.getDrawable(activity, utility.getIdIconByAccountBean(accountBean)));
            holder.titleAccount.setText(accountBean.getName());
            //String totAccountString = "Gestire" + utility.convertIntInEditTextValue(accountBean.getOpeningBalance() + (accountBean.getTotMoneyIncome() - accountBean.getTotMoneyExpense()));
            //totAccountString = utility.formatNumberCurrency(totAccountString);
            holder.totMoneyAccount.setText("Gestire");
            holder.currency.setText(currencyTxt);

            holder.btnElement.setOnClickListener(view -> recyclerViewClickAccountInterface.onItemClick(holder.getAdapterPosition()));

        } catch (Exception e) {
            //TODO: Firebase
            Log.e("bonny", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return accountBeanList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void insertAccountBeanList(List<AccountBean> accountBeanList) {
        this.accountBeanList.clear();
        this.accountBeanList.addAll(accountBeanList);
        notifyDataSetChanged();
    }

    public void insertAccountBean(AccountBean accountBean) {
        this.accountBeanList.add(accountBeanList.size(), accountBean);
        notifyItemInserted(accountBeanList.size());
    }

    public void updateAccountBean(AccountBean accountBean, int position) {
        this.accountBeanList.set(position, accountBean);
        notifyItemChanged(position);
    }

    public void deleteAccountBean(int position) {
        this.accountBeanList.remove(position);
        notifyItemRemoved(position);
    }

    public AccountBean findAccountBean(int position) {
        return this.accountBeanList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView iconAccount;
        TextView titleAccount, totMoneyAccount, currency;
        MaterialCardView btnElement;
        LinearLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.iconAccount = itemView.findViewById(R.id.iconAccount);
            this.titleAccount = itemView.findViewById(R.id.titleAccount);
            this.totMoneyAccount = itemView.findViewById(R.id.totMoneyAccount);
            this.btnElement = itemView.findViewById(R.id.btnElement);
            this.mainLayout = itemView.findViewById(R.id.mainLayout);
            this.currency = itemView.findViewById(R.id.currency);

            titleAccount.setSelected(true);
            titleAccount.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            titleAccount.setHorizontallyScrolling(true);
            titleAccount.setSingleLine(true);
            titleAccount.setLines(1);

            Animation animation = AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.translate_anim);
            mainLayout.setAnimation(animation);
        }

    }
}
