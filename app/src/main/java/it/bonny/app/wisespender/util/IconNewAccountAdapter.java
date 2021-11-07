package it.bonny.app.wisespender.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.IconBean;

public class IconNewAccountAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<IconBean> iconBeans = Utility.getListIconToNewAccount();

    public IconNewAccountAdapter(Context context) {
        this.mContext = context;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.item_icon_new_account, null);
        }

        final AppCompatImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(iconBeans.get(i).getDrawableInfo());

        return view;
    }

}
