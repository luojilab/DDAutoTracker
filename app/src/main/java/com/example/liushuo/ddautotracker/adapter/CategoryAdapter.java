package com.example.liushuo.ddautotracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.example.liushuo.ddautotracker.Bean.CategoryBean;
import com.example.liushuo.ddautotracker.R;
import com.luojilab.base.BaseArrayAdapter;
import com.example.liushuo.ddautotracker.databinding.LayoutListItemBinding;
import com.luojilab.utils.LayoutInflaterWrapper;

/**
 * Created by liushuo on 2017/9/21.
 */

public class CategoryAdapter extends BaseArrayAdapter<CategoryBean> {

    LayoutInflaterWrapper mInflaterWrapper;

    public CategoryAdapter(@NonNull Context context) {
        super(context, R.layout.layout_list_item);

        // todo: 2017/9/21 动态技术修改
        mInflaterWrapper = (LayoutInflaterWrapper) LayoutInflaterWrapper.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutListItemBinding binding;
        if (convertView == null) {
            binding = LayoutListItemBinding.inflate(mInflaterWrapper, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (LayoutListItemBinding) convertView.getTag();
        }
        CategoryBean bean = getItem(position);
        binding.tvCategory.setText(bean.getName());

        return convertView;
    }
}
