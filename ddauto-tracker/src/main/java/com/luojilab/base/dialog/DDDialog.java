package com.luojilab.base.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;

import com.google.common.base.Preconditions;
import com.luojilab.AutoTracker;
import com.luojilab.DataConfigureImp;

/**
 * Created by liushuo on 2017/6/1.
 */

public class DDDialog extends Dialog implements DataConfigureImp {

    /*用于配置自定义布局绑定的数据(自动打点使用)*/
    private DataConfigureImp mDataConfigure;


    public DDDialog(@NonNull Context context) {
        super(context);
    }

    public DDDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected DDDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 对话框的自动打点数据需要在super.onsTart()之后执行
     */
    @Override
    protected void onStart() {
        super.onStart();
        //配置自动打点
        mDataConfigure = AutoTracker.wrapWindowCallback(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
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
