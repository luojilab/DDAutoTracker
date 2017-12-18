package com.luojilab.strategy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.google.common.base.Preconditions;
import com.luojilab.AutoTracker;
import com.luojilab.utils.ViewHelper;

/**
 * user liuhuo
 * date 2017/4/7
 */

public class ExpandableListViewStrategy extends DataStrategy {
    private static final String TAG = AutoTracker.TAG;

    @Nullable
    @Override
    public Object fetchTargetData(@NonNull View container) {
        Preconditions.checkNotNull(container);

        ExpandableListView adapterView = (ExpandableListView) container;

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
        long packedPos = adapterView.getExpandableListPosition(adapterPos);
        if (packedPos == ExpandableListView.PACKED_POSITION_VALUE_NULL) {
            return null;
        }

        int posType = ExpandableListView.getPackedPositionType(packedPos);
        if (posType == ExpandableListView.PACKED_POSITION_TYPE_NULL) {
            return null;
        }

        ExpandableListAdapter adapter = adapterView.getExpandableListAdapter();
        if (posType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

            int grpPos = ExpandableListView.getPackedPositionGroup(packedPos);
            Object grpObj = adapter.getGroup(grpPos);

            return grpObj;

        } else if (posType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

            int childPos = ExpandableListView.getPackedPositionChild(packedPos);
            int grpPos = ExpandableListView.getPackedPositionGroup(packedPos);
            Object childObj = adapter.getChild(grpPos, childPos);

            return childObj;
        }

        return null;
    }
}
