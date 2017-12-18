package com.luojilab;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;

/**
 * user liushuo
 * date 2017/4/6
 */

public class SimpleWindowCallback implements Window.Callback {

    private Window.Callback mCallback;

    public SimpleWindowCallback(@Nullable Window.Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mCallback == null) return false;
        try {
            return mCallback.dispatchKeyEvent(event);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if (mCallback == null) return false;

        return mCallback.dispatchKeyShortcutEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mCallback == null) return false;

        return mCallback.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchTrackballEvent(MotionEvent event) {
        if (mCallback == null) return false;

        return mCallback.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (mCallback == null) return false;

        return mCallback.dispatchTouchEvent(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        if (mCallback == null) return false;

        return mCallback.dispatchPopulateAccessibilityEvent(event);
    }

    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        if (mCallback == null) return null;

        return mCallback.onCreatePanelView(featureId);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (mCallback == null) return false;

        return mCallback.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (mCallback == null) return false;

        return mCallback.onPreparePanel(featureId, view, menu);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (mCallback == null) return false;

        return mCallback.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (mCallback == null) return false;

        return mCallback.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {
        if (mCallback == null) return;

        mCallback.onWindowAttributesChanged(attrs);
    }

    @Override
    public void onContentChanged() {
        if (mCallback == null) return;

        mCallback.onContentChanged();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (mCallback == null) return;

        mCallback.onWindowFocusChanged(hasFocus);

    }

    @Override
    public void onAttachedToWindow() {
        if (mCallback == null) return;

        mCallback.onAttachedToWindow();

    }

    @Override
    public void onDetachedFromWindow() {
        if (mCallback == null) return;

        mCallback.onDetachedFromWindow();

    }

    @Override
    public void onPanelClosed(int featureId, Menu menu) {
        if (mCallback == null) return;

        mCallback.onPanelClosed(featureId, menu);

    }

    @Override
    public boolean onSearchRequested() {
        if (mCallback == null) return false;

        return mCallback.onSearchRequested();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean onSearchRequested(SearchEvent searchEvent) {
        if (mCallback == null) return false;

        return mCallback.onSearchRequested(searchEvent);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (mCallback == null) return null;

        return mCallback.onWindowStartingActionMode(callback);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        if (mCallback == null) return null;

        return mCallback.onWindowStartingActionMode(callback, type);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        if (mCallback == null) return;

        mCallback.onActionModeStarted(mode);
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        if (mCallback == null) return;

        mCallback.onActionModeFinished(mode);
    }
}
