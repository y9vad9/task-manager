package com.neon.systemtaskmanager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatDialog;
import android.view.View;

public class LoadingDialog {
    private Activity a;
    private AppCompatDialog dialog;
    public LoadingDialog(Activity activity) {
        a = activity;
        dialog = new AppCompatDialog(activity);
    }
    private Activity getActivity() {
        return a;
    }
    public void show() {
        if(dialog.isShowing())
            return;
        else {
            dialog = new AppCompatDialog(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.layout_loading, null);
            dialog.setContentView(view);
            dialog.getWindow().setBackgroundDrawable(new GradientDrawable() {{
                setColor(Color.TRANSPARENT);
                setCornerRadius(0);
            }});
            dialog.setCancelable(false);
            dialog.show();
        }
    }
    public void dismiss() {
        if(dialog == null)
            return;
        else
            dialog.dismiss();
    }
}
