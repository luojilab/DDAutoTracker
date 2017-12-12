package com.luojilab.strategy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * user liushuo
 * date 2017/4/7
 * <p>
 * 根据传入的点击事件，在指定的view group
 * 中查找点击事件关联的上下文信息。
 * 上下文定义：用户因为看到某个数据而发生
 * 点击行为，这个数据即数据上下文。与这个
 * 数据关联的view即视图上下文
 */

public abstract class DataStrategy {
    /**
     * 返回点击事件对应的视图上下文和数据上下文
     *
     * @param container
     * @return
     */
    @Nullable
    public abstract Object fetchTargetData(@NonNull View container);
}
