package com.neon.systemtaskmanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParsedProcessList {
    public ParsedProcess[] processes;
    public ParsedProcess[] old;
    public ParsedProcessList()
    {  }
    public void add(ParsedProcess process)
    {
        old = processes;
        processes = new ParsedProcess[processes.length+1];
        for(int i = 0; i!=old.length; i++)
        {
            processes[i] = old[i];
        }
    }
    public List<ParsedProcess> get() { return Arrays.asList(processes); }
}
