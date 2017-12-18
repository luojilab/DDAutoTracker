package com.luojilab;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by liushuo on 2017/5/12.
 */

public interface DataConfigureImp {
    /**
     * 配置layout的数据绑定关系
     *
     * @param id
     * @param object
     */
    @Nullable
    DataConfigureImp configLayoutData(@IdRes int id, @NonNull Object object);

    /**
     * 忽略控件的自动打点功能
     *
     * @param view
     */
    void ignoreAutoTrack(@NonNull View view);
}
