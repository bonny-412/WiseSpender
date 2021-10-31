package it.bonny.app.wisespender.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.manager.BottomSheetAccount;

public class HomeFragment extends Fragment {

    private FragmentActivity myContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);

        TextView accountBtn = root.findViewById(R.id.accountBtn);

        accountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetAccount bottomSheetAccount = new BottomSheetAccount();
                bottomSheetAccount.show(getParentFragmentManager(), "TAG");
            }
        });

        return root;
    }


}
