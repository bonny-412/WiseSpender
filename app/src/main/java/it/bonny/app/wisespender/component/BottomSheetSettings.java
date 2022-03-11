package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.SettingsBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;

public class BottomSheetSettings extends BottomSheetDialogFragment {
    private final Activity mActivity;
    private BottomSheetSettingsListener bottomSheetSettingsListener;
    private final SettingsBean settingsBean;
    private final int type;
    private View view;
    ConstraintLayout containerLightTheme, containerDarkTheme, containerSystemTheme;

    public BottomSheetSettings(SettingsBean settingsBean, int type, Activity mActivity) {
        this.settingsBean = settingsBean;
        this.mActivity = mActivity;
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        if(type == 0) {
            view = inflater.inflate(R.layout.bottom_sheet_settings_theme, container, false);

            initTheme(view);

            containerLightTheme.setOnClickListener(view12 -> {
                settingsBean.setTheme(TypeObjectBean.SETTING_THEME_LIGHT_MODE);
                bottomSheetSettingsListener.onReturnTheme(settingsBean.getTheme());
                dismiss();
            });

            containerDarkTheme.setOnClickListener(view12 -> {
                settingsBean.setTheme(TypeObjectBean.SETTING_THEME_DARK_MODE);
                bottomSheetSettingsListener.onReturnTheme(settingsBean.getTheme());
                dismiss();
            });

            containerSystemTheme.setOnClickListener(view12 -> {
                settingsBean.setTheme(TypeObjectBean.SETTING_THEME_SYSTEM_DEFAULT_MODE);
                bottomSheetSettingsListener.onReturnTheme(settingsBean.getTheme());
                dismiss();
            });
        }else if(type == 1) {
            view = inflater.inflate(R.layout.bottom_sheet_settings_currency, container, false);
            EditText currency = view.findViewById(R.id.currency);
            MaterialButton btnCancel = view.findViewById(R.id.btnCancel);
            MaterialButton btnOk = view.findViewById(R.id.btnOk);

            if(settingsBean.getCurrency() != null && !"".equals(settingsBean.getCurrency()))
                currency.setText(settingsBean.getCurrency());

            btnCancel.setOnClickListener(view -> dismiss());

            btnOk.setOnClickListener(view -> {
                String currencyTxt = currency.getText().toString();
                settingsBean.setCurrency(currencyTxt);
                bottomSheetSettingsListener.onReturnCurrency(settingsBean.getCurrency());
                dismiss();
            });
        }


        return view;
    }

    private void initTheme(View view) {
        containerLightTheme = view.findViewById(R.id.containerLightTheme);
        containerDarkTheme = view.findViewById(R.id.containerDarkTheme);
        containerSystemTheme = view.findViewById(R.id.containerSystemTheme);

        if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_LIGHT_MODE) {
            containerLightTheme.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerLightTheme.setElevation(6);
        }else if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_DARK_MODE) {
            containerDarkTheme.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerDarkTheme.setElevation(6);
        }else if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_SYSTEM_DEFAULT_MODE) {
            containerSystemTheme.setBackgroundColor(mActivity.getColor(R.color.background_card));
            containerSystemTheme.setElevation(6);
        }
    }

    /**
     */

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            bottomSheetSettingsListener = (BottomSheetSettingsListener) context;
        }catch (ClassCastException e) {
            //TODO: Firebase
            throw new ClassCastException(context
                    + " must implement BottomSheetSettingsListener");
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        bottomSheetSettingsListener.onReturnTheme(settingsBean.getTheme());
        super.onCancel(dialog);
    }

}
