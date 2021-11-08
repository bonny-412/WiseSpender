package it.bonny.app.wisespender.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.fragment.HomeFragment;
import it.bonny.app.wisespender.fragment.StatisticsFragment;
import it.bonny.app.wisespender.fragment.TransactionFragment;
import it.bonny.app.wisespender.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] iconSelected = {0};

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        bottomNavigationView.getMenu().getItem(2).setEnabled(false);
        bottomNavigationView.setOnItemSelectedListener(onItemSelectedListener);

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, new HomeFragment()).commit();

    }

    private final NavigationBarView.OnItemSelectedListener onItemSelectedListener = item -> {
        Fragment selectedFragment = null;
        if(item.getItemId() == R.id.home)
            selectedFragment = new HomeFragment();
        else if(item.getItemId() == R.id.transactions)
            selectedFragment = new TransactionFragment();
        else if(item.getItemId() == R.id.statistics)
            selectedFragment = new StatisticsFragment();
        else if(item.getItemId() == R.id.user)
            selectedFragment = new UserFragment();

        if(selectedFragment != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, selectedFragment).commit();

        return true;
    };

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

}