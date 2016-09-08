package com.lyue.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lyue.aw_recyclerview.R;

import java.util.ArrayList;
import java.util.List;

/**********************************************************
 * @文件名称：RefreshRecyclerAdater
 * @文件作者：孙顺涛
 * @Email：staryumou@163.com邮箱已改qyddai@gmail.com
 * @创建时间：2016/9/8
 * @文件描述：该类实现了Recyclerview上拉刷新下拉加载贡多的功能，但是没有FooterView。
 * @修改历史：2016/9/8
 **********************************************************/
public class RefreshRecyclerAdater extends RecyclerView.Adapter<RefreshRecyclerAdater.ViewHolder> {
    private LayoutInflater mInflater;
    private List<String> mTitles = null;

    public RefreshRecyclerAdater(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mTitles = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            int index = i + 1;
            mTitles.add("item" + index);
        }
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.aar_item_recycler_adapter_refresh, parent, false);
        //这边可以做一些属性设置，甚至事件监听绑定
        //view.setBackgroundColor(Color.RED);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_tv.setText(mTitles.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item_tv;

        public ViewHolder(View view) {
            super(view);
            item_tv = (TextView) view.findViewById(R.id.tvTitle);
        }
    }

    //添加数据
    public void addItem(List<String> newDatas) {
        //mTitles.add(position, data);
        //notifyItemInserted(position);
        newDatas.addAll(mTitles);
        mTitles.removeAll(mTitles);
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }

    public void addMoreItem(List<String> newDatas) {
        mTitles.addAll(newDatas);
        notifyDataSetChanged();
    }
}