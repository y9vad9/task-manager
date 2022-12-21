package com.neon.systemtaskmanager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class ThreadLoader {
    private Runnable after;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x2:
                    after.run();
            }
            super.handleMessage(msg);
        }
    };

    public void run(final Runnable runnable, Runnable _after)
    {
        after = _after;
        new Thread(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                handler.sendEmptyMessage(0x2);
            }
        }).start();
    }

}
