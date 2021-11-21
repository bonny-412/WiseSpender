package it.bonny.app.wisespender.util;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.manager.NewEditCategoryActivity;

public class CategoryGridViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<CategoryBean> categoryBeanList;
    private final Utility utility = new Utility();

    public CategoryGridViewAdapter(List<CategoryBean> categoryBeanList, Context context) {
        this.mContext = context;
        this.categoryBeanList = categoryBeanList;
    }

    @Override
    public int getCount() {
        return categoryBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_category_list_view, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.iconCategory.setImageResource(utility.getIdIconByCategoryBean(categoryBeanList.get(position)));
        viewHolder.textView.setText(categoryBeanList.get(position).getName());
        viewHolder.btnElement.setOnClickListener(view1 -> {
            Intent intent = new Intent(mContext, NewEditCategoryActivity.class);
            intent.putExtra("idCategory", "" + categoryBeanList.get(position).getId());
            view1.getContext().startActivity(intent);
        });

        return view;
    }

    private static class ViewHolder {
        MaterialCardView btnElement;
        AppCompatImageView iconCategory;
        AppCompatTextView textView;

        public ViewHolder(View view) {
            btnElement = view.findViewById(R.id.btnElement);
            iconCategory = view.findViewById(R.id.iconCategory);
            textView = view.findViewById(R.id.textView);
        }
    }

}
