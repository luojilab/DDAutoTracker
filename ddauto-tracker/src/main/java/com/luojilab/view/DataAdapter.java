package com.luojilab.view;

/**
 * Created by liushuo on 2017/6/9.
 * 自定义布局的数据适配器,泛型T表示该自定义布局中操作的数据bean类型
 */

public interface DataAdapter<T> {
    T getData();

    void setData(T data);
}
