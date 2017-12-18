package com.luojilab.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.luojilab.WindowCallbackWrapper;

/**
 * Created by liushuo on 2017/6/1.
 * 包裹任意 content view，拦截touch 事件，处理自动打点逻辑
 */

public class DDDecorView extends FrameLayout {

    WindowCallbackWrapper mCallbackWrapper;

    public DDDecorView(@NonNull Context context) {
        super(context);
    }

    public DDDecorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DDDecorView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCallbackWrapper(@NonNull WindowCallbackWrapper callbackWrapper) {
        mCallbackWrapper = callbackWrapper;
    }

    @Override
    public void addView(View child) {
        removeAllViews();

        super.addView(child);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mCallbackWrapper == null) {
            return super.dispatchTouchEvent(ev);
        }

        mCallbackWrapper.dispatchTouchEvent(ev);

        return super.dispatchTouchEvent(ev);
    }
}
