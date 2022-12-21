package com.neon.systemtaskmanager;

import java.io.IOException;

public class RootUtils {
    public static void suExec(String cmd) throws IOException { Runtime.getRuntime().exec("su -c " + cmd); }
    public static void grantPermission(String pkg, String permission) throws IOException {
        Runtime.getRuntime().exec("su -c \"pm grant "+pkg+" "+permission+"\"");
    }
}
