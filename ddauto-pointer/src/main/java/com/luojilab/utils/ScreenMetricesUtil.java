package com.luojilab.utils;

import android.content.Context;

/**
 * Created by liushuo on 2017/8/24.
 */

public class ScreenMetricesUtil {
    public static int convertDipToPixels(Context context, float dips) {
        return (int) (context.getResources().getDisplayMetrics().density * dips + 0.5f);
    }
}
