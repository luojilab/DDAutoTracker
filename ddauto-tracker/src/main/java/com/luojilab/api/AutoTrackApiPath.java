package com.luojilab.api;

/**
 * user liushuo
 * date 2017/4/12
 */

public interface AutoTrackApiPath {
    //调试接口 发送view id+全量数据
    String POINT_DEBUG = "/dasdk/xxxxxxxx/ctr.do";
    //下拉埋点配置接口
    String PULL_POINT_CONFIGS = "/dasdk/xxxxxxx/dacfg.do";
    //调试nlog接口路径
    String OFFLINE_NLOG_PATH = "/xxxxxx/logsSdk.do";
    //线上nlog发送路径
    String ONLINE_NLOG_PATH = "/logsSdk.do";

}
