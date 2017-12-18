package com.luojilab;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liushuo on 2017/6/23.
 * 为了跟踪线上错误，内部定义的打点结构
 */

public class InternalTracks {
    /*自动打点异常,data key = exception*/
    public static final String AutoTrackerError = "android_auto_pointer_error";

    /*推送异常,data key = exception*/
    public static final String PushError = "android_push_error";


    /**
     * 记录内部错误到埋点平台
     *
     * @param event
     * @param data
     */
    public static void logInternalError(@NonNull String event, @NonNull Object data) {
        Preconditions.checkNotNull(event);
        Preconditions.checkNotNull(data);

        Map<String, Object> value = new HashMap<>();
        value.put("exception", data);

        // TODO: 2017/11/15  日志上报内部错误
    }
}
