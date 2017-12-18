package com.luojilab.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.Collection;

public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {
    protected int mItemLayout;
    protected Context mContext;

    public BaseArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.mItemLayout = resource;
        this.mContext = context;
    }

    public void addAll(@NonNull Collection<? extends T> collection) {
        this.setNotifyOnChange(false);
        super.clear();
        this.setNotifyOnChange(true);
        super.addAll(collection);
    }

    @NonNull
    public abstract View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent);
}
