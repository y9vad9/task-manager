package com.neon.systemtaskmanager;

import org.sufficientlysecure.rootcommands.Shell;

import java.io.IOException;

public class SUConsole {
    public static Shell shell;

    static {
        try {
            shell = Shell.startRootShell();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
