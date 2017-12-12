package com.example.liushuo.ddautopointer;

import android.app.Application;

import com.luojilab.AutoPointer;
import com.luojilab.init.AutoPointerInitializer;

/**
 * Created by liushuo on 2017/9/22.
 */

public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        AutoPointerInitializer.getInstance()
                .appContext(getApplicationContext())
                .serverEnvironment("测试");

        AutoPointer.enableAutoPoint(true);
        AutoPointer.enableDebugPoint(true);
    }
}
