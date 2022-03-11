package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.SettingsBean;
import it.bonny.app.wisespender.bean.TypeObjectBean;
import it.bonny.app.wisespender.component.BottomSheetPeriod;
import it.bonny.app.wisespender.component.BottomSheetPeriodListener;
import it.bonny.app.wisespender.component.BottomSheetSettings;
import it.bonny.app.wisespender.component.BottomSheetSettingsListener;
import it.bonny.app.wisespender.util.Utility;

public class SettingsActivity extends AppCompatActivity implements BottomSheetSettingsListener {

    private SwitchMaterial switchReminder;
    private ConstraintLayout containerReminder, containerTheme, containerCurrency;
    private TextView titleThemeSelected, titleCurrencySelected;
    private SettingsBean settingsBean;
    private final Utility utility = new Utility();
    private MaterialButton btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsBean = utility.getSettingsBeanSaved(this);
        init();

        containerTheme.setOnClickListener(view -> {
            BottomSheetSettings bottomSheetSettings = new BottomSheetSettings(settingsBean, 0,SettingsActivity.this);
            bottomSheetSettings.show(getSupportFragmentManager(), "SELECT_THEME");
        });

        containerCurrency.setOnClickListener(view -> {
            BottomSheetSettings bottomSheetSettings = new BottomSheetSettings(settingsBean, 1,SettingsActivity.this);
            bottomSheetSettings.show(getSupportFragmentManager(), "SELECT_CURRENCY");
        });

        btnReturn.setOnClickListener(view -> finish());
    }

    private void init() {
        switchReminder = findViewById(R.id.switchReminder);
        containerReminder = findViewById(R.id.containerReminder);
        containerTheme = findViewById(R.id.containerTheme);
        titleThemeSelected = findViewById(R.id.titleThemeSelected);
        containerCurrency = findViewById(R.id.containerCurrency);
        titleCurrencySelected = findViewById(R.id.titleCurrencySelected);
        btnReturn = findViewById(R.id.btnReturn);

        String nameTheme = "";
        if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_LIGHT_MODE) {
            nameTheme = getString(R.string.settings_page_theme_light);
        }else if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_DARK_MODE) {
            nameTheme = getString(R.string.settings_page_theme_dark);
        }else if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_SYSTEM_DEFAULT_MODE) {
            nameTheme = getString(R.string.settings_page_theme_system_default);
        }

        titleThemeSelected.setText(nameTheme);
        titleCurrencySelected.setText(settingsBean.getCurrency());
    }

    @Override
    public void onReturnTheme(int theme) {
        manageTheme(theme);
    }

    @Override
    public void onReturnCurrency(String currency) {
        settingsBean.setCurrency(currency);
        utility.saveSettingsBean(settingsBean, SettingsActivity.this);
        titleCurrencySelected.setText(settingsBean.getCurrency());
    }

    private void manageTheme(int theme) {
        settingsBean.setTheme(theme);
        utility.saveSettingsBean(settingsBean, SettingsActivity.this);
        if(theme == TypeObjectBean.SETTING_THEME_DARK_MODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else if(theme == TypeObjectBean.SETTING_THEME_LIGHT_MODE) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
        String nameTheme = "";
        if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_LIGHT_MODE) {
            nameTheme = getString(R.string.settings_page_theme_light);
        }else if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_DARK_MODE) {
            nameTheme = getString(R.string.settings_page_theme_dark);
        }else if(settingsBean.getTheme() == TypeObjectBean.SETTING_THEME_SYSTEM_DEFAULT_MODE) {
            nameTheme = getString(R.string.settings_page_theme_system_default);
        }

        titleThemeSelected.setText(nameTheme);
    }
}