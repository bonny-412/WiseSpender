package it.bonny.app.wisespender.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.fragment.HomeFragment;
import it.bonny.app.wisespender.fragment.StatisticsFragment;
import it.bonny.app.wisespender.fragment.TransactionFragment;
import it.bonny.app.wisespender.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
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

}