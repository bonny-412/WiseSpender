package it.bonny.app.wisespender.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import it.bonny.app.wisespender.manager.CategoryExpenseFragment;
import it.bonny.app.wisespender.manager.CategoryIncomeFragment;

public class MyViewPager2Adapter extends FragmentStateAdapter {

    public MyViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 1) {
            return new CategoryExpenseFragment();
        }
        return new CategoryIncomeFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }



}
