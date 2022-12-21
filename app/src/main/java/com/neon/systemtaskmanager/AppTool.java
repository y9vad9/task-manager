package com.neon.systemtaskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.michaelbel.bottomsheet.BottomSheet;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;

import static android.support.constraint.Constraints.TAG;
import static com.neon.systemtaskmanager.ContextContainer.context;


public class AppTool {
    private String pkg;
    private String appname;
    private Activity activity;
    private Drawable icon;
    public AppTool(String app_package, String appName,Drawable icon, Activity activity) {
        this.activity = activity;
        pkg = app_package;
        appname = appName;
        this.icon = icon;
    }
    private RecyclerView info;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public void showInfo(final Listeners listeners) {

        PackageManager packageManager = activity.getPackageManager();
        String titles[] = {activity.getString(R.string.app_size), activity.getString(R.string.app_type), activity.getString(R.string.app_ver), activity.getString(R.string.app_permission)};
        final String abouts[] = {getFileSize(getAppSize()), getAppType(), getVersion(), activity.getString(R.string.more)};
        ArrayList<View.OnClickListener> onClickListeners = new ArrayList<>();
        // TODO bad solution
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        onClickListeners.add(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] perms = PermissionsUtil.getPermission(pkg.trim());
                BottomSheet.Builder builder = new BottomSheet.Builder(activity);
                builder.setTitle(R.string.app_permission);
                builder.setItems(perms, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String data = perms[i];
                        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                        alert.setTitle(R.string.info);
                        alert.setMessage(PermissionsUtil.getInfoAboutPerm(data));
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alert.show();
                    }
                });
                builder.show();
            }
        });

        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(activity);
        View v = activity.getLayoutInflater().inflate(R.layout.dialog_app_info, null);
        bottomSheetDialog.setContentView(v);
        info = (RecyclerView) v.findViewById(R.id.info);
        TextView title = (TextView) v.findViewById(R.id.title);
        LinearLayout topbar = (LinearLayout) v.findViewById(R.id.topbar);
        ImageView iconn = (ImageView) v.findViewById(R.id.icon);
        final Switch turn = (Switch) v.findViewById(R.id.turn);
        try {
            if (!InitFiles.DisabledStartups.contains(pkg.trim()))
                turn.setChecked(true);
            else
                turn.setChecked(false);
        } catch (Exception e) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
        }
        iconn.setImageDrawable(icon);
        TextView pkga = (TextView) v.findViewById(R.id.pkg);
        title.setText(appname);
        pkga.setText(pkg);
        layoutManager = new LinearLayoutManager(activity);
        mAdapter = new AppToolInfoAdapter(titles, abouts, onClickListeners);
        info.setLayoutManager(layoutManager);
        info.setAdapter(mAdapter);
        topbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turn.performClick();
            }
        });
        turn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    if (b) {
                        InitFiles.DisabledStartups.rem(pkg.trim());
                    } else {
                        InitFiles.DisabledStartups.add(pkg.trim());
                    }
                    listeners.onAppEnableChanged();
                } catch (Exception e) {

                }
            }
        });
        bottomSheetDialog.show();
    }
    private long getAppSize () {
        try {
            final PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo = pm.getApplicationInfo(pkg, 0);
            File file = new File(applicationInfo.publicSourceDir);
            long size = file.length();
            return size;
        } catch (Exception e) {
            return 0;
        }
    }
    private static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    private String getAppType() {
        if(MainManager.isUserApp(pkg.trim())) {
            return activity.getString(R.string.app_type_user);
        } else {
            return activity.getString(R.string.app_type_system);
        }
    }
    public interface Listeners {
        void onAppEnableChanged();
    }
    private String getVersion() {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(pkg.trim(), 0);
            String version = pInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }


}