package com.neon.systemtaskmanager;

import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/taskmanager/errors/error.txt"),toStr(e));
            MainActivity.one.finish();
        } catch (Exception ee) {
            ee.printStackTrace();
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }
    private static String toStr(Throwable throwable) {
        StringWriter stackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTrace));
        return stackTrace.toString();
    }
}
