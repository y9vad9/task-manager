package com.neon.systemtaskmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.io.IOException;

import static com.neon.systemtaskmanager.ContextContainer.activity;

public class AppStartupListItem {
    public Drawable icon;
    public String title;
    public String pkg;
    public boolean enabled;
    public AppStartupListItem(Drawable icon, String title, String pkg, boolean enabled)
    {
        this.icon = icon;
        this.title = title;
        this.pkg = pkg;
        this.enabled = enabled;

    }
    public void toggle()
    {
        try {
            AppTool appTool = new AppTool(pkg,title,icon,activity);
            appTool.showInfo(new AppTool.Listeners() {
                @Override
                public void onAppEnableChanged() {
                    ViewPagerAdapter.shared_adapter.refresh();
                }
            });
        } catch(Exception e) {

            e.printStackTrace();
            Toast.makeText(MainActivity.one, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
