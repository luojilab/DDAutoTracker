package com.luojilab.utils;

/**
 * Created by liushuo on 2017/8/25.
 */

public class AutoTrackerSwitch {
    private static AutoTrackerSwitch mAutoPointerSwitch;

    public static AutoTrackerSwitch getInstance() {
        if (mAutoPointerSwitch == null) {
            synchronized (AutoTrackerSwitch.class) {
                if (mAutoPointerSwitch == null) {
                    mAutoPointerSwitch = new AutoTrackerSwitch();
                }
            }
        }

        return mAutoPointerSwitch;
    }

    // 默认关闭自动打点配置，除非打点框架主动调用 enableAutoPoint
    private boolean mAutoPointEnable = false;

    private AutoTrackerSwitch() {
    }

    public boolean isAutoPointEnable() {
        return mAutoPointEnable;
    }

    public void enableAutoPoint(boolean enable) {
        mAutoPointEnable = enable;
    }
}
