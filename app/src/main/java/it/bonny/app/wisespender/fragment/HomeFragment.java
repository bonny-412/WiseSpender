package it.bonny.app.wisespender.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

import java.util.List;

import it.bonny.app.wisespender.R;
import it.bonny.app.wisespender.bean.AccountBean;
import it.bonny.app.wisespender.db.DatabaseHelper;
import it.bonny.app.wisespender.manager.BottomSheetAccount;
import it.bonny.app.wisespender.util.Utility;

public class HomeFragment extends Fragment {

    private DatabaseHelper db;
    private Utility utility;
    private TextView accountName, accountBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home_fragment, container, false);
        init(root);

        utility = new Utility(getActivity());
        db = new DatabaseHelper(getContext());
        showWelcomeAlert();

        List<AccountBean> accountBeanList = db.getAllAccountBeans();
        AccountBean accountBeanSelected = db.getAccountBeanSelected();
        db.closeDB();

        accountName.setText(accountBeanSelected.getName());
        accountBtn.setOnClickListener(view -> {
            BottomSheetAccount bottomSheetAccount = new BottomSheetAccount(accountBeanList, getActivity());
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
                prefs = getContext().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE);
                firstStart = prefs.getBoolean("firstStart", true);
            }catch (Exception e) {
                //TODO: Firebase
                Log.e("HOME_FRAGMENT", e.toString());
            }
        }
        if(firstStart) {
            utility.insertAccountDefault(db);
            Toast.makeText(getContext(), "Benvenuto", Toast.LENGTH_SHORT).show();
            if(getActivity() != null) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Utility.PREFS_NAME_FILE, Context.MODE_PRIVATE).edit();
                editor.putBoolean("firstStart", false);
                editor.apply();
            }
        }
    }

    private void init(View root) {
        accountBtn = root.findViewById(R.id.accountBtn);
        accountName = root.findViewById(R.id.accountName);
    }

}
