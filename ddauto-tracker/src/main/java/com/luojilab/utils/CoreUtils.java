package com.luojilab.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Copyright (c) 2014 Nono_Lilith All right reserved.
 */
public class CoreUtils {

    private static ThreadLocal<Gson> sGson = new ThreadLocal<Gson>();
    private static ThreadLocal<JsonParser> sJsonParser = new ThreadLocal<JsonParser>();

    //系统公用线程池
    public static Executor sExecutor = Executors.newFixedThreadPool(3);


    /**
     * 多个线程同时使用一个Gson操作数据，会有并发问题
     *
     * @return
     */
    @NonNull
    public static Gson getGson() {
        Gson gson = sGson.get();
        if (gson == null) {
            gson = new Gson();
            sGson.set(gson);
        }
        return gson;
    }

    /**
     * 防止多线程同时使用JsonParser可能带来的并发问题
     *
     * @return
     */
    @NonNull
    public static JsonParser getJsonParser() {
        JsonParser parser = sJsonParser.get();
        if (parser == null) {
            parser = new JsonParser();
            sJsonParser.set(parser);
        }
        return parser;
    }

    @Nullable
    public static Map<String, Object> bean2Map(@NonNull Object object) {
        Preconditions.checkNotNull(object);

        try {
            String jsonStr = CoreUtils.getGson().toJson(object);
            JsonObject json = CoreUtils.parseJsonObject(jsonStr);
            Map<String, Object> value = CoreUtils.json2Bean(json, new TypeToken<Map<String, Object>>() {
            });
            return value;
        } catch (Exception e) {
            e.printStackTrace();

            //parse json error
            return null;
        }
    }

    @Nullable
    public static JsonElement bean2Json(@NonNull Object object) {
        Preconditions.checkNotNull(object);

        try {
            String jsonStr = CoreUtils.getGson().toJson(object);
            return CoreUtils.parseJsonObject(jsonStr);
        } catch (Exception e) {
            e.printStackTrace();

            //parse json error
            return null;
        }
    }

    /**
     * @param json
     * @param cls
     * @param <T>
     * @return 调用者需要根据返回结果是否为null判断解析是否成功，执行不同的逻辑
     */
    @Nullable
    public static <T> T json2Bean(@NonNull String json, @NonNull Class<T> cls) {
        Preconditions.checkNotNull(json);
        Preconditions.checkNotNull(cls);

        try {
            return getGson().fromJson(json, cls);
        } catch (Exception e) {
            DDLogger.e(e, null);

            return null;
        }
    }

    /**
     * @param json
     * @param typeToken
     * @param <T>
     * @return 调用者需要根据返回结果是否为null判断解析是否成功，执行不同的逻辑
     */
    @Nullable
    public static <T> T json2Bean(@NonNull JsonElement json, @NonNull TypeToken<T> typeToken) {
        Preconditions.checkNotNull(json);
        Preconditions.checkNotNull(typeToken);

        try {
            return getGson().fromJson(json, typeToken.getType());
        } catch (Exception e) {
            DDLogger.e(e, null);

            return null;
        }
    }

    /**
     * @param json
     * @param cls
     * @param <T>
     * @return 调用者需要根据返回结果是否为null判断解析是否成功，执行不同的逻辑
     */
    @Nullable
    public static <T> T json2Bean(@NonNull JsonElement json, @NonNull Class<T> cls) {

        try {
            return getGson().fromJson(json, cls);
        } catch (Exception e) {
            DDLogger.e(e, null);

            return null;
        }
    }

    /**
     * @param element
     * @return 调用者不关注element是否转换成功，而关心转换的结果，
     * 如果element无效，则结果为空list
     */
    @NonNull
    public static List<JsonObject> toJsonObjects(@NonNull JsonElement element) {
        Preconditions.checkNotNull(element);

        if (element.isJsonNull()) return Collections.emptyList();

        List<JsonObject> list = new ArrayList<>();
        if (element.isJsonObject()) {
            list.add((JsonObject) element);
            return list;
        }

        if (element.isJsonArray()) {
            JsonArray arr = (JsonArray) element;
            Iterator<JsonElement> itr = arr.iterator();
            while (itr.hasNext()) {
                JsonElement e = itr.next();
                list.addAll(toJsonObjects(e));
            }

            return list;
        }

        return list;
    }


    /**
     * parse string to JsonObject
     *
     * @param content
     * @return 调用者需要根据返回结果是否为null判断解析是否成功，执行不同的逻辑
     */
    @Nullable
    public static JsonObject parseJsonObject(@NonNull String content) {
        Preconditions.checkNotNull(content);

        if (TextUtils.isEmpty(content)) return null;

        JsonParser parser = getJsonParser();
        try {
            JsonObject o = parser.parse(content).getAsJsonObject();
            return o;
        } catch (Exception e) {
            DDLogger.e(e, null);

            return null;
        }
    }

    /**
     * parse string to JsonArray
     *
     * @param content
     * @return 调用者需要根据返回结果是否为null判断解析是否成功，执行不同的逻辑
     */
    @Nullable
    public static JsonArray parseJsonArray(@NonNull String content) {
        Preconditions.checkNotNull(content);

        if (TextUtils.isEmpty(content)) return null;

        JsonParser parser = getJsonParser();
        try {
            JsonArray o = parser.parse(content).getAsJsonArray();
            return o;
        } catch (Exception e) {
            DDLogger.e(e, null);

            return null;
        }
    }


    /**
     * @param context c
     * @return true网路可用。false不可用
     * @since 1.0.0
     */
    public static boolean checkNetActive(@NonNull Context context) {
        Preconditions.checkNotNull(context);

        try {
            //获取连接管理对象
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivity == null) return false;

            //获取活动的网络连接
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info == null || !info.isConnected()) return false;

            if (info.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        } catch (Exception e) {
            DDLogger.e(e, null);

        }
        return false;
    }


    @Nullable
    public static Map<String, Object> json2Map(@NonNull String json) {
        Preconditions.checkNotNull(json);

        if (TextUtils.isEmpty(json)) return null;

        try {
            TypeToken<Map<String, Object>> token = new TypeToken<Map<String, Object>>() {
            };

            return getGson().fromJson(json, token.getType());
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * @param json
     * @return 调用者需要根据返回结果是否为null判断转换是否成功，执行不同的逻辑
     */
    @Nullable
    public static ContentValues json2ContentValues(@NonNull JsonObject json) {
        Preconditions.checkNotNull(json);

        if (json.size() == 0) return null;

        ContentValues values = new ContentValues();
        Set<Map.Entry<String, JsonElement>> set = json.entrySet();
        Iterator<Map.Entry<String, JsonElement>> itr = set.iterator();

        while (itr.hasNext()) {
            Map.Entry<String, JsonElement> entry = itr.next();
            if (entry == null) continue;

            String key = entry.getKey();
            if (TextUtils.isEmpty(key)) continue;

            Object value = entry.getValue();
            if (value == null) {
                DDLogger.d(DDLogger.TAG, "value 值为null,key:" + key);
                continue;
            }

            if (value instanceof String) {
                values.put(key, (String) value);
            } else if (value instanceof Integer) {
                values.put(key, (Integer) value);
            } else if (value instanceof Float) {
                values.put(key, (Float) value);
            } else {
                DDLogger.d(DDLogger.TAG, "未知的value类型,key:" + key + ",value:" + value);

            }
        }

        return values;
    }

    /**
     * 循环向上转型, 获取对象的 DeclaredField
     *
     * @param object    : 子类对象
     * @param fieldName : 父类中的属性名
     * @return 父类中的属性对象
     */
    @Nullable
    public static Field getDeclaredField(@NonNull Object object, @NonNull String fieldName) {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(!TextUtils.isEmpty(fieldName));

        if (TextUtils.isEmpty(fieldName)) return null;

        Class<?> clazz = object.getClass();

        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                //这里甚么都不要做！并且这里的异常必须这样写，不能抛出去。
                //如果这里的异常打印或者往外抛，则就不会执行clazz = clazz.getSuperclass(),最后就不会进入到父类中了
            } catch (NullPointerException e) {
                e.printStackTrace();

                return null;
            } catch (SecurityException e) {
                e.printStackTrace();

                return null;
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        return null;
    }

    @Nullable
    public static Object getFieldValue(@NonNull Object object, @NonNull String fieldName) {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(!TextUtils.isEmpty(fieldName));

        Field field = getDeclaredField(object, fieldName);
        if (field == null) return null;

        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static <T> T[] createGenericArray(@NonNull Class<T> type, @IntRange(from = 0) int size) {
        Preconditions.checkNotNull(type);
        Preconditions.checkArgument(size >= 0);

        return (T[]) Array.newInstance(type, size);
    }

    @NonNull
    public static String toHexString(@NonNull byte[] bytes) {
        Preconditions.checkNotNull(bytes);

        String hexStr = "0123456789abcdef";
        String result = "";
        String hex = "";
        for (int i = 0; i < bytes.length; i++) {
            //字节高4位
            hex = String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4));
            //字节低4位
            hex += String.valueOf(hexStr.charAt(bytes[i] & 0x0F));
            result += hex;  //这里可以去掉空格，或者添加0x标识符。
        }
        return result;
    }

    @NonNull
    public static Map<String, String> json2UrlEncodedMap(@NonNull JsonObject data) {
        Preconditions.checkNotNull(data);

        Map<String, String> map = new HashMap<>();

        Set<Map.Entry<String, JsonElement>> set = data.entrySet();
        for (Map.Entry<String, JsonElement> entry : set) {

            String key = entry.getKey();
            if (TextUtils.isEmpty(key)) {
                DDLogger.e("key is empty,value:" + data);
                continue;
            }

            JsonElement value = entry.getValue();
            if (value == null) {
                DDLogger.e("value is empty,value:" + value);
                continue;
            }

            String content = value.isJsonPrimitive() ? value.getAsString() : value.toString();
            try {
                map.put(key, URLEncoder.encode(content, "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @NonNull
    public static Map<String, String> json2QueryMap(@NonNull JsonObject data) {
        Preconditions.checkNotNull(data);

        Map<String, String> map = new HashMap<>();

        Set<Map.Entry<String, JsonElement>> set = data.entrySet();
        for (Map.Entry<String, JsonElement> entry : set) {

            String key = entry.getKey();
            if (TextUtils.isEmpty(key)) {
                DDLogger.e("key is empty,value:" + data);
                continue;
            }

            JsonElement value = entry.getValue();
            if (value == null) {
                DDLogger.e("value is empty,value:" + value);
                continue;
            }

            String content = value.isJsonPrimitive() ? value.getAsString() : value.toString();
            map.put(key, content);
        }

        return map;
    }


}
