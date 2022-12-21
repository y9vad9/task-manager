package com.neon.systemtaskmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import org.sufficientlysecure.rootcommands.command.SimpleCommand;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class AppListItem {
    public Drawable icon;
    public String title;
    public String pkg;
    public double used;
    public Long[] pids;
    public AppListItem(Drawable icon, String title, String pkg, double used, Long[] pids)
    {
        this.icon = icon;
        this.title = title;
        this.pkg = pkg;
        this.used = used;
        this.pids = pids;
    }
    public void kill()
    {
        for(long pid : pids)
        android.os.Process.killProcess((int)pid);
    }
    public void rootKill(Context context)
    {
        new ThreadLoader().run(new Runnable() {
            @Override
            public void run() {

            }
        }, new Runnable() {
            @Override
            public void run() {

            }
        });
    }
}
