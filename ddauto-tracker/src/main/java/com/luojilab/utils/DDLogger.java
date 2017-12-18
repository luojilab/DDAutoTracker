package com.luojilab.utils;

import android.util.Log;

import com.google.common.base.Strings;

/**
 * Author: liushuo
 * Date: 16/5/25
 */
public class DDLogger {
    public static final String TAG = "DDLogger";
    private static boolean isEnable = false;

    private DDLogger() {
    }

    public static boolean isEnable() {
        return isEnable;
    }

    public static void enableLogger(boolean enable) {
        isEnable = enable;
    }

    /**
     * 打印任意非String对象
     *
     * @param object
     */
    public static void v(Object object) {
        if (!isEnable) return;

        if (object == null) {
            Log.v(TAG, "empty object!");
            return;
        }

        Log.d(TAG, String.valueOf(object));
    }

    /**
     * 指定自定义tag，打印格式化字符串
     *
     * @param tag
     * @param message
     * @param args
     */
    public static void v(String tag, String message, Object... args) {
        if (!isEnable) return;

        if (tag == null) {
            v(DDLogger.TAG, message, args);
            return;
        }

        if (args == null || args.length == 0) {
            Log.v(tag, Strings.nullToEmpty(message));
            return;
        }

        Log.v(tag, String.format(Strings.nullToEmpty(message), args));
    }


    /**
     * 打印任意非String对象
     *
     * @param object
     */
    public static void d(Object object) {
        if (!isEnable) return;

        if (object == null) {
            Log.d(TAG, "empty object!");
            return;
        }

        Log.d(TAG, String.valueOf(object));
    }


    /**
     * 指定自定义tag，打印格式化字符串
     *
     * @param tag
     * @param message
     * @param args
     */
    public static void d(String tag, String message, Object... args) {
        if (!isEnable) return;

        if (tag == null) {
            d(TAG, message, args);
            return;
        }

        if (args == null || args.length == 0) {
            Log.d(tag, Strings.nullToEmpty(message));
            return;
        }

        Log.d(tag, String.format(Strings.nullToEmpty(message), args));
    }

    /**
     * 指定自定义tag，打印格式化字符串
     *
     * @param tag
     * @param message
     * @param args
     */
    public static void i(String tag, String message, Object... args) {
        if (!isEnable) return;

        if (tag == null) {
            i(TAG, message, args);
            return;
        }

        if (args == null || args.length == 0) {
            Log.i(tag, Strings.nullToEmpty(message));
            return;
        }

        Log.i(tag, String.format(Strings.nullToEmpty(message), args));
    }

    /**
     * 指定自定义tag，打印格式化字符串
     *
     * @param tag
     * @param message
     * @param args
     */
    public static void w(String tag, String message, Object... args) {
        if (!isEnable) return;

        if (tag == null) {
            w(TAG, message, args);
            return;
        }

        if (args == null || args.length == 0) {
            Log.w(tag, Strings.nullToEmpty(message));
            return;
        }


        Log.w(tag, String.format(Strings.nullToEmpty(message), args));
    }

    /**
     * 打印格式化字符串
     *
     * @param message
     */
    public static void e(String message) {
        if (!isEnable) return;

        Log.e(TAG, Strings.nullToEmpty(message));
    }

    /**
     * 指定自定义tag，打印格式化字符串
     *
     * @param tag
     * @param message
     * @param args
     */
    public static void e(String tag, String message, Object... args) {
        if (!isEnable) return;

        if (tag == null) {
            e(TAG, message, args);
            return;
        }

        if (args == null || args.length == 0) {
            Log.e(tag, Strings.nullToEmpty(message));
            return;
        }


        Log.e(tag, String.format(Strings.nullToEmpty(message), args));
    }

    /**
     * 打印格式化字符串
     *
     * @param message
     */
    public static void e(Throwable tr, String message) {
        if (!isEnable) return;

        Log.e(TAG, Strings.nullToEmpty(message));
        Log.e(TAG, String.valueOf(tr));
    }

    /**
     * 指定自定义tag，打印格式化字符串
     *
     * @param tag
     * @param message
     * @param args
     */
    public static void e(String tag, Throwable tr, String message, Object... args) {
        if (!isEnable) return;

        if (tag == null) {
            e(TAG, tr, message, args);
            return;
        }

        if (args == null || args.length == 0) {
            Log.e(tag, Strings.nullToEmpty(message));
            Log.e(tag, String.valueOf(tr));
            return;
        }

        Log.e(tag, String.format(Strings.nullToEmpty(message), args));
        Log.e(tag, String.valueOf(tr));
    }

}