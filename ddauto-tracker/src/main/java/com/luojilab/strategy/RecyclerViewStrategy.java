package com.luojilab.strategy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.common.base.Preconditions;
import com.luojilab.AutoTracker;
import com.luojilab.utils.DDLogger;
import com.luojilab.utils.ViewHelper;
import com.luojilab.widget.adapter.DDRecyclerAdapter;

/**
 * user liuhuo
 * date 2017/4/7
 */

public class RecyclerViewStrategy extends DataStrategy {
    private static final String TAG = AutoTracker.TAG;

    @Nullable
    @Override
    public Object fetchTargetData(@NonNull View container) {
        Preconditions.checkNotNull(container);

        RecyclerView recyclerView = (RecyclerView) container;

        View child = ViewHelper.findTouchTarget(recyclerView);
        if (child == null) return null;
        if (child == recyclerView) return null;

        //parse data
        int adapterPos = recyclerView.getChildAdapterPosition(child);
        if (adapterPos == RecyclerView.NO_POSITION) {
            return null;
        }

        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (!(adapter instanceof DDRecyclerAdapter)) {
            DDLogger.e(TAG, "this RecyclerView does not support auto point action!!!");
            return null;
        }
        DDRecyclerAdapter autoPointAdapter = (DDRecyclerAdapter) adapter;
        Object data = autoPointAdapter.getItem(adapterPos);

        return data;
    }
}
