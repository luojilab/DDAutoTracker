package com.example.liushuo.ddautopointer;

import android.app.Application;

import com.luojilab.AutoTracker;
import com.luojilab.init.AutoTrackerInitializer;

/**
 * Created by liushuo on 2017/9/22.
 */

public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        AutoTrackerInitializer.getInstance()
                .appContext(getApplicationContext())
                .serverEnvironment("测试");

        AutoTracker.enableAutoPoint(true);
        AutoTracker.enableDebugPoint(true);
    }
}
