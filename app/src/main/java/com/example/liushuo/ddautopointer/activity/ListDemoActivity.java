package com.example.liushuo.ddautopointer.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liushuo.ddautopointer.Bean.CategoryBean;
import com.example.liushuo.ddautopointer.R;
import com.example.liushuo.ddautopointer.adapter.CategoryAdapter;
import com.luojilab.AutoPointer;
import com.luojilab.DataConfigureImp;
import com.luojilab.base.BaseActivity;
import com.example.liushuo.ddautopointer.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListDemoActivity extends BaseActivity {

    Object[][] data = {
            {2, "GridView Demo", "describe how to use AutoPointer in GridView"},
            {3, "RecyclerView Demo", "describe how to use AutoPointer in RecyclerView"},
            {4, "ViewPager Demo", "describe how to use AutoPointer in ViewPager"},
            {5, "Dialog Demo", "describe how to use AutoPointer in Dialog"},
            {6, "tab layout", "describe how to use AutoPointer in TabLayout"}
    };

    private List<CategoryBean> mData = new ArrayList<>();
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        for (int i = 0, size = data.length; i < size; i++) {
            Object[] arr = data[i];

            CategoryBean categoryBean = new CategoryBean();
            categoryBean.setId((Integer) arr[0]);
            categoryBean.setName((String) arr[1]);
            categoryBean.setDescription((String) arr[2]);
            mData.add(categoryBean);
        }

        CategoryAdapter adapter = new CategoryAdapter(this);
        mBinding.lvEntries.setAdapter(adapter);

        adapter.addAll(mData);

        mBinding.lvEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListDemoActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();

                CategoryBean bean = (CategoryBean) parent.getItemAtPosition(position);
                switch (bean.getId()) {
                    case 6:
                        Intent intent = new Intent(ListDemoActivity.this, TabDemoActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(ListDemoActivity.this, GridViewDemoActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(ListDemoActivity.this, RecyclerViewDemoActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(ListDemoActivity.this, ViewPagerDemoActivity.class);
                        startActivity(intent);
                        break;
                    case 5:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ListDemoActivity.this);
                        builder.setPositiveButton("确定", null);
                        builder.setNegativeButton("取消", null);

                        TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_text, null, false);
                        tv.setTextSize(20);
                        tv.setTextColor(Color.BLACK);
                        tv.setText("click");
                        tv.setGravity(Gravity.CENTER);
                        tv.setWidth(200);
                        tv.setHeight(200);

                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ListDemoActivity.this, "click", Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.setView(tv);

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        DataConfigureImp imp = AutoPointer.wrapWindowCallback(alertDialog);

                        Map<String, String> data = new HashMap<>();
                        data.put("item", "点击我，查看Dialog中自动打点使用");

                        //绑定数据
                        imp.configLayoutData(R.id.text, data);


                        break;
                }
            }
        });
    }
}
