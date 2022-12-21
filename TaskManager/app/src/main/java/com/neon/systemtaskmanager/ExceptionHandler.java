package com.neon.systemtaskmanager;

import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            File f = new File("/data/data/com.neon.taskmanager/files/error.txt");
            f.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(f);
            outputStream.write((e.toString()).getBytes());
            outputStream.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(MainActivity.one, e1.toString(), Toast.LENGTH_LONG).show();
        }

    }
}
