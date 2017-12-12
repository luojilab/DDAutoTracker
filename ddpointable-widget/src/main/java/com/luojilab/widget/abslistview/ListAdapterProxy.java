package com.luojilab.widget.abslistview;

import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.google.common.base.Preconditions;

/**
 * user liushuo
 * date 2017/5/13
 */

public class ListAdapterProxy implements ListAdapter {

    public static ListAdapter wrapAdapter(@NonNull AbsListView lv, @NonNull ListAdapter adapter, @DrawableRes int selector) {
        Preconditions.checkNotNull(lv);
        Preconditions.checkNotNull(adapter);

        return new ListAdapterProxy(lv, adapter, selector);
    }

    private class ItemClickDelegate implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = mListView.getPositionForView(v);
            if (position == AdapterView.INVALID_POSITION) return;

            mListView.performItemClick(v, position, getItemId(position));
        }
    }

    private class ItemLongClickDelegate implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {
            int position = mListView.getPositionForView(v);
            if (position == AdapterView.INVALID_POSITION) return false;

            return mListView.showContextMenuForChild(v);
        }
    }

    private AbsListView mListView;
    private ListAdapter mAdapter;
    private int mSelector;

    private View.OnClickListener mClickListener = new ItemClickDelegate();
    private View.OnLongClickListener mLongClickListener = new ItemLongClickDelegate();

    private ListAdapterProxy(AbsListView lv, ListAdapter adapter, @DrawableRes int selector) {
        this.mAdapter = adapter;
        mListView = lv;
        mSelector = selector;

    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
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
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = mAdapter.getView(position, convertView, parent);

        Drawable bg = view.getBackground();
        if (bg == null) {
            view.setBackgroundResource(mSelector);
        }

        if (hasItemClickListener()) {
            view.setOnClickListener(mClickListener);
        }

        if (hasItemLongClickListener()) {
            view.setOnLongClickListener(mLongClickListener);
        }

        view.setEnabled(isEnabled(position));

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }


    private boolean hasItemClickListener() {
        return mListView.getOnItemClickListener() != null;
    }

    private boolean hasItemLongClickListener() {
        return mListView.getOnItemLongClickListener() != null;
    }
}
