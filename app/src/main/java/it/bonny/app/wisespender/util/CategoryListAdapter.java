package it.bonny.app.wisespender.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.ViewHolder> {

    private final List<CategoryBean> categoryBeanList;
    private final Activity activity;
    private final Utility utility = new Utility();
    private final RecyclerViewClickCategoryInterface categoryInterface;

    public CategoryListAdapter(List<CategoryBean> categoryBeanList, Activity activity, RecyclerViewClickCategoryInterface categoryInterface) {
        this.activity = activity;
        this.categoryBeanList = categoryBeanList;
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public CategoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_category_list_view, parent, false);
        return new CategoryListAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.ViewHolder holder, int position) {
        final CategoryBean categoryBean = categoryBeanList.get(position);
        try {
            holder.iconCategory.setImageDrawable(AppCompatResources.getDrawable(activity, utility.getIdIconByCategoryBean(categoryBean)));
            String a = categoryBean.getName() + "_" + categoryBean.getId();
            holder.textView.setText(a);
            holder.btnElement.setOnClickListener(view -> categoryInterface.onItemClick(holder.getAdapterPosition()));
        } catch (Exception e) {
            //TODO: Firebase
            Log.e("bonny", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return categoryBeanList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void insertCategoryBeanList(List<CategoryBean> categoryBeanList) {
        this.categoryBeanList.clear();
        this.categoryBeanList.addAll(categoryBeanList);
        notifyDataSetChanged();
    }

    public void insertCategoryBean(CategoryBean categoryBean) {
        this.categoryBeanList.add(0, categoryBean);
        notifyItemInserted(0);

    }

    public void updateCategoryBean(CategoryBean categoryBean, int position) {
        this.categoryBeanList.set(position, categoryBean);
        notifyItemChanged(position);
    }

    public void deleteCategoryBean(int position) {
        this.categoryBeanList.remove(position);
        notifyItemRemoved(position);
    }

    public CategoryBean findCategoryBean(int position) {
        return this.categoryBeanList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView btnElement;
        AppCompatImageView iconCategory;
        AppCompatTextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            btnElement = itemView.findViewById(R.id.btnElement);
            iconCategory = itemView.findViewById(R.id.iconCategory);
            textView = itemView.findViewById(R.id.textView);
        }

    }

}
