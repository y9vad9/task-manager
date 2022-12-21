package com.neon.systemtaskmanager;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(Intent.ACTION_SHUTDOWN));
        else if(action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            try
            {
                InitFiles.init(context.getPackageName());
                ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                PackageManager packageManager = context.getPackageManager();
                final List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(PackageManager.GET_META_DATA);
                for (PackageInfo packageInfo : packageInfoList)
                    if (InitFiles.DisabledStartups.contains(packageInfo.packageName))
                    {
                        am.killBackgroundProcesses(packageInfo.packageName);
                        if(SharedProps.read("root").equals("true"))
                            ProcessesUtils.rootKill(packageInfo.packageName);
                    }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

    }
}
