package com.neon.systemtaskmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import org.sufficientlysecure.rootcommands.RootCommands;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static ViewPagerAdapter pagerAdapter;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fab;
    SwipeRefreshLayout multiSwipeRefreshLayout;
    public static Activity one;
    ViewPagerAdapter adapter;
    private double mb = 0;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
        one = this;
        ContextContainer.activity = this;
        ContextContainer.context = getApplicationContext();
        setContentView(R.layout.activity_main);
        init();
        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                clean();
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().setNavigationBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }
        initTabs();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     switch (requestCode) {
         case 1:
             ViewPagerAdapter.shared_adapter.refresh();
             return;
     }
    }
    private void init()
    {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        new ThreadLoader().run(new Runnable() {
            @Override
            public void run() {
                try {
                    InitFiles.init(getPackageName());
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ContextContainer.context, "ERROR!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.one, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        });
        if(SharedProps.read("root").equals("undef")) NeededPermission.PermissionTips(this);
        if(SharedProps.read("root").equals("true")&& !RootCommands.rootAccessGiven()) {
            SharedProps.write("root", "false");
            NeededPermission.PermissionTips(this);
        }
        YesNoAlertDialog.activity = this;
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        multiSwipeRefreshLayout = findViewById(R.id.swiperefresh);
    }
    private void initTabs()
    {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.setContext(this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        multiSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.refresh();
                multiSwipeRefreshLayout.setRefreshing(false);
            }
        });
        viewPager.addOnPageChangeListener( new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled( int position, float v, int i1 ) {
            }

            @Override
            public void onPageSelected( int position ) {
                if(position == 0) fab.show(); else fab.hide();
            }

            @Override
            public void onPageScrollStateChanged( int state ) {
                enableDisableSwipeRefresh( state == ViewPager.SCROLL_STATE_IDLE );
            }
        } );
    }
    private void enableDisableSwipeRefresh(boolean enable) {
        if (multiSwipeRefreshLayout != null) {
            multiSwipeRefreshLayout.setEnabled(enable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.action_refresh:
                adapter.refresh();
                break;
            case R.id.reportbug:
                Intent i = new Intent();
                i.setData(Uri.parse("mailto:support@neonteam.net"));
                i.setAction(Intent.ACTION_VIEW);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showMessage(String m){
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }
    private void clean() {

        final LoadingDialog loadingDialog = new LoadingDialog(MainActivity.one);
        loadingDialog.show();

        new ThreadLoader().run(new Runnable() {
            @Override
            public void run() {

                try {
                    double currentMemory = MemoryUtils.freeMemory();
                    mb = ProcessesUtils.killBackgroundProcesses(currentMemory);
                    if (mb < 0) mb = 0;
                } catch (Exception e) {
                    clean();
                }

            }
        }, new Runnable() {
            @Override
            public void run() {

                loadingDialog.dismiss();
                Snackbar.make(fab, getString(R.string.cleaned) + " " + (int) mb + "MB", Snackbar.LENGTH_LONG).show();
                adapter.refresh();

            }
        });
    }
}
