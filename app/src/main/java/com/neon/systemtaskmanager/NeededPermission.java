package com.neon.systemtaskmanager;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.neon.systemtaskmanager.R;

import org.sufficientlysecure.rootcommands.RootCommands;

public class NeededPermission {
    public static void PermissionTips(final Activity activity)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
//            boolean granted = false;
//            AppOpsManager opsManager = (AppOpsManager) activity.getSystemService(Context.APP_OPS_SERVICE);
//            int mode = opsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), activity.getPackageName());
//            if(mode == AppOpsManager.MODE_DEFAULT) granted = (activity.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
//                    else granted = (mode == AppOpsManager.MODE_ALLOWED);
//                    if(granted) {
//
//                    } else {
//                        AlertDialog dialog = new AlertDialog.Builder(activity)
//                                .setTitle(R.string.permission_tip)
//                                .setMessage(R.string.permission_tip_message)
//                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        activity.startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
//                                    }
//                                })
//                                .show();
//                    }
            AlertDialog dialog = new AlertDialog.Builder(activity)
                                .setTitle(R.string.permission_root_tip)
                                .setMessage(R.string.permission_root_tip_message)
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(RootCommands.rootAccessGiven())
                                        {
                                            SharedProps.write("root", "true");
                                            ViewPagerAdapter.shared_adapter.refresh();
                                        }
                                        else
                                        {
                                            SharedProps.write("root", "false");
                                        }
                                    }
                                })
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        SharedProps.write("root", "false");
                                    }
                                }).show();
        }
    }
}
