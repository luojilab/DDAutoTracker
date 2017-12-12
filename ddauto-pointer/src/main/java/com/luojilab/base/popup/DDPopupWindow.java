package com.luojilab.base.popup;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.PopupWindow;

import com.google.common.base.Preconditions;
import com.luojilab.AutoPointer;
import com.luojilab.DataConfigureImp;

/**
 * Created by liushuo on 2017/6/1.
 */

public class DDPopupWindow extends PopupWindow implements DataConfigureImp {

    /*用于配置自定义布局绑定的数据(自动打点使用)*/
    private DataConfigureImp mDataConfigure;

    public DDPopupWindow(Context context) {
        super(context);
    }

    public void wrapWindowCallback() {
        mDataConfigure = AutoPointer.wrapWindowCallback(this);
    }

    public DDPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);

        mDataConfigure = AutoPointer.wrapWindowCallback(this);
    }

    public DDPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);

        mDataConfigure = AutoPointer.wrapWindowCallback(this);
    }

    @NonNull
    @Override
    public DataConfigureImp configLayoutData(@IdRes int id, @NonNull Object object) {
        Preconditions.checkNotNull(object);

        mDataConfigure.configLayoutData(id, object);
        return mDataConfigure;
    }

    @Override
    public void ignoreAutoPoint(@NonNull View view) {
        Preconditions.checkNotNull(view);

        mDataConfigure.ignoreAutoPoint(view);
    }

}
