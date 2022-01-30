package it.bonny.app.wisespender.component;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import it.bonny.app.wisespender.R;

public class LoadingDialog {

    private final Activity activity;
    private AlertDialog dialog;
    private String title;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void startDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = View.inflate(activity, R.layout.custom_loading_dialog, null);

        builder.setCancelable(false);
        builder.setView(view);

        TextView textView = view.findViewById(R.id.textView);
        textView.setText(getTitle());

        dialog = builder.create();
        if(dialog.getWindow() != null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(activity.getApplicationContext().getColor(R.color.transparent)));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

}
