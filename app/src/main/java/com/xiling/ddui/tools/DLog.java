package com.xiling.ddui.tools;

import android.util.Log;

import com.xiling.ddui.config.AppConfig;

public class DLog {

    static boolean isLogOpen = AppConfig.DEBUG;

    private static final String TAG = "D-LOG";

    public static void e(String text) {
        e(TAG, text);
    }

    public static void d(String text) {
        d(TAG, text);
    }

    public static void w(String text) {
        w(TAG, text);
    }

    public static void i(String text) {
        i(TAG, text);
    }

    public static void e(String tag, String text) {
        if (isLogOpen) {
            Log.e(tag, text);
        }
    }

    public static void d(String tag, String text) {
        if (isLogOpen) {
            Log.d(tag, text);
        }
    }

    public static void w(String tag, String text) {
        if (isLogOpen) {
            Log.w(tag, text);
        }
    }

    public static void i(String tag, String text) {
        if (isLogOpen) {
            Log.i(tag, text);
        }
    }

}
