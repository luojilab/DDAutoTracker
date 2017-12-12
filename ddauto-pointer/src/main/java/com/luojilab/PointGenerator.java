package com.luojilab;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Pair;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.luojilab.bean.PointConfig;
import com.luojilab.utils.CoreUtils;
import com.luojilab.utils.DDLogger;
import com.luojilab.utils.PointConfigsHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * user liushuo
 * date 2017/4/6
 */

public class PointGenerator {
    private static final String TAG = AutoPointer.TAG;

    //埋点配置集合 global_view_id--->PointConfig
    private Map<String, PointConfig> mPointConfigs = new ConcurrentHashMap<>();

    private static PointGenerator sGenerator;

    private PointGenerator() {
        syncLocalConfigs();
    }

    public static PointGenerator getInstance() {
        synchronized (PointGenerator.class) {
            if (sGenerator == null) {
                sGenerator = new PointGenerator();
            }

            return sGenerator;
        }
    }

    public void syncLocalConfigs() {
        //解析本地的埋点配置
        List<JsonObject> list = PointConfigsHelper.readConfigFile();
        cachePointConfigAsMap(list);
    }

    @Nullable
    public Pair<String, Map<String, Object>> generatePointData(@NonNull String idName, @Nullable Object object) {
        Preconditions.checkArgument(!TextUtils.isEmpty(idName));

        /*发送调试埋点逻辑*/
        if (AutoPointer.isDebugPoint()) {
            //快速失败策略
            if (object == null) {
                return Pair.create(idName, null);
            }

            //兼容旧版本使用JSONObject作为业务数据
            if (object instanceof JSONObject) {
                Map<String, Object> map = CoreUtils.json2Map(object.toString());
                return Pair.create(idName, map);
            }

            //bean作为业务数据
            Map<String, Object> map = CoreUtils.bean2Map(object);
            return Pair.create(idName, map);
        }

        /*处理发送nlog逻辑*/
        if (mPointConfigs.isEmpty()) {
            DDLogger.e(TAG, "没有埋点配置信息，不能发送埋点...");
            return null;
        }


        PointConfig config = mPointConfigs.get(idName);
        if (config == null) {
            DDLogger.e(TAG, String.format("id 为%s的控件没有对应的埋点配置,不能发送埋点", idName));
            return null;
        }

        Map<String, Object> pointData = getFieldsForConfig(config, object);

        return Pair.create(config.getEvent_id(), pointData);


    }

    @NonNull
    private Map<String, Object> getFieldsForConfig(@NonNull PointConfig config, @Nullable Object data) {

        if (data == null) return new HashMap<>();

        List<PointConfig.ParamsBean> list = config.getParams();
        //该埋点未配置参数字段，无需上传数据
        if (list == null || list.isEmpty()) return new HashMap<>();

        Map<String, Object> point = new HashMap<>();
        for (PointConfig.ParamsBean param : list) {
            String p = param.getP();
            if (TextUtils.isEmpty(p)) continue;

            Object fieldValue = getFieldValue(data, p);
            if (fieldValue == null) continue;

            point.put(p, String.valueOf(fieldValue));
        }
        return point;
    }

    @Nullable
    private Object getFieldValue(@NonNull Object data, @NonNull String param) {

        //支持多层级
        Iterable<String> segments = Splitter.on("/")
                .trimResults()
                .omitEmptyStrings()
                .split(param);

        List<String> keys = Lists.newArrayList(segments);
        if (keys.size() == 0) return null;

        //兼容旧版本操作json的情况
        if (data instanceof JSONObject) {
            JSONObject json = (JSONObject) data;

            int itrTrail = keys.size() - 1;
            for (int i = 0; i < itrTrail; i++) {
                String key = keys.get(i);

                Object obj = json.opt(key);
                if (obj == null) return null;
                if (!(obj instanceof JSONObject)) return null;

                json = (JSONObject) obj;
            }
            return json.opt(keys.get(itrTrail));
        }

        Object obj = data;
        for (String segment : segments) {
            obj = CoreUtils.getFieldValue(obj, segment);
            if (obj == null) break;
        }

        return obj;
    }

    /**
     * "ctr_id": "ColumnVC_ArticleIcon",
     * "event_id": "article_into",
     * "position": "column_list",
     * "params": [
     * {
     * "p": "article_id"
     * }
     *
     * @param list
     */
    private void cachePointConfigAsMap(@NonNull List<JsonObject> list) {

        Map<String, PointConfig> map = new HashMap<>();

        for (JsonObject config : list) {
            if (config == null) continue;

            PointConfig pc = CoreUtils.json2Bean(config, PointConfig.class);
            if (pc == null) continue;

            String id = pc.getCtr_id();
            if (TextUtils.isEmpty(id)) continue;

            map.put(id, pc);
        }

        //解决并发修改问题
        Map<String, PointConfig> diff = Maps.difference(map, mPointConfigs).entriesOnlyOnRight();
        if (diff == null || diff.size() == 0) {
            mPointConfigs.putAll(map);
            return;
        }

        Set<String> diffKeys = diff.keySet();
        mPointConfigs.keySet().removeAll(diffKeys);

        mPointConfigs.putAll(map);

    }
}
