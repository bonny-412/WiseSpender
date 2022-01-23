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
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.manager.NewEditAccountActivity;

public class ListCategoriesAdapter extends ArrayAdapter<CategoryBean>  {
    private final List<CategoryBean> categoryBeanList;
    private final Activity activity;
    private final Utility utility = new Utility();
    public ListCategoriesAdapter(List<CategoryBean> categoryBeanList, Activity activity) {
        super(activity, R.layout.item_list_categories,categoryBeanList);
        this.categoryBeanList = categoryBeanList;
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

            //holder.iconAccount.setImageDrawable(AppCompatResources.getDrawable(activity, utility.getIdIconByAccountBean(categoryBeanList.get(position))));
            holder.titleCategory.setText(categoryBeanList.get(position).getName());
            holder.btnDeleteCategory.setOnClickListener(view -> getAlertDialogDeleteListCategory(position, categoryBeanList.get(position).getId()));
            holder.btnEditCategory.setOnClickListener(view -> {
                Intent intent = new Intent(activity, NewEditAccountActivity.class);
                String id = "" + categoryBeanList.get(position).getId();
                intent.putExtra("idCategory", id);
                activity.startActivity(intent);
            });

        } catch (Exception e) {
            //TODO: Firebase
        }

        return rowView != null ? rowView : View.inflate(getContext(), R.layout.item_list_accounts, null);
    }

    private static class ViewHolder {
        private final AppCompatImageView iconCategory;
        private final TextView titleCategory;
        private final MaterialCardView btnEditCategory, btnDeleteCategory;

        ViewHolder(View v) {
            iconCategory = v.findViewById(R.id.iconCategory);
            titleCategory = v.findViewById(R.id.titleCategory);
            btnEditCategory = v.findViewById(R.id.btnEditCategory);
            btnDeleteCategory = v.findViewById(R.id.btnDeleteCategory);
        }
    }

    private void getAlertDialogDeleteListCategory(final int position, final long id){
        final DatabaseHelper db = new DatabaseHelper(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View viewInfoDialog = View.inflate(getContext(), R.layout.alert_delete, null);
        builder.setCancelable(false);
        builder.setView(viewInfoDialog);
        TextView btnCancel = viewInfoDialog.findViewById(R.id.btnCancel);
        TextView btnDelete = viewInfoDialog.findViewById(R.id.btnDelete);
        TextView textAlert = viewInfoDialog.findViewById(R.id.textAlert);
        textAlert.setText(activity.getString(R.string.alert_delete_category_title));
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
            if(resultDelete){
                Toast.makeText(activity, activity.getString(R.string.delete_ok), Toast.LENGTH_SHORT).show();
                categoryBeanList.remove(position);
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
