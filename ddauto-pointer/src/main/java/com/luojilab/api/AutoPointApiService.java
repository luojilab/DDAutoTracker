package com.luojilab.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * user liushuo
 * date 2017/4/12
 */

public interface AutoPointApiService {

    //发送调试埋点
    @POST()
    Call<JsonObject> postDebugPoint(@Body JsonObject param, @Url String url);

    //请求埋点配置
    @POST()
    Call<JsonObject> getPointConfigInfos(@Body JsonObject param, @Url String url);

}
