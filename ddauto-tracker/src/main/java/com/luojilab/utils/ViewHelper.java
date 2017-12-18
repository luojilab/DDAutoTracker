package com.luojilab.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.base.Preconditions;
import com.luojilab.InternalTracks;

import java.lang.reflect.Field;

/**
 * user liushuo
 * date 2017/4/7
 */

public class ViewHelper {
    /**
     * @param ancestor
     * @return null, 则解析过程发生异常
     */
    @Nullable
    public static View findTouchTarget(@NonNull ViewGroup ancestor) {
        Preconditions.checkNotNull(ancestor);

        try {
            Field firstTouchTargetField = CoreUtils.getDeclaredField(ancestor, "mFirstTouchTarget");
            if (firstTouchTargetField == null) {
                logReflectException("mFirstTouchTarget");
                return ancestor;
            }

            firstTouchTargetField.setAccessible(true);
            Object firstTouchTarget = firstTouchTargetField.get(ancestor);
            if (firstTouchTarget == null) return ancestor;

            Field firstTouchViewField = firstTouchTarget.getClass().getDeclaredField("child");
            if (firstTouchViewField == null) {
                logReflectException("child");
                return ancestor;
            }

            firstTouchViewField.setAccessible(true);
            View firstTouchView = (View) firstTouchViewField.get(firstTouchTarget);
            if (firstTouchView == null) return ancestor;

            return firstTouchView;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    @Nullable
    public static View findTouchTargetRecursive(@NonNull ViewGroup ancestor) {
        Preconditions.checkNotNull(ancestor);

        try {
            Field firstTouchTargetField = CoreUtils.getDeclaredField(ancestor, "mFirstTouchTarget");
            if (firstTouchTargetField == null) {
                logReflectException("mFirstTouchTarget");
                return ancestor;
            }

            firstTouchTargetField.setAccessible(true);
            Object firstTouchTarget = firstTouchTargetField.get(ancestor);
            if (firstTouchTarget == null) return ancestor;

            Field firstTouchViewField = firstTouchTarget.getClass().getDeclaredField("child");
            if (firstTouchViewField == null) {
                logReflectException("child");
                return ancestor;
            }

            firstTouchViewField.setAccessible(true);
            View firstTouchView = (View) firstTouchViewField.get(firstTouchTarget);
            if (firstTouchView == null) return ancestor;

            if (firstTouchView instanceof ViewGroup) {
                return findTouchTargetRecursive((ViewGroup) firstTouchView);
            }

            return firstTouchView;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /*如何发生兼容性问题，通过反射无法获取到需要的field，则记录一次log，不重复记录*/
    private static boolean sHasLog = false;

    private static void logReflectException(String param) {
        if (sHasLog) return;
        sHasLog = true;

        Object desc = String.format("cannot get %s child", param);
        String event = InternalTracks.AutoTrackerError;
        InternalTracks.logInternalError(event, desc);
    }

}