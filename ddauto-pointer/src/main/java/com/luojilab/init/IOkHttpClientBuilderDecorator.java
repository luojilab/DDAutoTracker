package com.luojilab.init;

import okhttp3.OkHttpClient;

/**
 * Created by liushuo on 2017/8/24.
 */

public interface IOkHttpClientBuilderDecorator {
    void decorate(OkHttpClient.Builder builder);
}
