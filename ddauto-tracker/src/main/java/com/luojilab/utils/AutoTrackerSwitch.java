package com.luojilab.utils;

/**
 * Created by liushuo on 2017/8/25.
 */

public class AutoTrackerSwitch {
    private static AutoTrackerSwitch mAutoTrackSwitch;

    public static AutoTrackerSwitch getInstance() {
        if (mAutoTrackSwitch == null) {
            synchronized (AutoTrackerSwitch.class) {
                if (mAutoTrackSwitch == null) {
                    mAutoTrackSwitch = new AutoTrackerSwitch();
                }
            }
        }

        return mAutoTrackSwitch;
    }

    // 默认关闭自动打点配置，除非打点框架主动调用 enableAutoTrack
    private boolean mAutoTrackEnable = false;

    private AutoTrackerSwitch() {
    }

    public boolean isAutoTrackEnable() {
        return mAutoTrackEnable;
    }

    public void enableAutoTrack(boolean enable) {
        mAutoTrackEnable = enable;
    }
}
