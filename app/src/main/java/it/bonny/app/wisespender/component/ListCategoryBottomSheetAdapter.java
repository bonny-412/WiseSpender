package it.bonny.app.wisespender.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.util.Utility;

public class ListCategoryBottomSheetAdapter extends RecyclerView.Adapter<ListCategoryBottomSheetAdapter.ViewHolder>  {
    private final List<CategoryBean> categoryBeanList;
    private final Utility utility = new Utility();
    private final RecyclerViewClickBottomSheetInterface categoryInterface;
    private final List<Long> idCategorySelected;

    public ListCategoryBottomSheetAdapter(List<CategoryBean> categoryBeanList, List<Long> idCategorySelected, RecyclerViewClickBottomSheetInterface categoryInterface) {
        this.categoryBeanList = categoryBeanList;
        this.categoryInterface = categoryInterface;
        this.idCategorySelected = idCategorySelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_bottom_sheet_search_transaction_category, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CategoryBean categoryBean = categoryBeanList.get(position);
        holder.textView.setText(categoryBean.getName());
        holder.iconCategory.setImageResource(utility.getIdIconByCategoryBean(categoryBean));

        holder.checkBox.setChecked(idCategorySelected.contains(categoryBean.getId()));

        holder.mainLayout.setOnClickListener(view -> {
            holder.checkBox.setChecked(!holder.checkBox.isChecked());
            categoryInterface.onItemClick(categoryBean.getId(), true);
        });

        holder.checkBox.setOnClickListener(view -> categoryInterface.onItemClick(categoryBean.getId(), true));

        if(categoryBean.getTypeCategory() == TypeObjectBean.CATEGORY_INCOME) {
            holder.iconTypeCategory.setImageResource(R.drawable.ic_arrow_up_open_list);
        }else {
            holder.iconTypeCategory.setImageResource(R.drawable.ic_arrow_down_close_list);
        }

    }

    @Override
    public int getItemCount() {
        return categoryBeanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView iconCategory, iconTypeCategory;
        private final ConstraintLayout mainLayout;
        private final MaterialCheckBox checkBox;

        public ViewHolder(View v) {
            super(v);
            this.textView = v.findViewById(R.id.textView);
            this.iconCategory = v.findViewById(R.id.iconCategory);
            this.iconTypeCategory = v.findViewById(R.id.iconTypeCategory);
            this.mainLayout = v.findViewById(R.id.mainLayout);
            this.checkBox = v.findViewById(R.id.checkBox);
        }
    }


}
