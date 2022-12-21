package com.neon.systemtaskmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.util.ArrayList;

public class ParsedProcess {
    int uid;
    int pid;
    public ArrayList<Integer> otherPids;
    String name;
    ApplicationInfo applicationInfo;
    public ParsedProcess(String line)
    {
        String[] input = line.split(" ");
        uid = ProcessesUtils.UID(input[0]);
        pid = Integer.parseInt(input[1]);
        name = input[2];
        otherPids = new ArrayList<>();
    }
}
