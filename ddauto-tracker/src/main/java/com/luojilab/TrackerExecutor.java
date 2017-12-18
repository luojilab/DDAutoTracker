package com.luojilab;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.support.annotation.NonNull;

/**
 * user liushuo
 * date 2017/4/28
 */
class TrackerExecutor extends HandlerThread {
    private static final String TAG = "TrackerExecutor";

    private static Handler sHandler;

    private TrackerExecutor(String name, int priority) {
        super(name, priority);
    }

    @NonNull
    public static Handler getHandler() {
        if (sHandler == null) {
            HandlerThread thread = new TrackerExecutor(TAG, Process.THREAD_PRIORITY_BACKGROUND);
            thread.start();

            sHandler = new Handler(thread.getLooper());
        }

        return sHandler;
    }

}
