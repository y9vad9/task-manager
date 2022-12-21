package com.neon.systemtaskmanager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.neon.systemtaskmanager.R;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

/**
 * A placeholder fragment containing a simple view.
 */
public class PerformanceFragment extends Fragment {

    public PerformanceFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_performance, container, false);
        return v;
    }
}
