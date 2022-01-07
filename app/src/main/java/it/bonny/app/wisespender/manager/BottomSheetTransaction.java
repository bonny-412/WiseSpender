package it.bonny.app.wisespender.manager;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.TypeObjectBean;

public class BottomSheetTransaction extends BottomSheetDialogFragment {
    private BottomSheetListener mListener;
    private int filterType;
    private View view;
    private ConstraintLayout containerToday, containerWeek, containerMonth, containerYear, containerAll, containerCustom;
    private ImageView radioButtonToday, radioButtonWeek, radioButtonMonth, radioButtonYear, radioButtonAll, radioButtonCustom;

    public BottomSheetTransaction(int filterType) {
        this.filterType = filterType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        view = inflater.inflate(R.layout.bottom_sheet_transaction, container, false);
        init();
        changeFilter(filterType, true);

        containerToday.setOnClickListener(view -> changeFilter(TypeObjectBean.FILTER_DATE_DAY, false));
        containerWeek.setOnClickListener(view -> changeFilter(TypeObjectBean.FILTER_DATE_RANGE, false));

        return view;
    }

    private void init() {
        containerToday = view.findViewById(R.id.containerToday);
        containerWeek = view.findViewById(R.id.containerWeek);
        containerMonth = view.findViewById(R.id.containerMonth);
        containerYear = view.findViewById(R.id.containerYear);
        containerToday = view.findViewById(R.id.containerToday);
        containerAll = view.findViewById(R.id.containerAll);
        containerCustom = view.findViewById(R.id.containerCustom);

        radioButtonToday = view.findViewById(R.id.radioButtonToday);
        radioButtonWeek = view.findViewById(R.id.radioButtonWeek);
        radioButtonMonth = view.findViewById(R.id.radioButtonMonth);
        radioButtonYear = view.findViewById(R.id.radioButtonYear);
        radioButtonAll = view.findViewById(R.id.radioButtonAll);
        radioButtonCustom = view.findViewById(R.id.radioButtonCustom);
    }

    private void changeFilter(int filterType, boolean isOpen) {
        this.filterType = filterType;
        radioButtonToday.setVisibility(View.GONE);
        radioButtonWeek.setVisibility(View.GONE);
        radioButtonMonth.setVisibility(View.GONE);
        radioButtonYear.setVisibility(View.GONE);
        radioButtonAll.setVisibility(View.GONE);
        radioButtonCustom.setVisibility(View.GONE);
        if(filterType == TypeObjectBean.FILTER_DATE_DAY) {
            radioButtonToday.setVisibility(View.VISIBLE);
        }else if(filterType == TypeObjectBean.FILTER_DATE_RANGE) {
            radioButtonWeek.setVisibility(View.VISIBLE);
        }

        if(!isOpen)
            closeBottomSheet();
    }

    private void closeBottomSheet() {
        String res = "" + this.filterType;
        mListener.onFilterClick(res);
        dismiss();
    }

    public interface BottomSheetListener {
        void onFilterClick(String filterType);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (BottomSheetListener) context;
        }catch (ClassCastException e) {
            //TODO: Firebase
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetListener");
        }
    }
}
