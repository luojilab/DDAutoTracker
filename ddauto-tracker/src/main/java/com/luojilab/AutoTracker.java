package com.luojilab;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.PopupWindow;

import com.google.common.base.Preconditions;
import com.luojilab.init.AutoTrackerInitializer;
import com.luojilab.utils.AutoTrackerSwitch;
import com.luojilab.view.DDDecorView;

/**
 * user liushuo
 * date 2017/4/6
 */

public class AutoTracker {
    public static final String TAG = "AutoTracker";

    public enum SERVER_ENV {
        TEST("测试环境"),
        SIMULATION("仿真环境"),
        ONLINE("线上环境");

        private String mDes;

        SERVER_ENV(String des) {
            this.mDes = des;
        }

        @Override
        public String toString() {
            return mDes;
        }

    }

    private static SERVER_ENV sServerEnv;

    static {
        initServerEnv();
    }

    /*是否发送调试埋点*/
    private static boolean sDebugPoint = !AutoTracker.isOnlineEnv();

    /*自动打点功能是否启用,全局有效,默认值为false(小部分用户测试稳定性)*/
    private static boolean sEnableAutoTrack = false;

    private AutoTracker() {
    }

    private static void initServerEnv() {
        String type = AutoTrackerInitializer.getInstance().getServerEnvironment();
        switch (type) {
            case "线上":
                sServerEnv = SERVER_ENV.ONLINE;
                break;
            case "仿真":
                sServerEnv = SERVER_ENV.SIMULATION;
                break;
            case "测试":
                sServerEnv = SERVER_ENV.TEST;
                break;
            default:
                sServerEnv = SERVER_ENV.ONLINE;
                break;
        }
    }

    public static boolean isOnlineEnv() {
        return sServerEnv == SERVER_ENV.ONLINE;
    }

    @NonNull
    public static SERVER_ENV getServerEnv() {
        return sServerEnv;
    }

    public static boolean isAutoTrackEnable() {
        return sEnableAutoTrack;
    }

    public static void enableAutoTrack(boolean enable) {
        sEnableAutoTrack = enable;

        AutoTrackerSwitch.getInstance().enableAutoTrack(enable);
    }

    public static boolean isDebugPoint() {
        return sDebugPoint;
    }

    /**
     * 开启调试埋点功能(埋点发送全量数据)
     *
     * @param enable
     */
    public static void enableDebugTrack(boolean enable) {
        sDebugPoint = enable;
    }

    @NonNull
    public static DataConfigureImp wrapWindowCallback(@NonNull Window window) {
        Preconditions.checkNotNull(window, "Window is null in AutoTracker.wrapWindowCallback()");

        Window.Callback callback = window.getCallback();
        View decorView = window.getDecorView();

        WindowCallbackWrapper wrapper = new WindowCallbackWrapper(decorView, callback);
        window.setCallback(wrapper);
        return wrapper;
    }

    @NonNull
    public static DataConfigureImp wrapWindowCallback(@NonNull Activity activity) {
        Preconditions.checkNotNull(activity, "activity is null in AutoTracker.wrapWindowCallback()");

        Window window = activity.getWindow();

        return wrapWindowCallback(window);
    }

    @NonNull
    public static DataConfigureImp wrapWindowCallback(@NonNull Dialog dialog) {
        Preconditions.checkNotNull(dialog, "dialog is null in AutoTracker.wrapWindowCallback()");

        Window window = dialog.getWindow();

        return wrapWindowCallback(window);
    }

    @NonNull
    public static DataConfigureImp wrapWindowCallback(@NonNull PopupWindow popup) {
        Preconditions.checkNotNull(popup, "pop window is null in AutoTracker.wrapWindowCallback()");

        View contentView = popup.getContentView();
        Preconditions.checkNotNull(contentView, "wrap PopupWindow before it has a ContentView");

        DDDecorView decorView = new DDDecorView(contentView.getContext());
        decorView.addView(contentView);

        popup.setContentView(decorView);

        WindowCallbackWrapper wrapper = new WindowCallbackWrapper(decorView, null);
        decorView.setCallbackWrapper(wrapper);
        return wrapper;
    }

}



