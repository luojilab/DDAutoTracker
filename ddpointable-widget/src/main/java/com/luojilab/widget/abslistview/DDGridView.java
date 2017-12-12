package com.luojilab.widget.abslistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.luojilab.widget.ViewConstants;

/**
 * user liushuo
 * date 2017/5/13
 * 所有布局中使用系统GridView的地方，会自动替换为DDGridView
 */

public class DDGridView extends GridView {

    private int mSelectorRes;

    public DDGridView(Context context) {
        super(context);
        initView();
    }

    public DDGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public DDGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        mSelectorRes = attrs.getAttributeResourceValue(ViewConstants.ANDROID_NAMESPACE, ViewConstants.ATTR_LIST_SELECTOR, ViewConstants.VALUE_LIST_SELECTOR_DEFAULT);
    }

    private void initView() {
        mSelectorRes = ViewConstants.VALUE_LIST_SELECTOR_DEFAULT;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter == null) {
            super.setAdapter(null);
            return;
        }

        super.setAdapter(ListAdapterProxy.wrapAdapter(this, adapter, mSelectorRes));
    }
}
