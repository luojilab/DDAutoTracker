package com.luojilab.reqeust;

import com.luojilab.AutoPointer;
import com.luojilab.bean.PointConfig;

/**
 * user liushuo
 * date 2017/4/8
 * 程序启动，需要发送请求，获取埋点配置信息及下载埋点配置文件
 * 埋点配置的格式，见 {@link PointConfig}
 */

public class PullPointConfigRequest {
    private static final String TAG = AutoPointer.TAG;

    private static final String URL = "url";
    private static final String MD5 = "md5";

    /**
     @Override public void onRetrofitResponse(@NonNull JsonObject header, @NonNull JsonElement content) {
     super.onRetrofitResponse(header, content);

     //请求埋点配置返回
     JsonObject objectResult = (JsonObject) content;

     Gsonner gsonner = Gsonner.createGsonner(objectResult);

     //埋点配置文件的地址
     final String url = gsonner.getAsString(URL);
     //埋点配置文件md5值，用于完整性验证
     final String md5 = gsonner.getAsString(MD5);

     if (TextUtils.isEmpty(url) || TextUtils.isEmpty(md5)) return;

     new Thread() {
     @Override public void run() {
     super.run();

     PointConfigsHelper.FileDownloadCallback callback = new PointConfigsHelper.FileDownloadCallback() {
     @Override public void onComplete(@Nullable File file, @NonNull String url) {
     if (file == null) return; //file 为null，则下载文件失败

     //埋点配置下载成功，同步到内存
     PointGenerator.getInstance().syncLocalConfigs();
     }
     };

     //开始下载埋点配置文件
     PointConfigsHelper.getInstance().downloadConfigFile(url, md5, callback);

     }
     }.start();
     }
     **/
}
