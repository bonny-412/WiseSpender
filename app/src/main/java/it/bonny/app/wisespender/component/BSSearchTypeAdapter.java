package it.bonny.app.wisespender.component;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.util.Utility;

public class BSSearchTypeAdapter extends RecyclerView.Adapter<BSSearchTypeAdapter.ViewHolder> {

    private final List<String> elements;
    private final Activity activity;
    private final Utility utility = new Utility();
    private final RecyclerViewClickInterface viewClickInterface;

    public BSSearchTypeAdapter(List<String> elements, Activity activity, RecyclerViewClickInterface viewClickInterface) {
        this.activity = activity;
        this.elements = elements;
        this.viewClickInterface = viewClickInterface;
    }

    @NonNull
    @Override
    public BSSearchTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_bs_search_type, parent, false);
        return new BSSearchTypeAdapter.ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull BSSearchTypeAdapter.ViewHolder holder, int position) {
        final String element = elements.get(position);
        try {
            holder.textView.setText(element);
            //holder..setOnClickListener(view -> categoryInterface.onItemClick(holder.getAdapterPosition()));
        } catch (Exception e) {
            //TODO: Firebase
            Log.e("bonny", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialCheckBox checkBox;
        TextView textView;
        ConstraintLayout mainLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.textView);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }

    }

}
