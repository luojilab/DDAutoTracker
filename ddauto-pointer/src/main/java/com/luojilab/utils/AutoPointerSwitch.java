package com.luojilab.utils;

/**
 * Created by liushuo on 2017/8/25.
 */

public class AutoPointerSwitch {
    private static AutoPointerSwitch mAutoPointerSwitch;

    public static AutoPointerSwitch getInstance() {
        if (mAutoPointerSwitch == null) {
            synchronized (AutoPointerSwitch.class) {
                if (mAutoPointerSwitch == null) {
                    mAutoPointerSwitch = new AutoPointerSwitch();
                }
            }
        }

        return mAutoPointerSwitch;
    }

    // 默认关闭自动打点配置，除非打点框架主动调用 enableAutoPoint
    private boolean mAutoPointEnable = false;

    private AutoPointerSwitch() {
    }

    public boolean isAutoPointEnable() {
        return mAutoPointEnable;
    }

    public void enableAutoPoint(boolean enable) {
        mAutoPointEnable = enable;
    }
}
