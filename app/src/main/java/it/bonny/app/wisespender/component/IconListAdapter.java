package it.bonny.app.wisespender.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.HashMap;
import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.IconBean;

public class IconListAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<IconBean> iconBeans;
    public HashMap<Integer, Boolean> hashMapSelected;

    public IconListAdapter(List<IconBean> iconBeans, Context context) {
        this.mContext = context;
        this.iconBeans = iconBeans;
        hashMapSelected = new HashMap<>();
        for (int i = 0; i < iconBeans.size(); i++) {
            hashMapSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return iconBeans.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void makeAllUnselect(int position) {
        hashMapSelected.put(position, true);
        for (int i = 0; i < hashMapSelected.size(); i++) {
            if (i != position)
                hashMapSelected.put(i, false);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_icon_new_account, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else
            viewHolder = (ViewHolder) view.getTag();

        viewHolder.imageView.setImageResource(iconBeans.get(i).getDrawableInfo());
        viewHolder.containerIcon.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.icon_background_no_selected));
        viewHolder.imageView.setImageResource(iconBeans.get(i).getDrawableInfo());
        if (hashMapSelected != null && hashMapSelected.size() > 0 && hashMapSelected.get(i)) {
            viewHolder.containerIcon.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.selected_item_icon_new_account));
        } else {
            viewHolder.containerIcon.setBackground(AppCompatResources.getDrawable(mContext, R.drawable.icon_background_no_selected));
        }

        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
        LinearLayout containerIcon;

        public ViewHolder(View view) {
            imageView = view.findViewById(R.id.imageView);
            containerIcon = view.findViewById(R.id.containerIcon);
        }
    }

}
