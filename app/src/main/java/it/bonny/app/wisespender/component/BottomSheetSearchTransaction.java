package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.bean.CategoryBean;
import it.bonny.app.wisespender.bean.FilterTransactionBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.db.DatabaseHelper;

public class BottomSheetSearchTransaction extends BottomSheetDialogFragment implements RecyclerViewClickBottomSheetInterface {

    private final int type;
    private final Activity mActivity;
    private View view;

    private ConstraintLayout containerIncome, containerExpense;
    private MaterialCheckBox checkBoxIncome, checkBoxExpense;


    private BottomSheetFilterSearchListener bottomSheetFilterSearchListener;
    private final FilterTransactionBean filterTransactionBean;

    private final DatabaseHelper db;

    public BottomSheetSearchTransaction(int type, Activity activity, FilterTransactionBean filterTransactionBean) {
        this.type = type;
        this.mActivity = activity;
        this.filterTransactionBean = filterTransactionBean;
        db = new DatabaseHelper(activity.getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        if(type == TypeObjectBean.SEARCH_TRANSACTION_TYPE) {
            view = inflater.inflate(R.layout.bottom_sheet_search_transaction_type, container, false);
            filterTransactionBean.setTypeBottomSheet(TypeObjectBean.SEARCH_TRANSACTION_TYPE);
            initTransactionType();

            containerIncome.setOnClickListener(view -> {
                checkBoxIncome.setChecked(!checkBoxIncome.isChecked());
                configCheckTypeFilter();
            });

            containerExpense.setOnClickListener(view -> {
                checkBoxExpense.setChecked(!checkBoxExpense.isChecked());
                configCheckTypeFilter();
            });

            checkBoxIncome.setOnCheckedChangeListener((compoundButton, b) -> {
                checkBoxIncome.setChecked(b);
                configCheckTypeFilter();
            });

            checkBoxExpense.setOnCheckedChangeListener((compoundButton, b) -> {
                checkBoxExpense.setChecked(b);
                configCheckTypeFilter();
            });

        }else if(type == TypeObjectBean.SEARCH_TRANSACTION_CATEGORY) {
            view = inflater.inflate(R.layout.bottom_sheet_search_transaction_category, container, false);
            filterTransactionBean.setTypeBottomSheet(TypeObjectBean.SEARCH_TRANSACTION_CATEGORY);
            RecyclerView recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            findAllCategories(recyclerViewCategory, progressBar, mActivity, db);
        }else if(type == TypeObjectBean.SEARCH_TRANSACTION_ACCOUNT) {
            view = inflater.inflate(R.layout.bottom_sheet_search_transaction_account, container, false);
            filterTransactionBean.setTypeBottomSheet(TypeObjectBean.SEARCH_TRANSACTION_ACCOUNT);
            RecyclerView recyclerViewAccount = view.findViewById(R.id.recyclerViewAccount);
            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            findAllAccounts(recyclerViewAccount, progressBar, mActivity, db);
        }else if(type == TypeObjectBean.SEARCH_TRANSACTION_DATE) {
            view = inflater.inflate(R.layout.bottom_sheet_search_transaction_date, container, false);
            filterTransactionBean.setTypeBottomSheet(TypeObjectBean.SEARCH_TRANSACTION_DATE);

            MaterialButton btnCancel = view.findViewById(R.id.btnCancel);
            MaterialButton btnOk = view.findViewById(R.id.btnOk);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String[] sStart = filterTransactionBean.getDateFrom().split("/");
            int dayFromStart = Integer.parseInt(sStart[0]);
            int monthFromStart = Integer.parseInt(sStart[1]);
            int yearFromStart = Integer.parseInt(sStart[2]);

            String[] sEnd = filterTransactionBean.getDateA().split("/");
            int dayAEnd = Integer.parseInt(sEnd[0]);
            int monthAEnd = Integer.parseInt(sEnd[1]);
            int yearAEnd = Integer.parseInt(sEnd[2]);

            Calendar calendarStart = Calendar.getInstance();
            calendarStart.set(Calendar.YEAR, yearFromStart);
            if(monthFromStart > 0)
                monthFromStart = monthFromStart - 1;
            calendarStart.set(Calendar.MONTH, monthFromStart);
            calendarStart.set(Calendar.DAY_OF_MONTH, dayFromStart);

            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.set(Calendar.YEAR, yearAEnd);
            if(monthAEnd > 0)
                monthAEnd = monthAEnd - 1;
            calendarEnd.set(Calendar.MONTH, monthAEnd);
            calendarEnd.set(Calendar.DAY_OF_MONTH, dayAEnd);

            ConstraintLayout dateStartElement = view.findViewById(R.id.dateStart);
            ConstraintLayout dateEndElement = view.findViewById(R.id.dateEnd);
            TextView dateStartText = view.findViewById(R.id.dateStartText);
            TextView dateEndText = view.findViewById(R.id.dateEndText);

            MaterialDatePicker.Builder<Long> materialDateBuilderStart = MaterialDatePicker.Builder.datePicker();
            long dateStartLong = calendarStart.getTime().getTime();
            materialDateBuilderStart.setSelection(dateStartLong);
            final MaterialDatePicker<Long> materialDatePickerStart = materialDateBuilderStart.build();
            materialDatePickerStart.setCancelable(false);

            MaterialDatePicker.Builder<Long> materialDateBuilderEnd = MaterialDatePicker.Builder.datePicker();
            long dateEndLong = calendarEnd.getTime().getTime();
            materialDateBuilderEnd.setSelection(dateEndLong);
            final MaterialDatePicker<Long> materialDatePickerEnd = materialDateBuilderEnd.build();
            materialDatePickerEnd.setCancelable(false);

            dateStartElement.setOnClickListener(view -> {
                if(getActivity() != null)
                    materialDatePickerStart.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            });

            materialDatePickerStart.addOnPositiveButtonClickListener(selection -> {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(selection);
                controlDateFromA(dateFormat.format(c.getTime()), true, calendarStart, dateStartText);
            });

            dateEndElement.setOnClickListener(view -> {
                if(getActivity() != null)
                    materialDatePickerEnd.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            });

            materialDatePickerEnd.addOnPositiveButtonClickListener(selection -> {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(selection);
                controlDateFromA(dateFormat.format(c.getTime()), false, calendarEnd, dateEndText);
            });

            btnCancel.setOnClickListener(view -> dismiss());

            btnOk.setOnClickListener(view -> {
                bottomSheetFilterSearchListener.onFilterClick(filterTransactionBean);
                dismiss();
            });

            dateStartText.setText(dateFormat.format(calendarStart.getTime()));
            dateEndText.setText(dateFormat.format(calendarEnd.getTime()));


        }

        return view;
    }

    private void controlDateFromA(String dateSelected, boolean isDateFromSelected, Calendar calendar, TextView dateText) {
        boolean esito = false;

        String dateTextFinal;

        String[] stringsSelected = dateSelected.split("/");
        int yearSelected = Integer.parseInt(stringsSelected[2]);
        int monthSelected = Integer.parseInt(stringsSelected[1]);
        int daySelected = Integer.parseInt(stringsSelected[0]);

        String[] strings;
        int year, month, day;

        if(isDateFromSelected) {
            strings = filterTransactionBean.getDateA().split("/");
            year = Integer.parseInt(strings[2]);
            month = Integer.parseInt(strings[1]);
            day = Integer.parseInt(strings[0]);

            if(yearSelected <= year) {
                if(monthSelected <= month) {
                    if(daySelected <= day) {
                        esito = true;
                        filterTransactionBean.setDateFrom(dateSelected);
                    }else
                        filterTransactionBean.setDateFrom(filterTransactionBean.getDateA());
                }else
                    filterTransactionBean.setDateFrom(filterTransactionBean.getDateA());
            }else
                filterTransactionBean.setDateFrom(filterTransactionBean.getDateA());

            dateTextFinal = filterTransactionBean.getDateFrom();

        }else {
            strings = filterTransactionBean.getDateFrom().split("/");
            year = Integer.parseInt(strings[2]);
            month = Integer.parseInt(strings[1]);
            day = Integer.parseInt(strings[0]);

            if(yearSelected >= year) {
                if(monthSelected >= month) {
                    if(daySelected >= day) {
                        esito = true;
                        filterTransactionBean.setDateA(dateSelected);
                    }else
                        filterTransactionBean.setDateA(filterTransactionBean.getDateFrom());
                }else
                    filterTransactionBean.setDateA(filterTransactionBean.getDateFrom());
            }else
                filterTransactionBean.setDateA(filterTransactionBean.getDateFrom());

            dateTextFinal = filterTransactionBean.getDateA();
        }

        dateText.setText(dateTextFinal);
        if(esito) {
            calendar.set(Calendar.YEAR, yearSelected);
            if(monthSelected > 0)
                calendar.set(Calendar.MONTH, monthSelected - 1);
            else
                calendar.set(Calendar.MONTH, monthSelected);
            calendar.set(Calendar.DAY_OF_MONTH, daySelected);
        }else {
            calendar.set(Calendar.YEAR, year);
            if(month > 0)
                calendar.set(Calendar.MONTH, month - 1);
            else
                calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
        }
    }

    private void configCheckTypeFilter() {
        if(checkBoxIncome.isChecked() && checkBoxExpense.isChecked()) {
            filterTransactionBean.setTypeFilter(TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_ALL);
        }else if(checkBoxIncome.isChecked()) {
            filterTransactionBean.setTypeFilter((TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_INCOME));
        }else if(checkBoxExpense.isChecked()) {
            filterTransactionBean.setTypeFilter((TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_EXPENSE));
        }else if(!checkBoxIncome.isChecked() && !checkBoxExpense.isChecked()) {
            filterTransactionBean.setTypeFilter(TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_ALL_NO_CHECKED);
        }
    }

    private void initTransactionType() {
        containerIncome = view.findViewById(R.id.containerIncome);
        containerExpense = view.findViewById(R.id.containerExpense);
        checkBoxIncome = view.findViewById(R.id.checkBoxIncome);
        checkBoxExpense = view.findViewById(R.id.checkBoxExpense);

        if(filterTransactionBean.getTypeFilter() == TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_INCOME) {
            checkBoxIncome.setChecked(true);
            checkBoxExpense.setChecked(false);
        }else if(filterTransactionBean.getTypeFilter() == TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_EXPENSE) {
            checkBoxExpense.setChecked(true);
            checkBoxIncome.setChecked(false);
        }else if(filterTransactionBean.getTypeFilter() == TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_ALL_NO_CHECKED) {
            checkBoxExpense.setChecked(false);
            checkBoxIncome.setChecked(false);
        }else if(filterTransactionBean.getTypeFilter() == TypeObjectBean.FILTER_SEARCH_TRANSACTION_TYPE_ALL) {
            checkBoxExpense.setChecked(true);
            checkBoxIncome.setChecked(true);
        }

    }

    private void findAllCategories(RecyclerView recyclerView, ProgressBar progressBar1, Activity mActivity, DatabaseHelper db) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        recyclerView.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        service.execute(() -> {
            List<CategoryBean> categoryBeanList = db.getAllCategoryBeans();
            ListCategoryBottomSheetAdapter listCategoryBottomSheetAdapter = new ListCategoryBottomSheetAdapter(categoryBeanList, filterTransactionBean.getIdCategories(), BottomSheetSearchTransaction.this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
            recyclerView.setAdapter(listCategoryBottomSheetAdapter);

            mActivity.runOnUiThread(() -> {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.GONE);
            });
        });
    }

    private void findAllAccounts(RecyclerView recyclerView, ProgressBar progressBar1, Activity mActivity, DatabaseHelper db) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        recyclerView.setVisibility(View.GONE);
        progressBar1.setVisibility(View.VISIBLE);
        service.execute(() -> {
            List<AccountBean> accountBeans = db.getAllAccountBeansNoMaster();
            ListAccountSearchFilterAdapter listAccountSearchFilterAdapter = new ListAccountSearchFilterAdapter(accountBeans, filterTransactionBean.getIdAccounts(), BottomSheetSearchTransaction.this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity.getApplicationContext()));
            recyclerView.setAdapter(listAccountSearchFilterAdapter);

            mActivity.runOnUiThread(() -> {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.GONE);
            });
        });
    }

    /**
     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetFilterSearchListener = (BottomSheetFilterSearchListener) context;
        }catch (ClassCastException e) {
            //TODO: Firebase
            throw new ClassCastException(context.toString()
                    + " must implement BottomSheetFilterSearchListener");
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        bottomSheetFilterSearchListener.onFilterClick(filterTransactionBean);
        super.onCancel(dialog);
    }

    @Override
    public void onItemClick(long idElement, boolean isCategory) {
        if(isCategory) {
            if(filterTransactionBean.getIdCategories().contains(idElement)) {
                filterTransactionBean.getIdCategories().remove(idElement);
            }else {
                filterTransactionBean.getIdCategories().add(idElement);
            }
            filterTransactionBean.setIdCategories(filterTransactionBean.getIdCategories());
        }else {
            if(filterTransactionBean.getIdAccounts().contains(idElement)) {
                filterTransactionBean.getIdAccounts().remove(idElement);
            }else {
                filterTransactionBean.getIdAccounts().add(idElement);
            }
            filterTransactionBean.setIdAccounts(filterTransactionBean.getIdAccounts());
        }
    }

}
