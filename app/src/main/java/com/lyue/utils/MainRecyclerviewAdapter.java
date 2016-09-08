package com.lyue.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lyue.aw_recyclerview.R;


/**
 * Just Simple Author Adapter
 * <p/>最简单的Adapter
 * Created by Clock on 2016/8/24.
 */
public class MainRecyclerviewAdapter extends RecyclerView.Adapter<MainRecyclerviewAdapter.SimpleViewHolder> {

    private final View.OnClickListener mSimpleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Context context = v.getContext();
//            Intent intent = new Intent(context, TestActivity.class);
//            context.startActivity(intent);
//            AboutAAR aar = new AboutAAR();
            Toast.makeText(context, "vsd" + "", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.aar_item_recycler_adapter_refresh, parent, false);
        return new SimpleViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {
        holder.itemView.setOnClickListener(mSimpleClickListener);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
