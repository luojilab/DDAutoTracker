package com.luojilab.strategy;

import com.google.common.base.Preconditions;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liushuo on 2018/3/11.
 */

public class DataStrategyConfiguration {
    private static DataStrategy recyclerViewStrategy = new RecyclerViewStrategy();
    private static DataStrategy EListViewStrategy = new ExpandableListViewStrategy();
    private static DataStrategy adapterViewStrategy = new AdapterViewStrategy();
    private static DataStrategy viewPagerStrategy = new ViewPagerStrategy();
    private static DataStrategy tabLayoutStrategy = new TabLayoutStrategy();

    private static Map<String, DataStrategy> sStrategies = new HashMap<>();

    static {
        configureDataStrategies();
    }

    private static void configureRecyclerViewDataStrategy(@NonNull String fullClassName) {
        Preconditions.checkNotNull(fullClassName);

        sStrategies.put(fullClassName, recyclerViewStrategy);
    }

    private static void configureExpandableListViewDataStrategy(@NonNull String fullClassName) {
        Preconditions.checkNotNull(fullClassName);

        sStrategies.put(fullClassName, EListViewStrategy);
    }

    private static void configureListViewDataStrategy(@NonNull String fullClassName) {
        Preconditions.checkNotNull(fullClassName);

        sStrategies.put(fullClassName, adapterViewStrategy);
    }

    private static void configureGridViewDataStrategy(@NonNull String fullClassName) {
        Preconditions.checkNotNull(fullClassName);

        sStrategies.put(fullClassName, adapterViewStrategy);
    }

    private static void configureViewPagerDataStrategy(@NonNull String fullClassName) {
        Preconditions.checkNotNull(fullClassName);

        sStrategies.put(fullClassName, viewPagerStrategy);
    }

    private static void configureTabLayoutDataStrategy(@NonNull String fullClassName) {
        Preconditions.checkNotNull(fullClassName);

        sStrategies.put(fullClassName, tabLayoutStrategy);
    }


    public static void configureDataStrategies() {
        //configure RecyclerView and subclass's search strategy
        configureRecyclerViewDataStrategy("RecyclerView");
        configureRecyclerViewDataStrategy("DDCollectionView");

        //ExpandableListView
        configureExpandableListViewDataStrategy("ExpandableListView");
        configureExpandableListViewDataStrategy("DDExpandableListView");

        //ListView
        configureListViewDataStrategy("ListView");
        configureListViewDataStrategy("DDListView");
        configureListViewDataStrategy("ListViewCompat");

        //GridView
        configureGridViewDataStrategy("GridView");
        configureGridViewDataStrategy("DDGridView");

        //ViewPager
        configureViewPagerDataStrategy("ViewPager");

        //TabLayout
        configureTabLayoutDataStrategy("TabLayout");
    }

    public static boolean hasDataStrategy(@NonNull String fullClassName) {
        Preconditions.checkNotNull(fullClassName);

        return sStrategies.containsKey(fullClassName);
    }

    public static DataStrategy getDataStrategy(@NonNull String fullClassName) {
        Preconditions.checkNotNull(fullClassName);

        return sStrategies.get(fullClassName);
    }
}
