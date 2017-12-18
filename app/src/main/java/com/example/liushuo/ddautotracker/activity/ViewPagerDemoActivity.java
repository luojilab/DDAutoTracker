package com.example.liushuo.ddautotracker.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liushuo.ddautotracker.R;
import com.luojilab.base.BaseActivity;
import com.luojilab.widget.adapter.DDPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerDemoActivity extends BaseActivity {

    Object[][] data = {
            {0, "元素1", "这是示例元素1"},
            {1, "元素2", "这是示例元素2"},
            {1, "元素3", "这是示例元素3"}
    };

    private List<View> mViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_demo);

        LayoutInflater layoutInflater = getLayoutInflater();

        for (int i = 0, size = data.length; i < size; i++) {
            Object[] arr = data[i];

            TextView textView = (TextView) layoutInflater.inflate(R.layout.item_text,null,false);
            textView.setTextSize(20);
            textView.setText((String) arr[1]);
            textView.setBackgroundColor(Color.parseColor("#D3D3D3"));

            mViews.add(textView);
        }


        ViewPager vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new DDPagerAdapter() {

            @Override
            public Object getItem(int position) {
                return "position:" + position;
            }

            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, final int position) {
                TextView textView = (TextView) mViews.get(position);
                container.addView(textView);

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "点击item:" + position, Toast.LENGTH_SHORT).show();
                    }
                });

                return textView;
            }
        });
    }
}
