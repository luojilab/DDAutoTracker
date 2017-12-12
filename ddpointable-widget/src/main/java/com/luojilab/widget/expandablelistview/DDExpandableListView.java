package com.luojilab.widget.expandablelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * user liushuo
 * date 2017/5/13
 */

public class DDExpandableListView extends ExpandableListView {

    public DDExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public DDExpandableListView(Context context) {
        super(context);
    }

    public DDExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        if (adapter == null) {
            super.setAdapter(adapter);
            return;
        }

        super.setAdapter(ExpandableListAdapterProxy.wrapAdapter(this, adapter));
    }
}
