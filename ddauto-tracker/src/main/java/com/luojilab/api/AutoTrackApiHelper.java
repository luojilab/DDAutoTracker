package com.luojilab.api;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.luojilab.AutoTracker;
import com.luojilab.utils.DDLogger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Created by liushuo on 2017/5/19.
 */

public class AutoTrackApiHelper {

    /*测试环境、仿真环境、正式环境 发送埋点需要使用的域名地址*/
    private static final String DEBUG_POINTER_ADDRESS = "http://xxx.xxx.xx.xxx:8080";
    private static final String SIMULATION_POINTER_ADDRESS = "http://xxx.xxx.xx.xxx";
    private static final String ONLINE_POINTER_ADDRESS = "http://xxx.xxx.xx.xxx";

    /*测试环境、仿真环境、正式环境 请求埋点配置需要使用的域名地址*/
    private static final String DEBUG_PULL_CONFIG_ADDRESS = "http://xxx.xxx.xx.xxx:8080";
    private static final String SIMULATION_PULL_CONFIG_ADDRESS = "http://xxx.xxx.xx.xxx";
    private static final String ONLINE_PULL_CONFIG_ADDRESS = "http://xxx.xxx.xx.xxx";

    private static final String[][] sDomains = new String[][]{
            //打点地址，请求配置地址

            //测试环境
            {DEBUG_POINTER_ADDRESS, DEBUG_PULL_CONFIG_ADDRESS},
            //仿真环境
            {SIMULATION_POINTER_ADDRESS, SIMULATION_PULL_CONFIG_ADDRESS},
            //线上环境
            {ONLINE_POINTER_ADDRESS, ONLINE_PULL_CONFIG_ADDRESS}
    };

    private static final int POINTER_DOMAIN_INDEX = 0;
    private static final int PULL_CONFIG_DOMAIN_INDEX = 1;

    private AutoTrackApiHelper() {
    }

    /**
     * 获取发送埋点信息的地址
     *
     * @param apiPath 传入空值，认为是非法调用，抛异常提示
     * @return
     */
    @NonNull
    public static String getPointerApiUrl(String apiPath) {
        checkArgument(!TextUtils.isEmpty(apiPath), "apiPath cannot be empty");

        AutoTracker.SERVER_ENV serverEnv = AutoTracker.getServerEnv();
        DDLogger.d(AutoTracker.TAG, "环境:" + serverEnv);

        String netApiAddress = sDomains[serverEnv.ordinal()][POINTER_DOMAIN_INDEX];
        return netApiAddress + apiPath;
    }

    /**
     * 获取拉取埋点配置的地址
     *
     * @param apiPath 传入空值，认为是非法调用，抛异常提示
     * @return
     */
    @NonNull
    public static String getPullConfigUrl(String apiPath) {
        checkArgument(!TextUtils.isEmpty(apiPath), "apiPath cannot be empty");

        AutoTracker.SERVER_ENV serverEnv = AutoTracker.getServerEnv();
        DDLogger.d(AutoTracker.TAG, "环境:" + serverEnv);

        String netApiAddress = sDomains[serverEnv.ordinal()][PULL_CONFIG_DOMAIN_INDEX];
        return netApiAddress + apiPath;
    }


}
