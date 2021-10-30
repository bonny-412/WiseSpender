package it.bonny.app.wisespender.manager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, new HomeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            if(item.getItemId() == R.id.home) {
                fragment = new HomeFragment();
            }else if(item.getItemId() == R.id.statistics) {
                fragment = new StatisticsFragment();
            }else if(item.getItemId() == R.id.transactions) {
                fragment = new TransactionFragment();
            }else if(item.getItemId() == R.id.user) {
                fragment = new UserFragment();
            }

            if(fragment != null)
                getSupportFragmentManager().beginTransaction().replace(R.id.rootLayout, fragment).commit();

            return true;
        });

    }

}