package com.luojilab.widget.expandablelistview;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * user liushuo
 * date 2017/5/13
 */

public class ExpandableListAdapterProxy extends BaseExpandableListAdapter {

    public static ExpandableListAdapter wrapAdapter(@NonNull ExpandableListView listView, @NonNull ExpandableListAdapter adapter) {
        return new ExpandableListAdapterProxy(listView, adapter);
    }

    private class ItemClickDelegate implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int pos = mListView.getPositionForView(v);
            if (pos == AdapterView.INVALID_POSITION) return;

            long packedPos = mListView.getExpandableListPosition(pos);
            if (packedPos == ExpandableListView.PACKED_POSITION_VALUE_NULL) return;

            int type = ExpandableListView.getPackedPositionType(packedPos);
            switch (type) {
                case ExpandableListView.PACKED_POSITION_TYPE_NULL:
                    break;
                case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
                    caseChildClick(mListView, v, packedPos, pos);
                    break;
                case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
                    caseGroupClick(mListView, v, packedPos, pos);
                    break;
            }

        }

        private void caseGroupClick(ExpandableListView lv, View view, long packedPos, int flatPos) {

            int grpPos = ExpandableListView.getPackedPositionGroup(packedPos);
            if (grpPos < 0) return;

            ExpandableListAdapter adapter = lv.getExpandableListAdapter();
            long grpId = adapter.getGroupId(grpPos);

            lv.performItemClick(view, flatPos, grpId);
        }

        private void caseChildClick(ExpandableListView lv, View view, long packedPos, int flatPos) {

            int grpPos = ExpandableListView.getPackedPositionGroup(packedPos);
            if (grpPos < 0) return;

            int childPos = ExpandableListView.getPackedPositionChild(packedPos);
            if (childPos < 0) return;

            ExpandableListAdapter adapter = lv.getExpandableListAdapter();
            long chiId = adapter.getChildId(grpPos, childPos);

            lv.performItemClick(view, flatPos, chiId);
        }
    }


    private ExpandableListView mListView;
    private ExpandableListAdapter mAdapter;
    private View.OnClickListener mClickListener = new ItemClickDelegate();

    private ExpandableListAdapterProxy(ExpandableListView listView, ExpandableListAdapter adapter) {
        this.mAdapter = adapter;
        mListView = listView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public int getGroupCount() {
        return mAdapter.getGroupCount();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mAdapter.getChildrenCount(groupPosition);
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mAdapter.getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mAdapter.getChild(groupPosition, childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return mAdapter.getGroupId(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return mAdapter.getChildId(groupPosition, childPosition);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view = mAdapter.getGroupView(groupPosition, isExpanded, convertView, parent);

//        view.setBackgroundResource(ViewConstants.VALUE_LIST_SELECTOR_DEFAULT);
        view.setOnClickListener(mClickListener);

        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View view = mAdapter.getChildView(groupPosition, childPosition, isLastChild, convertView, parent);
//        view.setBackgroundResource(ViewConstants.VALUE_LIST_SELECTOR_DEFAULT);
        view.setOnClickListener(mClickListener);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return mAdapter.isChildSelectable(groupPosition, childPosition);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        mAdapter.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        mAdapter.onGroupCollapsed(groupPosition);
    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return mAdapter.getCombinedChildId(groupId, childId);
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return mAdapter.getCombinedGroupId(groupId);
    }

    /**
     * @see DataSetObservable#notifyInvalidated()
     */
    public void notifyDataSetInvalidated() {
        if (!(mAdapter instanceof BaseExpandableListAdapter)) return;

        BaseExpandableListAdapter bAdapter = (BaseExpandableListAdapter) mAdapter;
        bAdapter.notifyDataSetInvalidated();
    }

    /**
     * @see DataSetObservable#notifyChanged()
     */
    public void notifyDataSetChanged() {
        if (!(mAdapter instanceof BaseExpandableListAdapter)) return;

        BaseExpandableListAdapter bAdapter = (BaseExpandableListAdapter) mAdapter;
        bAdapter.notifyDataSetChanged();
    }


    /**
     * {@inheritDoc}
     *
     * @return 0 for any group or child position, since only one child type count is declared.
     */
    public int getChildType(int groupPosition, int childPosition) {
        if (!(mAdapter instanceof BaseExpandableListAdapter)) return 0;

        BaseExpandableListAdapter bAdapter = (BaseExpandableListAdapter) mAdapter;
        return bAdapter.getChildType(groupPosition, childPosition);
    }

    /**
     * {@inheritDoc}
     *
     * @return 1 as a default value in BaseExpandableListAdapter.
     */
    public int getChildTypeCount() {
        if (!(mAdapter instanceof BaseExpandableListAdapter)) return 1;

        BaseExpandableListAdapter bAdapter = (BaseExpandableListAdapter) mAdapter;
        return bAdapter.getChildTypeCount();
    }

    /**
     * {@inheritDoc}
     *
     * @return 0 for any groupPosition, since only one group type count is declared.
     */
    public int getGroupType(int groupPosition) {
        if (!(mAdapter instanceof BaseExpandableListAdapter)) return 0;

        BaseExpandableListAdapter bAdapter = (BaseExpandableListAdapter) mAdapter;
        return bAdapter.getGroupType(groupPosition);
    }

    /**
     * {@inheritDoc}
     *
     * @return 1 as a default value in BaseExpandableListAdapter.
     */
    public int getGroupTypeCount() {
        if (!(mAdapter instanceof BaseExpandableListAdapter)) return 1;

        BaseExpandableListAdapter bAdapter = (BaseExpandableListAdapter) mAdapter;
        return bAdapter.getGroupTypeCount();
    }
}
