package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.HashMap;
import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.util.Utility;

public class ListMonthSelectPeriodAdapter extends BaseAdapter {
    private final List<String> months;
    private final int monthSelected;
    private final Context context;
    public HashMap<Integer, Boolean> hashMapSelected;

    public ListMonthSelectPeriodAdapter(List<String> months, int monthSelected, Context context) {
        this.months = months;
        this.monthSelected = monthSelected;
        this.context = context;
        hashMapSelected = new HashMap<>();
        for (int i = 0; i < months.size(); i++) {
            hashMapSelected.put(i, false);
        }
    }

    public void makeAllUnselect(int position) {
        hashMapSelected.put(position, true);
        for (int i = 0; i < hashMapSelected.size(); i++) {
            if (i != position)
                hashMapSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return months.size();
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
        final ListMonthSelectPeriodAdapter.ViewHolder viewHolder;
        String month = months.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_bottom_sheet_select_period, viewGroup, false);
            viewHolder = new ListMonthSelectPeriodAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        }else
            viewHolder = (ListMonthSelectPeriodAdapter.ViewHolder) view.getTag();

        viewHolder.nameMonth.setText(month);
        viewHolder.mainLayout.setBackgroundColor(context.getColor(R.color.background));
        if (hashMapSelected != null && hashMapSelected.size() > 0 && hashMapSelected.get(i)) {
            viewHolder.mainLayout.setBackgroundColor(context.getColor(R.color.background_card));
        } else {
            viewHolder.mainLayout.setBackgroundColor(context.getColor(R.color.background));
        }

        return view;
    }

    private static class ViewHolder {
        TextView nameMonth;
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            nameMonth = view.findViewById(R.id.nameMonth);
            mainLayout = view.findViewById(R.id.mainLayout);
        }
    }

}
