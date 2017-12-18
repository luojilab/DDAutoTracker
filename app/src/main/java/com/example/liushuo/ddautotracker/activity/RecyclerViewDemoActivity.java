package com.example.liushuo.ddautotracker.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.liushuo.ddautotracker.Bean.CategoryBean;
import com.example.liushuo.ddautotracker.R;
import com.example.liushuo.ddautotracker.databinding.LayoutListItemBinding;
import com.luojilab.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewDemoActivity extends BaseActivity {

    Object[][] data = {
            {0, "元素1", "这是示例元素1"},
            {1, "元素2", "这是示例元素2"},
            {2, "元素3", "这是示例元素3"},
            {3, "元素4", "这是示例元素4"},
            {4, "元素5", "这是示例元素5"}
    };

    private List<CategoryBean> mData = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_demo);

        for (int i = 0, size = data.length; i < size; i++) {
            Object[] arr = data[i];

            CategoryBean categoryBean = new CategoryBean();
            categoryBean.setId((Integer) arr[0]);
            categoryBean.setName((String) arr[1]);
            categoryBean.setDescription((String) arr[2]);
            mData.add(categoryBean);
        }


        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, RecyclerView.HORIZONTAL));
        rv.setAdapter(new MyAdapter(this));
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private LayoutInflater mInflater;
        private Context mContext;

        public MyAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutListItemBinding.inflate(mInflater, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            CategoryBean bean = mData.get(position);

            holder.bindData(bean);

        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            LayoutListItemBinding mBinding;

            public MyViewHolder(LayoutListItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "点击item:"+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            public void bindData(CategoryBean bean) {
                mBinding.tvCategory.setText(bean.getName());
            }

        }
    }
}
