package com.luojilab.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.luojilab.AutoTracker;
import com.luojilab.init.AutoTrackerInitializer;

import org.xutils.common.task.PriorityExecutor;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * user liushuo
 * date 2017/4/10
 */

public class PointConfigsHelper {
    public static final String TAG = AutoTracker.TAG;

    private static PointConfigsHelper sConfigsHelper;

    private static final File CACHE_DIR = AutoTrackerInitializer.getInstance().getAppContext().getExternalCacheDir();
    private static final String FILE_NAME = "da.cfg";


    private PointConfigsHelper() {
    }

    public static PointConfigsHelper getInstance() {

        synchronized (PointConfigsHelper.class) {
            if (sConfigsHelper == null) {
                sConfigsHelper = new PointConfigsHelper();
            }
        }

        return sConfigsHelper;
    }

    /**
     * {
     * "version": "2.7.1",
     * "configs": [
     * {
     * "ctr_id": "ColumnVC_ArticleIcon",
     * "event_id": "article_into",
     * "position": "column_list",
     * "params": [
     * {
     * "p": "article_id"
     * }
     * ]
     * },
     * {
     * "ctr_id": "ColumnVC_ColumnBuyButton",
     * "event_id": "column_buy",
     * "position": "column_page",
     * "params": [
     * {
     * " p": "column_id"
     * },
     * {
     * "p": "column_type"
     * }
     * ]
     * }
     * ]
     * }
     *
     * @return
     */
    @NonNull
    public static List<JsonObject> readConfigFile() {
        File file = new File(CACHE_DIR, FILE_NAME);
        List<JsonObject> list = file2JsonList(file);
        if (list != null) return list;

        return readAssetsConfigFile();
    }

    /**
     * 读取asset下的配置文件，防止由于配置下拉失败
     * 可能导致的无法发送埋点情况
     *
     * @return
     */
    @NonNull
    private static List<JsonObject> readAssetsConfigFile() {
        Context context = AutoTrackerInitializer.getInstance().getAppContext();
        if (context == null) return Collections.emptyList();

        try {
            InputStream is = context.getResources().getAssets().open(FILE_NAME);
            if (is == null) return Collections.emptyList();

            List<JsonObject> list = stream2JsonList(is);

            return list == null ? Collections.<JsonObject>emptyList() : list;
        } catch (Exception e) {
            e.printStackTrace();

            return Collections.emptyList();
        }
    }

    /**
     * @param content
     * @return 返回null，无法正确解析配置，返回empty list，没有配置内容
     */
    @Nullable
    private static List<JsonObject> string2JsonList(@NonNull String content) {

        if (TextUtils.isEmpty(content)) return Collections.emptyList();

        JsonObject json = CoreUtils.parseJsonObject(content);
        if (json == null) return null;

        Gsonner gsonner = Gsonner.createGsonner(json);
        JsonArray arr = gsonner.getJsonArray("configs");
        if (arr == null) return null;

        List<JsonObject> list = new ArrayList<>();
        for (int i = 0, count = arr.size(); i < count; i++) {
            JsonObject obj = (JsonObject) arr.get(i);
            list.add(obj);
        }

        return list;
    }

    /**
     * @param file
     * @return 返回null，无法正确解析配置，返回empty list，没有配置内容
     */
    @Nullable
    private static List<JsonObject> file2JsonList(@NonNull File file) {

        String content = FileUtils.file2String(file);
        if (content == null) return null;

        return string2JsonList(content);
    }

    /**
     * @param is
     * @return 返回null，无法正确解析配置，返回empty list，没有配置内容
     */
    @Nullable
    private static List<JsonObject> stream2JsonList(@NonNull InputStream is) {
        String content = FileUtils.stream2String(is);
        if (content == null) return null;

        return string2JsonList(content);
    }


    public void downloadConfigFile(@NonNull final String down_url, @NonNull final String md5, final FileDownloadCallback callback) {
        Preconditions.checkArgument(!TextUtils.isEmpty(down_url));
        Preconditions.checkArgument(!TextUtils.isEmpty(md5));

        //判断assets目录中的文件md5是否与服务器返回的md5相同
        InputStream assetsStream = FileUtils.getAssetsFileStream(FILE_NAME);
        if (assetsStream != null) {
            String oldMd5 = FileMd5Util.getMD5(assetsStream);

            boolean isNoNull = oldMd5 != null;
            boolean equal = TextUtils.equals(oldMd5, md5);

            if (isNoNull && equal) {
                DDLogger.d(TAG, "配置文件内容未发生变化，不下载文件，使用asset中的文件...");
                return;
            }
        }

        //判断本地目录中的文件md5是否与服务器返回的md5相同
        File file = new File(CACHE_DIR, FILE_NAME);
        if (file.exists()) {

            String oldMd5 = FileMd5Util.getMD5(file);

            boolean isNoNull = oldMd5 != null;
            boolean equal = TextUtils.equals(oldMd5, md5);

            if (isNoNull && equal) {
                DDLogger.d(TAG, "配置文件内容未发生变化，不下载文件,使用本地目录中的文件...");
                return;
            }
        }


        //创建下载临时文件，防止污染已下载成功的文件内容
        final File tmpFile = new File(CACHE_DIR, FILE_NAME + ".tmp");

        //xtils处理下载
        RequestParams params = new RequestParams(down_url);
        params.setExecutor(new PriorityExecutor(2, true));
        params.setSaveFilePath(tmpFile.getAbsolutePath());
        params.setCancelFast(false);
        params.setAutoResume(false);

        SimpleCallback requestCallback = new SimpleCallback() {
            @Override
            public void onSuccess(File result) {
                if (result == null) return;

                handleDownloadSuccess(result, md5, callback, down_url);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                handleDownloadError(callback, down_url);
            }
        };

        x.http().request(HttpMethod.GET, params, requestCallback);

        DDLogger.d(TAG, "开始下载配置文件...");
    }

    private void handleDownloadError(FileDownloadCallback callback, @NonNull String down_url) {
        if (callback == null) {
            DDLogger.d(TAG, "埋点配置文件下载失败");
            return;
        }

        callback.onComplete(null, down_url);
    }

    /**
     * @param result
     * @param md5      已验证不为空
     * @param callback
     * @param down_url 已验证不为空
     */
    private void handleDownloadSuccess(@NonNull File result, @NonNull String md5, @Nullable FileDownloadCallback callback, @NonNull String down_url) {

        boolean isIntegrity = checkFileIntegrity(md5, result);
        if (!isIntegrity) {

            FileUtils.deleteFileSafely(result);
            callback.onComplete(null, down_url);

            DDLogger.d(TAG, "配置文件没有通过完整性检查...");
            return;
        }


        File file = new File(CACHE_DIR, FILE_NAME);

        boolean success = false;
        try {
            success = result.renameTo(file);
        } catch (Exception e) {
            DDLogger.e(e, null);
        }

        if (!success) {
            FileUtils.deleteFileSafely(result);
            FileUtils.deleteFileSafely(file);
        }

        if (callback == null) {
            DDLogger.d(TAG, "埋点配置文件下载成功");
            return;
        }

        file = (success ? file : null);
        callback.onComplete(file, down_url);

        DDLogger.d(TAG, "配置文件下载成功...");
    }


    /**
     * @param md5     已检查不为空
     * @param tmpFile
     * @return
     */
    private boolean checkFileIntegrity(@NonNull String md5, @NonNull File tmpFile) {

        if (!tmpFile.exists()) return false;

        String fileMd5 = FileMd5Util.getMD5(tmpFile);
        return TextUtils.equals(fileMd5, md5);

    }

    public interface FileDownloadCallback {
        void onComplete(File file, String url);
    }
}

class SimpleCallback implements org.xutils.common.Callback.ProgressCallback<File> {
    @Override
    public void onWaiting() {

    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onLoading(long total, long current, boolean isDownloading) {

    }

    @Override
    public void onSuccess(File result) {

    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {

    }

    @Override
    public void onCancelled(CancelledException cex) {

    }

    @Override
    public void onFinished() {

    }
}
