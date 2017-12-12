package com.luojilab.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import static android.content.Context.WINDOW_SERVICE;

/**
 * Created by liushuo on 2017/8/24.
 */

public class ScreenMetrics {

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;

    public static int getScreenWidth(Context context) {
        if (SCREEN_WIDTH > 0) return SCREEN_WIDTH;

        SCREEN_WIDTH = getDisplayMetrics(context).widthPixels;
        return SCREEN_WIDTH;
    }

    /**
     * 获取屏幕高度。
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        if (SCREEN_HEIGHT > 0) return SCREEN_HEIGHT;

        SCREEN_HEIGHT = getDisplayMetrics(context).heightPixels;
        return SCREEN_HEIGHT;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        try {
            WindowManager wm = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            wm.getDefaultDisplay().getMetrics(dm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dm;
    }
}
