package com.luojilab;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.google.common.base.Preconditions;
import com.luojilab.strategy.AdapterViewStrategy;
import com.luojilab.strategy.DataStrategy;
import com.luojilab.strategy.ExpandableListViewStrategy;
import com.luojilab.strategy.RecyclerViewStrategy;
import com.luojilab.strategy.TabLayoutStrategy;
import com.luojilab.strategy.ViewPagerStrategy;
import com.luojilab.utils.DDLogger;
import com.luojilab.utils.ViewHelper;
import com.luojilab.view.DataAdapter;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * user liushuo
 * date 2017/4/6
 */

public class WindowCallbackWrapper extends SimpleWindowCallback implements DataConfigureImp {
    public static final String TAG = AutoTracker.TAG;

    private WeakReference<View> mViewRef;

    private static Map<String, DataStrategy> mStrategies = new HashMap<>();

    static {
        //configure RecyclerView and subclass's search strategy
        DataStrategy recyclerViewStrategy = new RecyclerViewStrategy();
        mStrategies.put("RecyclerView", recyclerViewStrategy);
        mStrategies.put("DDCollectionView", recyclerViewStrategy);

        //ExpandableListView
        DataStrategy EListViewStrategy = new ExpandableListViewStrategy();
        mStrategies.put("ExpandableListView", EListViewStrategy);
        mStrategies.put("DDExpandableListView", EListViewStrategy);

        DataStrategy adapterViewStrategy = new AdapterViewStrategy();
        //ListView
        mStrategies.put("ListView", adapterViewStrategy);
        mStrategies.put("DDListView", adapterViewStrategy);
        mStrategies.put("ListViewCompat", adapterViewStrategy);

        //GridView
        mStrategies.put("GridView", adapterViewStrategy);
        mStrategies.put("DDGridView", adapterViewStrategy);

        //ViewPager
        DataStrategy viewPagerStrategy = new ViewPagerStrategy();
        mStrategies.put("ViewPager", viewPagerStrategy);

        //TabLayout
        DataStrategy tabLayoutStrategy = new TabLayoutStrategy();
        mStrategies.put("TabLayout", tabLayoutStrategy);

    }

    private Map<Integer, Object> mDataLayout = new HashMap<>();

    //通过view实例的hash code决定忽略指定view的自动打点功能
    private Set<Integer> mIgnoreViews = new HashSet<>(0);

    /**
     * @param view     用于查找TouchTarget
     * @param callback 用于传递触摸事件,传null，不传递事件
     */
    public WindowCallbackWrapper(@NonNull View view, @Nullable Window.Callback callback) {
        super(callback);
        mViewRef = new WeakReference<>(view);
    }

    @Override
    public void ignoreAutoTrack(@NonNull View view) {
        Preconditions.checkNotNull(view);

        int viewHashCode = view.hashCode();
        mIgnoreViews.add(viewHashCode);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!AutoTracker.isAutoTrackEnable()) {
            return super.dispatchTouchEvent(ev);
        }

        int actionMasked = ev.getActionMasked();

        if (actionMasked != MotionEvent.ACTION_UP) {
            return super.dispatchTouchEvent(ev);
        }

        long t = System.currentTimeMillis();
        analyzeMotionEvent();

        //非线上版本，打印执行时间
        if (!AutoTracker.isOnlineEnv()) {
            long time = System.currentTimeMillis() - t;
            DDLogger.d(TAG, String.format(Locale.CHINA, "处理时间:%d 毫秒", time));
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 配制自定义布局的数据绑定关系，自定义布局内的任何
     * 控件发生点击行为时，发送的埋点都会携带改数据
     *
     * @param id
     * @param object
     * @return
     */
    @NonNull
    @Override
    public DataConfigureImp configLayoutData(@IdRes int id, @NonNull Object object) {
        Preconditions.checkNotNull(object);

        mDataLayout.put(id, object);
        return this;
    }

    /**
     * 分析用户的点击行为
     */
    private void analyzeMotionEvent() {
        if (mViewRef == null || mViewRef.get() == null) {
            DDLogger.e(TAG, "window is null");
            return;
        }

        ViewGroup decorView = (ViewGroup) mViewRef.get();
        int content_id = android.R.id.content;
        ViewGroup content = (ViewGroup) decorView.findViewById(content_id);
        if (content == null) {
            content = decorView; //对于非Activity DecorView 的情况处理
        }

        Pair<View, Object> targets = findActionTargets(content);
        if (targets == null) {
            DDLogger.e(TAG, "has no action targets!!!");
            return;
        }

        //发送任务在单线程池中
        int hashcode = targets.first.hashCode();
        if (mIgnoreViews.contains(hashcode)) return;

        TrackerExecutor.getHandler().post(TrackPostAction.create(targets.first, targets.second));
    }

    /**
     * @param root 必然非null
     * @return null，找不到touchtarget相关数据
     */
    @Nullable
    private Pair<View, Object> findActionTargets(@NonNull ViewGroup root) {

        View touchTarget;

        View strategyView = null;
        DataStrategy strategy = null;

        View configDataView = null;
        int configId = -1;

        DataAdapter dataAdapter = null;

        ViewGroup vg = root;
        while (true) {
            touchTarget = ViewHelper.findTouchTarget(vg);

            //无法找到touchTarget 相关信息
            if (touchTarget == null) return null;

            int vId = touchTarget.getId();
            if (mDataLayout.containsKey(vId)) {

                configDataView = touchTarget;
                configId = vId;

                //互斥操作
                if (strategyView != null) {
                    strategyView = null;
                    strategy = null;
                }

                if (dataAdapter != null) {
                    dataAdapter = null;
                }
            }

            if (touchTarget instanceof DataAdapter) {
                dataAdapter = (DataAdapter) touchTarget;

                //互斥操作
                if (strategyView != null) {
                    strategyView = null;
                    strategy = null;
                }

                if (configDataView != null) {
                    configDataView = null;
                    configId = -1;
                }
            }

            String clsName = touchTarget.getClass().getSimpleName();
            if (mStrategies.containsKey(clsName)) {

                strategyView = touchTarget;
                strategy = mStrategies.get(clsName);

                //互斥操作
                if (configDataView != null) {
                    configDataView = null;
                    configId = -1;
                }

                if (dataAdapter != null) {
                    dataAdapter = null;
                }
            }

            //已经找到touchTarget
            if (touchTarget == vg) break;

            boolean isVG = touchTarget instanceof ViewGroup;
            //已经找到touchTarget
            if (!isVG) break;

            //特殊处理TabLayout 的情况
            if (touchTarget instanceof TabLayout) {
                break;
            }

            //未找到touchTarget
            vg = (ViewGroup) touchTarget;
        }

        if (strategyView != null) {
            Object data = strategy.fetchTargetData(strategyView);

            return Pair.create(touchTarget, data);
        }

        if (configDataView != null) {
            return Pair.create(touchTarget, mDataLayout.get(configId));
        }

        //解决自定义布局的数据绑定问题
        if (dataAdapter != null) {
            return Pair.create(touchTarget, dataAdapter.getData());
        }

        return Pair.create(touchTarget, null);
    }

}