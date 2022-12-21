package com.neon.systemtaskmanager;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.neon.systemtaskmanager.R;

import java.io.IOException;
import java.util.ArrayList;


public class StartupFragment extends Fragment {

    public StartupFragment() {
    }
    AppStartupListAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    EditText search;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_statup, container, false);
        progressBar = v.findViewById(R.id.progressBar);
        recyclerView = v.findViewById(R.id.recyrclerView);
        search = v.findViewById(R.id.search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerView.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
            new ThreadLoader().run(new Runnable() {
                @Override
                public void run() {
                    try {
                    adapter = new AppStartupListAdapter(getContext(), StartupUtils.getListOfAutoStartApplications());
                } catch (PackageManager.NameNotFoundException e) {
                    } catch (IOException e) {
                        e.printStackTrace();
                }
                }
            }, new Runnable(){

                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(adapter);
                }
            });
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    adapter.getFilter().filter(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        return v;
    }
}
