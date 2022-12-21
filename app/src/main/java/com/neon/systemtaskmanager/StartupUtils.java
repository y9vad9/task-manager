package com.neon.systemtaskmanager;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Build;
import android.os.Debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartupUtils {
    private static Context context = ContextContainer.context;
    public static List<AppStartupListItem> getListOfAutoStartApplications() throws PackageManager.NameNotFoundException, IOException {
        ArrayList<AppStartupListItem> list = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        final List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(
                PackageManager.GET_PERMISSIONS | PackageManager.GET_PROVIDERS
        );
        for(PackageInfo packageInfo : packageInfoList)
        {
            if(packageInfo.permissions != null) {
                ArrayList<PermissionInfo> permissions = new ArrayList<>();
                for (PermissionInfo permission : packageInfo.permissions)
                    permissions.add(permission);
                if (permissions.contains(packageManager.getPermissionInfo(Manifest.permission.RECEIVE_BOOT_COMPLETED, 0)))
                    ;
                list.add(new AppStartupListItem(packageManager.getApplicationIcon(packageInfo.packageName), packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(), packageInfo.packageName, !InitFiles.DisabledStartups.contains(packageInfo.packageName)));
            }
        }
        return list;
    }


}
