package com.luojilab.strategy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.common.base.Preconditions;
import com.luojilab.AutoTracker;
import com.luojilab.utils.ViewHelper;
import com.luojilab.widget.adapter.DDPagerAdapter;

/**
 * user liuhuo
 * date 2017/4/7
 */

public class ViewPagerStrategy extends DataStrategy {
    private static final String TAG = AutoTracker.TAG;

    @Nullable
    @Override
    public Object fetchTargetData(@NonNull View container) {
        Preconditions.checkNotNull(container);

        ViewPager adapterView = (ViewPager) container;

        View child = ViewHelper.findTouchTarget(adapterView);
        if (child == null) return null;
        if (child == adapterView) return null;

        //parse data
        PagerAdapter adapter = adapterView.getAdapter();
        if (!(adapter instanceof DDPagerAdapter)) {
            return null;
        }
        int position = adapterView.getCurrentItem();
        Object data = ((DDPagerAdapter) adapter).getItem(position);

        return data;
    }
}
