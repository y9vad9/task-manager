package com.neon.systemtaskmanager;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.neon.systemtaskmanager.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProcessesFragment extends Fragment {
    public static ProcessesFragment shared_fragment;
    public ProcessesFragment() {
        shared_fragment = this;
    }
    ProgressBar progressBar;
    RecyclerView recyclerView;
    AppListAdapter adapter;
    Button rootButton;
    PieView pieView;
    TextView total;
    TextView used;
    TextView free;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_processes, container, false);

        total = v.findViewById(R.id.textView_total);
        used = v.findViewById(R.id.textView_used);
        free = v.findViewById(R.id.textView_free);
        total.setText(((int)MemoryUtils.totalMemory())+"MB");
        used.setText(((int)MemoryUtils.usedMemory())+"MB");
        free.setText(((int)MemoryUtils.freeMemory())+"MB");

        pieView = v.findViewById(R.id.pieView);
        pieView.setPercentageBackgroundColor(getResources().getColor(R.color.colorAccent));
        pieView.setMainBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        pieView.setPercentage((float)MemoryUtils.getRamPercent());
        PieAngleAnimation animation = new PieAngleAnimation(pieView);
        animation.setDuration(1000); //This is the duration of the animation in millis
        pieView.startAnimation(animation);
        progressBar = v.findViewById(R.id.progressBar);
        rootButton = v.findViewById(R.id.rootButton);
        rootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NeededPermission.PermissionTips(getActivity());
            }
        });
        recyclerView = v.findViewById(R.id.recyrclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setVisibility(View.GONE);
        if(SharedProps.read("root").equals("true")||Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
        {
            Toast.makeText(getContext(), R.string.root_process_loading, Toast.LENGTH_SHORT).show();
            new ThreadLoader().run(new Runnable() {
                @Override
                public void run() {
                    try {
                        adapter = new AppListAdapter(getContext(), ProcessesUtils.getListOfRunningApplications());
                    } catch (PackageManager.NameNotFoundException e) {
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                        Toast.makeText(MainActivity.one, e.toString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.one, e.toString(), Toast.LENGTH_LONG).show();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
            }, new Runnable() {

                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adapter);
                }
            });
        }
        else {
                    progressBar.setVisibility(View.GONE);
                    rootButton.setVisibility(View.VISIBLE);
                    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
                        recyclerView.setVisibility(View.VISIBLE);
                        rootButton.setVisibility(View.GONE);
                    }
        }
        return v;
    }
    public void refreshPie()
    {
        total.setText(((int)MemoryUtils.totalMemory())+"MB");
        used.setText(((int)MemoryUtils.usedMemory())+"MB");
        free.setText(((int)MemoryUtils.freeMemory())+"MB");
        pieView.setPercentage((float)MemoryUtils.getRamPercent());
        PieAngleAnimation animation = new PieAngleAnimation(pieView);
        animation.setDuration(500); //This is the duration of the animation in millis
        pieView.startAnimation(animation);
    }
}
