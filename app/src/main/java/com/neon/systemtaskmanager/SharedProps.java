package com.neon.systemtaskmanager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedProps {
    private static Context context = ContextContainer.context;
    public static void write(String key, String value)
    {
        final SharedPreferences preferences = context.getSharedPreferences("props", Context.MODE_PRIVATE);
        preferences.edit().putString(key, value).apply();
    }
    public static String read(String key)
    {
        final SharedPreferences preferences = context.getSharedPreferences("props", Context.MODE_PRIVATE);
        return preferences.getString(key, "undef");
    }
}
