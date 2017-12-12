package com.luojilab.utils;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.common.base.Preconditions;
import com.luojilab.DataConfigureImp;

/**
 * Created by liushuo on 2017/5/19.
 */

public class AutoPointHelper {
    /**
     * 忽略指定view的自动打点功能
     *
     * @param context
     * @param view
     */
    public static void ignoreAutoPoint(@NonNull Context context, @NonNull View view) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(view);

        if (context instanceof DataConfigureImp) {
            DataConfigureImp dci = (DataConfigureImp) context;
            dci.ignoreAutoPoint(view);
        }
    }

    public static void configLayoutData(@NonNull Context context, @IdRes int id, @NonNull Object data) {
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(data);
        Preconditions.checkArgument(id > View.NO_ID);

        if (context instanceof DataConfigureImp) {
            DataConfigureImp dc = (DataConfigureImp) context;
            dc.configLayoutData(id, data);

        }
    }


    public static boolean canConfigLayoutData(@Nullable Context context, @Nullable Object data) {
        if (context == null || data == null) return false;

        return context instanceof DataConfigureImp;
    }

    public static boolean canIgnoreAutoPoint(@Nullable Context context, @Nullable View view) {
        if (context == null || view == null) return false;

        return context instanceof DataConfigureImp;
    }
}
