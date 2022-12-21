package com.neon.systemtaskmanager;

import com.neon.systemtaskmanager.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class InitFiles {
    public static FileInputStream inputStream;
    public static FileOutputStream outputStream;
    public static File disabledStartups;
    public static BufferedReader bufferedReader;
    public static void init(String packagename) throws IOException {
        File folder = new File("/data/data/"+packagename+"/files/");
        folder.mkdir();
        disabledStartups = new File(folder, "disabled_startups.list");
        if(!disabledStartups.exists()) disabledStartups.createNewFile();
        inputStream = new FileInputStream(disabledStartups);
        outputStream = new FileOutputStream(new File("/data/data/com.neon.taskmanager/files/processes"));
        outputStream.write("ps -A | awk '{res=$1 FS $2; print res}'".getBytes());
        outputStream.close();
        new File("/data/data/com.neon.taskmanager/files/processes").setExecutable(true);
        if(!new File("/data/data/"+ContextContainer.context.getPackageName()+"/files/ps").exists())RawUtils.copyFiletoExternalStorage(R.raw.ps, "/data/data/"+ContextContainer.context.getPackageName()+"/files/ps", true);
        if(!new File("/data/data/"+ContextContainer.context.getPackageName()+"/files/awk").exists())RawUtils.copyFiletoExternalStorage(R.raw.awk, "/data/data/"+ContextContainer.context.getPackageName()+"/files/awk", true);
    }
    public static class DisabledStartups
    {
        public static List<String> get() throws IOException {
            ArrayList<String> list = new ArrayList<>();
            inputStream = new FileInputStream(disabledStartups);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = bufferedReader.readLine();
            while(line != null)
            {
                list.add(line);
                line = bufferedReader.readLine();
            }
            inputStream.close();
            bufferedReader.close();
            return list;
        }
        public static void add(String pkg) throws IOException {
            List<String> list = get();
            if(!list.contains(pkg)) list.add(pkg);
            wrt(list);
        }
        public static void rem(String pkg) throws IOException {
            List<String> list = get();
            list.remove(pkg);
            wrt(list);
        }
        public static void clr() throws IOException {
            disabledStartups.delete();
            disabledStartups.createNewFile();
        }
        public static void wrt(List<String> pkgs) throws IOException {
            clr();
            outputStream = new FileOutputStream(disabledStartups);
            for(String pkg : pkgs)
            {
                outputStream.write((pkg+"\n").getBytes());
            }
            outputStream.close();
        }
        public static boolean contains(String pkg) throws IOException {
            return get().contains(pkg);
        }
    }
}
