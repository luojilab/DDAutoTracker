package com.example.liushuo.ddautotracker;

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

        AutoTracker.enableAutoTrack(true);
        AutoTracker.enableDebugTrack(true);
    }
}
