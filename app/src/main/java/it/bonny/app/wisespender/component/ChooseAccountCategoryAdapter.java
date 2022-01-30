package it.bonny.app.wisespender.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;
import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.IconBean;
import it.bonny.app.wisespender.util.Utility;

public class ChooseAccountCategoryAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<AccountBean> accountBeans;
    private final List<CategoryBean> categoryBeans;
    private final Utility utility = new Utility();
    private final int positionSelected;
    private final long id;

    public ChooseAccountCategoryAdapter(List<AccountBean> accountBeans, List<CategoryBean> categoryBeans, int positionSelected, long id, Context context) {
        this.mContext = context;
        this.accountBeans = accountBeans;
        this.categoryBeans = categoryBeans;
        this.positionSelected = positionSelected;
        this.id = id;
    }

    @Override
    public int getCount() {
        int val;
        if(accountBeans != null) {
            val = accountBeans.size();
        }else {
            val = categoryBeans.size();
        }
        return val;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_grid_view, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) view.getTag();

        if(accountBeans != null) {
            viewHolder.imageView.setImageResource(utility.getIdIconByAccountBean(accountBeans.get(i)));
            viewHolder.textView.setText(accountBeans.get(i).getName());
        }else {
            viewHolder.imageView.setImageResource(utility.getIdIconByCategoryBean(categoryBeans.get(i)));
            viewHolder.textView.setText(categoryBeans.get(i).getName());
        }

        if(id > 0) {
            if(accountBeans != null) {
                if(id == accountBeans.get(i).getId()) {
                    viewHolder.iconSelected.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.iconSelected.setVisibility(View.GONE);
                }
            }else {
                if(id == categoryBeans.get(i).getId()) {
                    viewHolder.iconSelected.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.iconSelected.setVisibility(View.GONE);
                }
            }
        }else {
            if(positionSelected != -1 && positionSelected == i)
                viewHolder.iconSelected.setVisibility(View.VISIBLE);
            else
                viewHolder.iconSelected.setVisibility(View.GONE);
        }


        return view;
    }

    private static class ViewHolder {
        AppCompatImageView imageView, iconSelected;
        ConstraintLayout container;
        TextView textView;

        public ViewHolder(View view) {
            imageView = view.findViewById(R.id.imageView);
            container = view.findViewById(R.id.container);
            iconSelected = view.findViewById(R.id.iconSelected);
            textView = view.findViewById(R.id.textView);
        }
    }

}
