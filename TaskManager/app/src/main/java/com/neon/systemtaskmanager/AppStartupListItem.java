package com.neon.systemtaskmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.io.IOException;

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
            if (enabled) {
                if(MainManager.isUserApp(pkg)) {
                    InitFiles.DisabledStartups.add(pkg);
                    ViewPagerAdapter.shared_adapter.refresh();
                } else {
                    final AlertDialog.Builder alert = new AlertDialog.Builder(ContextContainer.context);
                    alert.setTitle(R.string.warning);
                    alert.setMessage(R.string.system_warn);
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                InitFiles.DisabledStartups.add(pkg);
                                ViewPagerAdapter.shared_adapter.refresh();
                            }catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.show();
                }
            } else {
                    InitFiles.DisabledStartups.rem(pkg);
                ViewPagerAdapter.shared_adapter.refresh();
            }
        } catch(Exception e) {

            e.printStackTrace();
            Toast.makeText(MainActivity.one, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
