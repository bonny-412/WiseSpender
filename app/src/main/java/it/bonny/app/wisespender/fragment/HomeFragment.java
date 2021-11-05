package it.bonny.app.wisespender.fragment;

import android.content.Context;
import android.content.SharedPreferences;
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
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.manager.BottomSheetAccount;
import it.bonny.app.wisespender.util.Utility;

public class HomeFragment extends Fragment {

    private FragmentActivity myContext;
    private DatabaseHelper db;
    private final Utility utility = new Utility();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);
        db = new DatabaseHelper(getContext());
        showWelcomeAlert();
        TextView accountBtn = root.findViewById(R.id.accountBtn);

        accountBtn.setOnClickListener(view -> {
            BottomSheetAccount bottomSheetAccount = new BottomSheetAccount(getActivity());
            bottomSheetAccount.show(getParentFragmentManager(), "TAG");
        });

        return root;
    }


    //Shows the welcome alert
    private void showWelcomeAlert(){
        boolean firstStart = false;
        SharedPreferences prefs;
        if(getContext() != null) {
            try {
                utility.insertAccountDefault(db);
                utility.insertCategoryDefault(db);
                prefs = getContext().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE);
                firstStart = prefs.getBoolean("firstStart", true);
            }catch (Exception e) {
                //TODO: Firebase
            }
        }
        if(firstStart) {
            Toast.makeText(getContext(), "Benvenuto", Toast.LENGTH_SHORT).show();
        }
    }


}
