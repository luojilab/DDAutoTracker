package com.luojilab.widget.abslistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.luojilab.widget.ViewConstants;


/**
 * @author liujun
 *         <p>
 *         所有布局中使用系统ListView的地方，会自动替换为DDListView
 */
public class DDListView extends ListView {

    private int mSelectorRes;

    public DDListView(Context context) {
        super(context);
        initView();
    }

    public DDListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }


    public DDListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    private void initView() {
        mSelectorRes = ViewConstants.VALUE_LIST_SELECTOR_DEFAULT;
    }

    private void initView(AttributeSet attrs) {
        mSelectorRes = attrs.getAttributeResourceValue(ViewConstants.ANDROID_NAMESPACE, ViewConstants.ATTR_LIST_SELECTOR, ViewConstants.VALUE_LIST_SELECTOR_DEFAULT);
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
