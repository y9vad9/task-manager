package com.neon.systemtaskmanager;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RawUtils {
    public static void copyFiletoExternalStorage(int resourceId, String resourceName, boolean executable){
        String path = resourceName;
        try{
            InputStream in = ContextContainer.context.getResources().openRawResource(resourceId);
            FileOutputStream out = null;
            out = new FileOutputStream(path);
            byte[] buff = new byte[1000];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
                new File(path).setExecutable(true);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
