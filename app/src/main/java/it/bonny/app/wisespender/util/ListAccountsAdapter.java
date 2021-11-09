package it.bonny.app.wisespender.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
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
import it.bonny.app.wisespender.manager.NewAccountActivity;

public class ListAccountsAdapter extends ArrayAdapter<AccountBean>  {
    private final List<AccountBean> accountBeanList;
    private final Activity activity;
    private final Utility utility = new Utility();
    public ListAccountsAdapter(List<AccountBean> accountBeanList, Activity activity) {
        super(activity, R.layout.item_list_account_category,accountBeanList);
        this.accountBeanList = accountBeanList;
        this.activity = activity;
    }

    @Override
    @NonNull
    public View getView(int position, @Nullable View rowView, @Nullable ViewGroup parent) {
        try {
            ViewHolder holder;
            if(rowView == null){
                rowView = View.inflate(getContext(), R.layout.item_list_account_category, null);
                holder = new ViewHolder(rowView);
                rowView.setTag(holder);
            }else {
                holder = (ViewHolder) rowView.getTag();
            }

            holder.iconAccount.setImageDrawable(AppCompatResources.getDrawable(activity, utility.getIdIconByAccountBean(accountBeanList.get(position))));
            holder.titleAccount.setText(accountBeanList.get(position).getName());
            holder.btnDeleteAccount.setOnClickListener(view -> getAlertDialogDeleteListAccount(position, accountBeanList.get(position).getId()));
            holder.btnEditAccount.setOnClickListener(view -> {
                Intent intent = new Intent(activity, NewAccountActivity.class);
                String id = "" + accountBeanList.get(position).getId();
                intent.putExtra("idAccount", id);
                activity.startActivity(intent);
            });

        } catch (Exception e) {
            //TODO: Firebase
        }

        return rowView != null ? rowView : View.inflate(getContext(), R.layout.item_list_account_category, null);
    }

    private static class ViewHolder {
        private final AppCompatImageView iconAccount;
        private final TextView titleAccount;
        private final MaterialCardView btnEditAccount, btnDeleteAccount;

        ViewHolder(View v) {
            iconAccount = v.findViewById(R.id.iconAccount);
            titleAccount = v.findViewById(R.id.titleAccount);
            btnEditAccount = v.findViewById(R.id.btnEditAccount);
            btnDeleteAccount = v.findViewById(R.id.btnDeleteAccount);
        }
    }

    private void getAlertDialogDeleteListAccount(final int position, final long id){
        final DatabaseHelper db = new DatabaseHelper(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View viewInfoDialog = View.inflate(getContext(), R.layout.alert_delete, null);
        builder.setCancelable(false);
        builder.setView(viewInfoDialog);
        TextView btnCancel = viewInfoDialog.findViewById(R.id.btnCancel);
        TextView btnDelete = viewInfoDialog.findViewById(R.id.btnDelete);
        TextView textAlert = viewInfoDialog.findViewById(R.id.textAlert);
        textAlert.setText(activity.getString(R.string.alert_delete_title));
        final AlertDialog dialog = builder.create();
        if(dialog != null){
            if(dialog.getWindow() != null){
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getColor(R.color.transparent)));
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            }
        }
        btnCancel.setOnClickListener(v -> {
            if(dialog != null)
                dialog.dismiss();
        });
        btnDelete.setOnClickListener(v -> {
            AccountBean accountBean = db.getAccountBean(id);
            if(accountBean.getFlagSelected() == TypeObjectBean.SELECTED) {
                AccountBean master = db.getAccountBean(1);//Id Master Account
                master.setFlagSelected(TypeObjectBean.SELECTED);
                db.updateAccountBean(master);
            }
            boolean resultDelete = db.deleteAccountBean(id);
            //TODO: Cancellare tutte le transazioni collegate al conto
            db.closeDB();
            if(resultDelete){
                Toast.makeText(activity, activity.getString(R.string.delete_ok), Toast.LENGTH_SHORT).show();
                accountBeanList.remove(position);
                notifyDataSetChanged();
            }else {
                Toast.makeText(activity, activity.getString(R.string.delete_ko), Toast.LENGTH_SHORT).show();
            }
            if(dialog != null)
                dialog.dismiss();
        });
        if(dialog != null)
            dialog.show();
    }

}
