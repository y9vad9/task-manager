package com.neon.systemtaskmanager;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;

public class MainManager {
    static boolean isUserApp(String _package) {
        try {
            ApplicationInfo ai = ContextContainer.context.getPackageManager().getApplicationInfo(_package, 0);
            int mask = ApplicationInfo.FLAG_SYSTEM | ApplicationInfo.FLAG_UPDATED_SYSTEM_APP;
            return (ai.flags & mask) == 0;
        }catch (PackageManager.NameNotFoundException e) {
            System.out.println(e.getMessage());
            return true;
        }
    }
}
