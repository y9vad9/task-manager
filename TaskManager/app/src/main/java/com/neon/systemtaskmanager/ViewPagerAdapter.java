package com.neon.systemtaskmanager;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.neon.systemtaskmanager.R;

//TODO!!! PerformanceFragment
public class ViewPagerAdapter extends FragmentPagerAdapter {
    public static ViewPagerAdapter shared_adapter;
    Context context;
    public void setContext(Context c)
    {
        context = c;
        shared_adapter = this;
    }
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
//            case 0:
//                return new ProcessesFragment();
//            case 1:
//                return new PerformanceFragment();
//            case 2:
//                return new StartupFragment();
            case 0:
                return new ProcessesFragment();
            case 1:
                return new StartupFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
//            case 0:
//                return context.getString(R.string.processes);
//            case 1:
//                return context.getString(R.string.performance);
//            case 2:
//                return context.getString(R.string.startup);
            case 0:
                return context.getString(R.string.processes);
            case 1:
                return context.getString(R.string.startup);
/*            case 2:
                return context.getString(R.string.scheduled);*/
            default:
                return "null";
        }
    }

    public void refresh()
    {
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
// POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return /*3*/2;
    }
}
