package com.neon.systemtaskmanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class YesNoAlertDialog {
    public static Activity activity;
    public static void dialog(String title, String message, final Runnable action) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action.run();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
