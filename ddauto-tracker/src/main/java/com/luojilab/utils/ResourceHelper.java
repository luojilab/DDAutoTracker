package com.luojilab.utils;

import android.content.Context;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.luojilab.ddpointable_widget.R;
import com.google.common.base.Preconditions;

import java.util.Locale;

/**
 * user liushuo
 * date 2017/4/6
 */

public class ResourceHelper {

    @Nullable
    public static String getGlobalIdName(@NonNull View view) {
        Preconditions.checkNotNull(view);

        int id = view.getId();
        /*LayoutInflaterWrapper会为根View添加如下tag值*/
        String idNameSpace = (String) view.getTag(R.id.id_namespace_tag);
        boolean isRootView = !TextUtils.isEmpty(idNameSpace);
        if (id == View.NO_ID && !isRootView) {
            //this view has no id assigned
            return null;
        }

        try {
            Context context = view.getContext();

            String activityName = context.getClass().getSimpleName();
            String layoutFileName = getLayoutFileName(view);
            String idName;
            if (id == View.NO_ID) {
                /*如果View 无id，且该View是布局文件的根View,将
                * 文件名作为View的唯一标识*/
                idName = String.format(Locale.CHINA, "root_id:%s", layoutFileName);
            } else {
                idName = getResourceEntryName(context, id);
            }

            return String.format("%s_%s_%s", activityName, layoutFileName, idName);
        } catch (Exception e) {
            e.printStackTrace();

            //error occur when fetch id resource
            return null;
        }

    }

    /**
     * @param view
     * @return 返回值不区分 ""和null，统一返回""
     */
    @NonNull
    private static String getLayoutFileName(@NonNull View view) {

        String idNameSpace = (String) view.getTag(R.id.id_namespace_tag);
        if (!TextUtils.isEmpty(idNameSpace)) return idNameSpace;

        View tmp = view;
        while (tmp.getParent() != null && (tmp.getParent() instanceof View)) {
            View parent = (View) tmp.getParent();

            String space = (String) parent.getTag(R.id.id_namespace_tag);
            if (!TextUtils.isEmpty(space)) return space;

            tmp = parent;
        }

        return "";
    }

    /**
     * @param context
     * @param id
     * @return 返回值不区分 ""和null，统一返回""
     */
    @NonNull
    public static String getResourceEntryName(@NonNull Context context, @AnyRes int id) {
        Preconditions.checkNotNull(context);

        try {
            String name = context.getResources().getResourceEntryName(id);
            return name;
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
    }
}
