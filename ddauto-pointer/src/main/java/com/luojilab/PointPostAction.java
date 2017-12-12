package com.luojilab;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.luojilab.ddauto_pointer.R;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.luojilab.bean.PointData;
import com.luojilab.utils.DDLogger;
import com.luojilab.utils.ResourceHelper;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */

public class PointPostAction implements Runnable, Callback<JsonObject> {
    private static final String TAG = WindowCallbackWrapper.TAG;

    private View mActionTarget;
    private Object mContextData;


    public static PointPostAction create(@NonNull View actionTarget, @Nullable Object contextData) {
        Preconditions.checkNotNull(actionTarget);

        return new PointPostAction(actionTarget, contextData);
    }

    private PointPostAction(@NonNull View actionTarget, @Nullable Object contextData) {
        mActionTarget = actionTarget;
        mContextData = contextData;
    }

    @Override
    public void run() {
        String idName = ResourceHelper.getGlobalIdName(mActionTarget);//success get the idName
        DDLogger.d(TAG, String.format("global id name=%s", idName));

        if (TextUtils.isEmpty(idName)) return;

        Pair<String, Map<String, Object>> pair = PointGenerator.getInstance().generatePointData(idName, mContextData);
        if (pair == null) return;

        String eventKey = pair.first;
        Map<String, Object> eventValue = pair.second;

        //查看是否配置log_name
        String logName = (String) mActionTarget.getTag(R.id.id_auto_point_logname);

        boolean tagedLogName = !TextUtils.isEmpty(logName);
        boolean hasEventValue = eventValue != null;
        if (hasEventValue && tagedLogName) {
            eventValue.put(PointData.LOG_NAME, logName);
        }

        if (!AutoPointer.isOnlineEnv()) {
            DDLogger.d(TAG, String.format("key=%s,value=%s", eventKey, eventValue));
        }

        if (TextUtils.isEmpty(eventKey)) return;

        if (AutoPointer.isDebugPoint()) {
            //调试模式，埋点发送到调试接口
            postDebugPoint(eventKey, eventValue);
            return;
        }

        //发送埋点到nlog
        postNLog(eventKey, eventValue);
    }

    public static void manualPostPoint(@NonNull View actionTarget, @Nullable Object data) {
        Preconditions.checkNotNull(actionTarget);
        if(AutoPointer.isAutoPointEnable()) {
            PointerExecutor.getHandler().post(create(actionTarget, data));
        }
    }

    public static void postNLog(@NonNull String eventKey, @Nullable Map<String, Object> eventValue) {
        //将埋点信息发送到埋点数据平台
        Log.d(TAG, "正式埋点--->event:" + eventKey + ",value:" + eventValue);
    }

    private void postDebugPoint(@NonNull String eventKey, @Nullable Map<String, Object> eventValue) {
        //将获取到的埋点数据发送到调试埋点平台，以便运营人员可以根据数据配置埋点字段信息
        Log.d(TAG, "调试埋点--->event:" + eventKey + ",value:" + eventValue);
    }


    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        //埋点发送成功
//        ToastManager.show(response.message());
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        // 埋点发送失败
//        ToastManager.show(t.getMessage());
    }
}

