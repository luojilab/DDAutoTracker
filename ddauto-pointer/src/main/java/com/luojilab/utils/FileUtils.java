package com.luojilab.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.common.base.Preconditions;
import com.luojilab.init.AutoPointerInitializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * user liushuo
 * date 2017/4/10
 */

public class FileUtils {


    public static void deleteFileSafely(@NonNull File file) {
        Preconditions.checkNotNull(file);

        if (!file.exists()) return;

        try {
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param file
     * @return 返回null，文件读取失败
     */
    @Nullable
    public static String file2String(@NonNull File file) {
        Preconditions.checkNotNull(file);

        if (!file.exists()) return null;

        FileReader fr = null;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param is
     * @return 返回null，流读取失败
     */
    @Nullable
    public static String stream2String(@NonNull InputStream is) {
        Preconditions.checkNotNull(is);

        InputStreamReader fr = null;
        try {
            fr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(fr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    public static InputStream getAssetsFileStream(@NonNull String filename) {
        Preconditions.checkArgument(!TextUtils.isEmpty(filename));

        Context context = AutoPointerInitializer.getInstance().getAppContext();
        if (context == null) return null;

        try {
            AssetManager am = context.getResources().getAssets();
            return am.open(filename);
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
