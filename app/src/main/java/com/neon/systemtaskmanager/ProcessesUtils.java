package com.neon.systemtaskmanager;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Debug;
import android.support.v4.util.SparseArrayCompat;
import android.util.Log;
import android.widget.Toast;

import org.sufficientlysecure.rootcommands.RootCommands;
import org.sufficientlysecure.rootcommands.Shell;
import org.sufficientlysecure.rootcommands.command.SimpleCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeoutException;

public class ProcessesUtils {
    private static Context context = ContextContainer.context;

    public static List<AppListItem> getListOfRunningApplications() throws PackageManager.NameNotFoundException, IOException, TimeoutException {
        ArrayList<AppListItem> list = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.LOLLIPOP) {
//            UsageStatsManager usm = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
//            long time = System.currentTimeMillis();
//            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
//            if (appList != null && appList.size() == 0) {
//
//            }
//            if (appList != null && appList.size() > 0) {
//                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
//                for (UsageStats usageStats : appList) {
//                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
//                }
//                if (mySortedMap != null && !mySortedMap.isEmpty()) {
//                    List<Long> templist = new ArrayList<>(mySortedMap.keySet());
//                    Collections.sort(templist, Collections.reverseOrder());
//                    Set<Long> resultSet = new LinkedHashSet<>(templist);
//                    for(long i : resultSet) {
//                        try {
//                            ApplicationInfo info = context.getPackageManager().getApplicationInfo(mySortedMap.get(i).getPackageName(), 0);
//                            list.add(new AppListItem(context.getPackageManager().getApplicationIcon(info), context.getPackageManager().getApplicationLabel(info).toString(), info.packageName, 0, new Long[]{(long)info.uid}));
//                        } catch (Exception e) {
//
//                        }
//                    }
//                }
//            }


                HashMap<Integer, ParsedProcess> processList = new HashMap<>();
                SimpleCommand command = new SimpleCommand("/data/data/"+context.getPackageName()+"/files/ps -A | awk '{res=$1 FS $2 FS $9; print res}'");
                SUConsole.shell.add(command).waitForFinish();
                String[] lines = command.getOutput().split("\n");
                lines[0] = null;
                lines[1] = null;
                for (String line : lines) {
                    if (line != null) {
                        final ParsedProcess process = new ParsedProcess(line);
                        ApplicationInfo applicationInfo = null;
                        boolean isAndroidApp = true;
                        try {
                            applicationInfo = context.getPackageManager()
                                    .getApplicationInfo(context.getPackageManager().getNameForUid(process.uid), 0);
                        } catch (PackageManager.NameNotFoundException e) {
                            isAndroidApp = false;
                        }
                        process.applicationInfo = applicationInfo;
                        if (!processList.containsKey(process.uid) && isAndroidApp && applicationInfo.packageName.equals(process.name))
                        {
                            processList.put(process.uid, process);
                        }
                    }
                }
                for (int uid : processList.keySet()) {
                    ApplicationInfo applicationInfo = processList.get(uid).applicationInfo;
                    Drawable applicationIcon = context.getPackageManager().getApplicationIcon(applicationInfo);
                    String applicationLabel = context.getPackageManager().getApplicationLabel(applicationInfo).toString();
                    String applicationPackage = applicationInfo.packageName;
                    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    final Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[]{processList.get(uid).pid})[0];
                    double used = memoryInfo.getTotalPrivateDirty();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        used += memoryInfo.getTotalPrivateClean();
                    ArrayList<Long> pids = new ArrayList<>();
                    //for(ParsedProcess parsedProcess : processList.get(uid).get()) pids.add(parsedProcess.pid);
                    pids.add((long) processList.get(uid).pid);

                    list.add(new AppListItem(applicationIcon, applicationLabel, applicationPackage, used/1000l, pids.toArray(new Long[pids.size()])));
                }
            return list;
        } else {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
                final ApplicationInfo info = context.getPackageManager().getApplicationInfo(processInfo.processName, PackageManager.GET_META_DATA);
                final Debug.MemoryInfo memoryInfo = am.getProcessMemoryInfo(new int[]{processInfo.pid})[0];
                double used = memoryInfo.getTotalPrivateDirty();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    used += memoryInfo.getTotalPrivateClean();
                list.add(new AppListItem(context.getPackageManager().getApplicationIcon(info), context.getPackageManager().getApplicationLabel(info).toString(), processInfo.processName, used / 1000l, new Long[]{(long) processInfo.pid}));

            }
        }
        return list;
    }
    public static List<AppListItem> ROOTgetListOfRunningApplications() {
        final ArrayList<AppListItem> list = new ArrayList<>();
        try {
            HashMap<Integer, ParsedProcess> processList = new HashMap<>();
            SimpleCommand command = new SimpleCommand("/data/data/"+context.getPackageName()+"/files/ps -A | awk '{res=$1 FS $2 FS $9; print res}'");
            SUConsole.shell.add(command).waitForFinish();
            String[] lines = command.getOutput().split("\n");
            lines[0] = null;
            lines[1] = null;
            for (String line : lines) {
                if (line != null) {
                    final ParsedProcess process = new ParsedProcess(line);
                    ApplicationInfo applicationInfo = null;
                    boolean isAndroidApp = true;
                    try {
                        applicationInfo = context.getPackageManager()
                                .getApplicationInfo(context.getPackageManager().getNameForUid(process.uid), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        isAndroidApp = false;
                    }
                    if (!processList.containsKey(process.uid) && isAndroidApp)
                        processList.put(process.uid, process);
                    process.applicationInfo = applicationInfo;
                    //processList.get(process.uid).add(process);
                }
            }
            for (int uid : processList.keySet()) {
                ApplicationInfo applicationInfo = processList.get(uid).applicationInfo;
                Drawable applicationIcon = context.getPackageManager().getApplicationIcon(applicationInfo);
                String applicationLabel = context.getPackageManager().getApplicationLabel(applicationInfo).toString();
                String applicationPackage = applicationInfo.packageName;
                int used = 0;
                ArrayList<Long> pids = new ArrayList<>();
                //for(ParsedProcess parsedProcess : processList.get(uid).get()) pids.add(parsedProcess.pid);
                pids.add((long) processList.get(uid).pid);
                list.add(new AppListItem(applicationIcon, applicationLabel, applicationPackage, used, pids.toArray(new Long[pids.size()])));
            }
        } catch (Exception e) {
            new AlertDialog.Builder(MainActivity.one).setTitle(String.valueOf(e.getStackTrace()[0].getLineNumber())).setMessage(e.toString()).show();
            e.printStackTrace();
        }

//        Command command = new Command(0, "ps -A | awk '{res=$1 FS $2; print res}'")
//        {
//            @Override
//            public void commandOutput(int id, String line) {
//                new AlertDialog.Builder(MainActivity.one).setMessage(line).show();
//                super.commandOutput(id, line);
//            }
//        };
//        try {
//            RootTools.getShell(true).add(command);
//        }catch (IOException | RootDeniedException | TimeoutException ex) {
//            ex.printStackTrace();
//        }

//        HashMap<String, ArrayList<Long>> processes = new HashMap<>();
//        try {
//            String line;
//            Runtime.getRuntime().exec("su");
//            Process p = Runtime.getRuntime().exec("./data/data/com.neon.systemtaskmanager/files/processes");
//            BufferedReader input =
//                    new BufferedReader(new InputStreamReader(p.getInputStream()));
//            line = input.readLine();
//            while ((line = input.readLine()) != null) {
//
//                Log.d("AAA", line);
//                String[] str = line.split(" ");
//                if (!processes.containsKey(str[0])) {
//                    processes.put(str[0], new ArrayList<Long>());
//                }
//                processes.get(str[0]).add(Long.parseLong(str[1]));
//                new AlertDialog.Builder(MainActivity.one).setMessage(line).show();
//            }
//            input.close();
//        } catch (Exception err) {
//            Toast.makeText(MainActivity.one, err.toString(), Toast.LENGTH_LONG).show();
//        }
//        for(String uid : processes.keySet()) {
//            try {
//                ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageManager().getNameForUid((int) UID(uid)), 0);
//                Long pids[] = (Long[]) processes.get(uid).toArray();
//                list.add(new AppListItem(context.getPackageManager().getApplicationIcon(info), context.getPackageManager().getApplicationLabel(info).toString(), info.packageName, 0, pids));
//            } catch (Exception e) {
//                new AlertDialog.Builder(MainActivity.one).setTitle(String.valueOf(uid)).setMessage(e.toString()).show();
//                Toast.makeText(MainActivity.one, e.toString(), Toast.LENGTH_LONG).show();
//            }
//        }
        return list;
    }

    public static double killBackgroundProcesses(double freeMemory) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        final List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(
                PackageManager.GET_META_DATA
        );
        for (PackageInfo packageInfo : packageInfoList) {
            am.killBackgroundProcesses(packageInfo.packageName);
        }
        return MemoryUtils.freeMemory() - freeMemory;
    }

    public static int UID(String username) {
        int uid = 0;
        try {
            SimpleCommand command = new SimpleCommand("id -u " + username);
            SUConsole.shell.add(command).waitForFinish();
            uid = Integer.parseInt(command.getOutput().split("\n")[1]);
        } catch (Exception err) {
            Toast.makeText(MainActivity.one, err.toString() + "d", Toast.LENGTH_LONG).show();
        }
        return uid;
    }
    public static void rootKill(String _package)
    {
        try {
            SUConsole.shell.add(new SimpleCommand("killall -9 "+_package)).waitForFinish();
        } catch (Exception e) {
        }
    }
}
