package com.luojilab.strategy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.google.common.base.Preconditions;
import com.luojilab.AutoPointer;
import com.luojilab.utils.ViewHelper;

/**
 * user liuhuo
 * date 2017/4/7
 * <p>
 * Known Indirect Subclasses:
 * <p>
 * AdapterViewFlipper,
 * AppCompatSpinner,
 * ExpandableListView,
 * Gallery, GridView,
 * ListView,
 * Spinner,
 * StackView
 */

public class AdapterViewStrategy extends DataStrategy {
    private static final String TAG = AutoPointer.TAG;

    @Nullable
    @Override
    public Object fetchTargetData(@NonNull View container) {
        Preconditions.checkNotNull(container);

        AdapterView adapterView = (AdapterView) container;

        View child = ViewHelper.findTouchTarget(adapterView);
        if (child == null) return null;
        if (child == adapterView) return null;

        //parse data
        int firstPos = adapterView.getFirstVisiblePosition();
        int index = adapterView.indexOfChild(child);
        if (index == -1) {
            return null;
        }

        int adapterPos = firstPos + index;

        Adapter adapter = adapterView.getAdapter();
        Object data = adapter.getItem(adapterPos);

        return data;
    }
}
