package com.example.liushuo.ddautopointer.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.liushuo.ddautopointer.Bean.CategoryBean;
import com.example.liushuo.ddautopointer.R;
import com.example.liushuo.ddautopointer.adapter.CategoryAdapter;
import com.luojilab.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GridViewDemoActivity extends BaseActivity {

    Object[][] data = {
            {0, "元素1", "这是示例元素1"},
            {1, "元素2", "这是示例元素2"},
            {1, "元素3", "这是示例元素3"},
            {1, "元素4", "这是示例元素4"},
            {1, "元素5", "这是示例元素5"}
    };

    private List<CategoryBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_demo);

        GridView gv = (GridView) findViewById(R.id.gv);

        for (int i = 0, size = data.length; i < size; i++) {
            Object[] arr = data[i];

            CategoryBean categoryBean = new CategoryBean();
            categoryBean.setId((Integer) arr[0]);
            categoryBean.setName((String) arr[1]);
            categoryBean.setDescription((String) arr[2]);
            mData.add(categoryBean);
        }

        CategoryAdapter adapter = new CategoryAdapter(this);
        gv.setAdapter(adapter);

        adapter.addAll(mData);

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GridViewDemoActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
