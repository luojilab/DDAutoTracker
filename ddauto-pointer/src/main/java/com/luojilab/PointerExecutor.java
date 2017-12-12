package com.luojilab;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.annotation.NonNull;

/**
 * user liushuo
 * date 2017/4/28
 */
class PointerExecutor extends HandlerThread {
    private static final String TAG = "PointerExecutor";

    private static Handler sHandler;

    private PointerExecutor(String name, int priority) {
        super(name, priority);
    }

    @NonNull
    public static Handler getHandler() {
        if (sHandler == null) {
            HandlerThread thread = new PointerExecutor(TAG, Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();

            sHandler = new Handler(thread.getLooper());
        }

        return sHandler;
    }

}
