package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.util.Utility;

public class BottomSheetPeriod extends BottomSheetDialogFragment {
    private final Activity mActivity;
    private BottomSheetPeriodListener bottomSheetPeriodListener;
    private int periodSelected;
    ConstraintLayout containerDay, containerMonth, containerYear, containerAll, containerInterval, containerDate;

    public BottomSheetPeriod(int periodSelected, Activity mActivity) {
        this.periodSelected = periodSelected;
        this.mActivity = mActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_select_period, container, false);

        init(view);

        containerDay.setOnClickListener(view12 -> {
            periodSelected = TypeObjectBean.PERIOD_SELECTED_DAY;
            bottomSheetPeriodListener.onReturnMonth(periodSelected);
            dismiss();
        });

        containerMonth.setOnClickListener(view12 -> {
            periodSelected = TypeObjectBean.PERIOD_SELECTED_MONTH;
            bottomSheetPeriodListener.onReturnMonth(periodSelected);
            dismiss();
        });

        containerYear.setOnClickListener(view12 -> {
            periodSelected = TypeObjectBean.PERIOD_SELECTED_YEAR;
            bottomSheetPeriodListener.onReturnMonth(periodSelected);
            dismiss();
        });

        containerAll.setOnClickListener(view12 -> {
            periodSelected = TypeObjectBean.PERIOD_SELECTED_ALL;
            bottomSheetPeriodListener.onReturnMonth(periodSelected);
            dismiss();
        });

        containerInterval.setOnClickListener(view12 -> {
            periodSelected = TypeObjectBean.PERIOD_SELECTED_INTERVAL;
            bottomSheetPeriodListener.onReturnMonth(periodSelected);
            dismiss();
        });

        containerDate.setOnClickListener(view12 -> {
            periodSelected = TypeObjectBean.PERIOD_SELECTED_DATE;
            bottomSheetPeriodListener.onReturnMonth(periodSelected);
            dismiss();
        });

        return view;
    }

    private void init(View view) {
        containerDay = view.findViewById(R.id.containerDay);
        containerMonth = view.findViewById(R.id.containerMonth);
        containerYear = view.findViewById(R.id.containerYear);
        containerAll = view.findViewById(R.id.containerAll);
        containerInterval = view.findViewById(R.id.containerInterval);
        containerDate = view.findViewById(R.id.containerDate);

        if(periodSelected == TypeObjectBean.PERIOD_SELECTED_DAY) {
            containerDay.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerDay.setElevation(6);
        }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_MONTH) {
            containerMonth.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerMonth.setElevation(6);
        }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_YEAR) {
            containerYear.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerYear.setElevation(6);
        }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_ALL) {
            containerAll.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerAll.setElevation(6);
        }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_INTERVAL) {
            containerInterval.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerInterval.setElevation(6);
        }else if(periodSelected == TypeObjectBean.PERIOD_SELECTED_DATE) {
            containerDate.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerDate.setElevation(6);
        }
    }

    /**
     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetPeriodListener = (BottomSheetPeriodListener) context;
        }catch (ClassCastException e) {
            //TODO: Firebase
            throw new ClassCastException(context
                    + " must implement BottomSheetPeriodListener");
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        bottomSheetPeriodListener.onReturnMonth(periodSelected);
        super.onCancel(dialog);
    }

}
