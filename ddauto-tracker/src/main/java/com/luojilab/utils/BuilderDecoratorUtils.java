package com.luojilab.utils;

import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;
import com.luojilab.init.AutoTrackerInitializer;
import com.luojilab.init.IOkHttpClientBuilderDecorator;

import java.util.List;

import okhttp3.OkHttpClient;

/**
 * Created by liushuo on 2017/8/24.
 */

public class BuilderDecoratorUtils {
    public static void decorateOkHttpClientBuilder(@NonNull OkHttpClient.Builder builder) {
        Preconditions.checkNotNull(builder);

        List<IOkHttpClientBuilderDecorator> decorators = AutoTrackerInitializer.getInstance().
                getOkHttpClientBuilderDecorators();

        for(IOkHttpClientBuilderDecorator decorator:decorators){
            decorator.decorate(builder);
        }
    }
}
