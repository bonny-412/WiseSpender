package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.util.Utility;

public class BottomSheetPeriod extends BottomSheetDialogFragment {
    private final Activity mActivity;
    private BottomSheetPeriodListener bottomSheetPeriodListener;
    private int monthSelected;
    private int yearSelected;
    private final List<String> months;

    public BottomSheetPeriod(int monthSelected, int yearSelected, List<String> months, Activity mActivity) {
        this.monthSelected = monthSelected;
        this.yearSelected = yearSelected;
        this.months = months;
        this.mActivity = mActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        View view = inflater.inflate(R.layout.bottom_sheet_select_period, container, false);

        GridView gridViewMonth = view.findViewById(R.id.gridViewMonth);
        TextView yearSelectedElm = view.findViewById(R.id.yearSelected);
        MaterialButton btnLastYear = view.findViewById(R.id.btnLastYear);
        MaterialButton btnNextYear = view.findViewById(R.id.btnNextYear);

        yearSelectedElm.setText(convertIntToString(yearSelected));

        ListMonthSelectPeriodAdapter listMonthSelectPeriodAdapter = new ListMonthSelectPeriodAdapter(months, monthSelected, getContext());
        gridViewMonth.setAdapter(listMonthSelectPeriodAdapter);
        listMonthSelectPeriodAdapter.makeAllUnselect(monthSelected);
        listMonthSelectPeriodAdapter.notifyDataSetChanged();

        btnLastYear.setOnClickListener(view1 -> {
            yearSelected = yearSelected - 1;
            yearSelectedElm.setText(convertIntToString(yearSelected));
        });

        btnNextYear.setOnClickListener(view1 -> {
            yearSelected = yearSelected + 1;
            yearSelectedElm.setText(convertIntToString(yearSelected));
        });

        gridViewMonth.setOnItemClickListener((adapterView, view1, position, l) -> {
            listMonthSelectPeriodAdapter.makeAllUnselect(position);
            listMonthSelectPeriodAdapter.notifyDataSetChanged();
            monthSelected = position;
        });


        return view;
    }

    private String convertIntToString(int a) {
        return "" + a;
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
        bottomSheetPeriodListener.onReturnMonth(monthSelected, yearSelected);
        super.onCancel(dialog);
    }

}
