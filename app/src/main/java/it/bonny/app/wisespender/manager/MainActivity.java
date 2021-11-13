package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.util.Utility;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;
    private DatabaseHelper db;
    private final Utility utility = new Utility();
    private MaterialCardView btnAccounts;
    private TextView accountName, accountBtn;
    private final Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showWelcomeAlert();
        init();

        db = new DatabaseHelper(getApplicationContext());
        List<AccountBean> accountBeanList = db.getAllAccountBeans();
        AccountBean accountBeanSelected = db.getAccountBeanSelected();
        db.closeDB();

        btnAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ListAccountsActivity.class);
                startActivity(intent);
            }
        });

        BottomSheetAccount bottomSheetAccount = new BottomSheetAccount(accountBeanList, activity);
        accountName.setText(accountBeanSelected.getName());
        accountBtn.setOnClickListener(view -> bottomSheetAccount.show(getSupportFragmentManager(), "TAG"));

    }

    private void init() {
        btnAccounts = findViewById(R.id.btnAccounts);
        accountBtn = findViewById(R.id.accountBtn);
        accountName = findViewById(R.id.accountName);
    }

    //Shows the welcome alert
    private void showWelcomeAlert(){
        boolean firstStart = false;
        SharedPreferences prefs;
        if(getApplicationContext() != null) {
            try {
                prefs = getApplicationContext().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE);
                firstStart = prefs.getBoolean("firstStart", true);
            }catch (Exception e) {
                //TODO: Firebase
                Log.e("HOME_FRAGMENT", e.toString());
            }
        }
        if(firstStart) {
            if(activity != null) {
                utility.insertAccountDefault(db, activity);
                SharedPreferences.Editor editor = activity.getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE).edit();
                editor.putBoolean("firstStart", false);
                editor.apply();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(a);
        } else
            Toast.makeText(this, getString(R.string.pressToExit), Toast.LENGTH_SHORT).show();

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

}