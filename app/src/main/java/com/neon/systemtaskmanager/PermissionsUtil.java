package com.neon.systemtaskmanager;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static com.neon.systemtaskmanager.ContextContainer.activity;
import static com.neon.systemtaskmanager.ContextContainer.context;

public class PermissionsUtil {
    static ArrayList<String> getGrantedPermissions(final String appPackage) {
        ArrayList<String> granted = new ArrayList<String>();
        try {
            PackageInfo pi = MainActivity.one.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
                }
            }
        } catch (Exception e) {
        }
        return granted;
    }
    String getSize(String packagee) {
        try {
            final PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packagee, 0);
            File file = new File(applicationInfo.publicSourceDir);
            int size = (int) file.length();
            return String.valueOf(size) + " BYTES";
        } catch (Exception e) {
            return "Unnamed";
        }
    }
    static String[] getPermission(String pkg) {
        ArrayList<String> granted = new ArrayList<String>();
        try {
            PackageInfo pi = MainActivity.one.getPackageManager().getPackageInfo(pkg, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    granted.add(pi.requestedPermissions[i]);
                }
            }
        } catch (Exception e) {
        }
        String[] stringArray = granted.toArray(new String[0]);
        return stringArray;
    }
    public static String getInfoAboutPerm(String perm) {
        if (perm.equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            return activity.getString(R.string.perm_write_external_storage);
        } else if (perm.equals(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return activity.getString(R.string.perm_read_external_storage);
        } else if (perm.equals(Manifest.permission.INTERNET)) {
            return activity.getString(R.string.perm_internet);
        } else if (perm.equals(Manifest.permission.BLUETOOTH)) {
        return activity.getString(R.string.perm_bluetooth);
        } else if (perm.equals(Manifest.permission.NFC)) {
            return activity.getString(R.string.perm_nfc);
        } else if (perm.equals(Manifest.permission.RECEIVE_BOOT_COMPLETED)) {
            return activity.getString(R.string.perm_recieve_boot_complete);
        } else {
            return activity.getString(R.string.no_data);
        }
    }
}
