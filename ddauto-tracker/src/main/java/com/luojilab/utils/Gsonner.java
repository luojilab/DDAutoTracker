package com.luojilab.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * user liushuo
 * date 2017/3/21
 */

public class Gsonner {
    private JsonObject mJson;

    private Gsonner(JsonObject json) {
        mJson = json;
    }

    @NonNull
    public static Gsonner createGsonner(@NonNull JsonObject json) {
        Preconditions.checkNotNull(json);

        return new Gsonner(json);
    }

    @Nullable
    public JsonArray getJsonArray(@NonNull String key) {
        Preconditions.checkNotNull(key);

        JsonElement jsonElement = mJson.get(key);
        if (jsonElement == null || !(jsonElement instanceof JsonArray)) {
            DDLogger.w(DDLogger.TAG, String.format("Illegal jsonArray element of key! element=%1$s,key=%2$s", jsonElement, key));
            return null;
        }

        return (JsonArray) jsonElement;
    }

    @Nullable
    public JsonObject getJsonObject(@NonNull String key) {
        Preconditions.checkNotNull(key);

        JsonElement jsonElement = mJson.get(key);
        if (jsonElement == null || !(jsonElement instanceof JsonObject)) {
            DDLogger.w(DDLogger.TAG, String.format("Illegal jsonObject element of key! element=%1$s,key=%2$s", jsonElement, key));
            return null;
        }

        return (JsonObject) jsonElement;
    }

    @Nullable
    public JsonPrimitive getJsonPrimitive(@NonNull String key) {
        Preconditions.checkNotNull(key);

        JsonElement jsonElement = mJson.get(key);
        if (jsonElement == null || !(jsonElement instanceof JsonPrimitive)) {
            DDLogger.w(DDLogger.TAG, String.format("Illegal JsonPrimitive element of key! element=%1$s,key=%2$s", jsonElement, key));
            return null;
        }

        return (JsonPrimitive) jsonElement;
    }

    @NonNull
    public String getAsString(@NonNull String key) {
        Preconditions.checkNotNull(key);

        JsonPrimitive primitive = getJsonPrimitive(key);
        if (primitive == null) return "";

        return primitive.getAsString();
    }

    @Nullable
    public String getAsString(@NonNull String key, @Nullable String def) {
        Preconditions.checkNotNull(key);

        JsonPrimitive primitive = getJsonPrimitive(key);
        if (primitive == null) return def;

        return primitive.getAsString();
    }

    public int getAsInt(@NonNull String key, int def) {
        Preconditions.checkNotNull(key);

        JsonPrimitive primitive = getJsonPrimitive(key);
        if (primitive == null) return def;

        return primitive.getAsInt();
    }

    public double getAsDouble(@NonNull String key, double def) {
        Preconditions.checkNotNull(key);

        JsonPrimitive primitive = getJsonPrimitive(key);
        if (primitive == null) return def;

        return primitive.getAsDouble();
    }

    public long getAsLong(@NonNull String key, long def) {
        Preconditions.checkNotNull(key);

        JsonPrimitive primitive = getJsonPrimitive(key);
        if (primitive == null) return def;

        return primitive.getAsLong();
    }
}
