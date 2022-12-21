package com.neon.systemtaskmanager;

import android.app.ActivityManager;
import android.content.Context;

import static android.content.Context.ACTIVITY_SERVICE;

public class MemoryUtils {
    private static Context context = ContextContainer.context;
    public static double totalMemory()
    {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double totalMegs = mi.totalMem / 0x100000L;
        return totalMegs;
    }
    public static double freeMemory()
    {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;
        return availableMegs;
    }
    public static double usedMemory()
    {
        return totalMemory()-freeMemory();
    }
    public static double getRamPercent()
    {
        return usedMemory() / totalMemory()* 100.0;
    }
}
