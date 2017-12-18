package com.luojilab.init;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liushuo on 2017/8/24.
 * 该类用于 主项目向 本module提供数据，可以
 * 根据特定逻辑无限扩展字段
 */

public class AutoTrackerInitializer {
    private List<IOkHttpClientBuilderDecorator> mOkHttpClientBuilderDecorators =
            new ArrayList<>(0);

    private Context mContext;
    private String mServerEnvironment;

    private AutoTrackerInitializer() {
    }

    private static AutoTrackerInitializer mInstance;

    public static AutoTrackerInitializer getInstance() {
        if (mInstance == null) {
            synchronized (AutoTrackerInitializer.class) {
                if (mInstance == null) {
                    mInstance = new AutoTrackerInitializer();
                }
            }
        }

        return mInstance;
    }

    public String getServerEnvironment() {
        return mServerEnvironment;
    }

    public Context getAppContext() {
        return mContext;
    }

    @NonNull
    public List<IOkHttpClientBuilderDecorator> getOkHttpClientBuilderDecorators() {
        return mOkHttpClientBuilderDecorators;
    }

    public AutoTrackerInitializer addOkHttpClientBuilderDecorator(@NonNull IOkHttpClientBuilderDecorator builder) {
        Preconditions.checkNotNull(builder);

        mOkHttpClientBuilderDecorators.add(builder);
        return this;
    }

    public AutoTrackerInitializer appContext(@NonNull Context context) {
        Preconditions.checkNotNull(context);

        mContext = context;
        return this;
    }

    public AutoTrackerInitializer serverEnvironment(@NonNull String serverEnvironment) {
        Preconditions.checkNotNull(serverEnvironment);

        mServerEnvironment = serverEnvironment;
        return this;
    }
}
