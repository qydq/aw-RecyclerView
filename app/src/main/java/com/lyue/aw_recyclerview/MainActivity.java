package com.lyue.aw_recyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.Toast;

import com.lyue.utils.DividerItemDecoration;
import com.lyue.utils.RefreshFootAdapter;
import com.lyue.utils.RefreshRecyclerAdater;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RefreshFootAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private RefreshRecyclerAdater sadapter;
    private RecyclerView mRecyclerView;
    private int lastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aar_activity_recycler_main);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //设置swipeRefreshLayout颜色，android系统提供可以设置4中。
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //添加自定义颜色高度的分隔线
//        mRecyclerView.addItemDecoration(new DRecycleViewDivider(this, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.CommColorLine)));
        //添加自定义drawable的分隔线
//        mRecyclerView.addItemDecoration(new DRecycleViewDivider(this, LinearLayoutManager.VERTICAL, R.drawable.aar_qrcode_scan_line));
        //添加系统默认的分隔线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        adapter = new RefreshFootAdapter(this);
        mRecyclerView.setAdapter(adapter);
//设置下拉刷新的事件监听。
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> newDatas = new ArrayList<String>();
                        for (int i = 0; i < 5; i++) {
                            int index = i + 1;
                            newDatas.add("new item" + index);
                        }
                        adapter.addItem(newDatas);
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "更新了五条数据...", Toast.LENGTH_SHORT).show();
                        //通知更新RecyclerView中的数据，以防止不更新。
                        adapter.notifyDataSetChanged();
                    }
                }, 5000);
            }
        });
//设置上拉加载的事件监听。
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
                    adapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> newDatas = new ArrayList<String>();
                            for (int i = 0; i < 5; i++) {
                                int index = i + 1;
                                newDatas.add("more item" + index);
                            }
                            adapter.addMoreItem(newDatas, RefreshFootAdapter.PULLUP_LOAD_MORE);
                        }
                    }, 2500);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
//        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount()) {
//                    adapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            List<String> newDatas = new ArrayList<String>();
//                            for (int i = 0; i < 5; i++) {
//                                int index = i + 1;
//                                newDatas.add("more item" + index);
//                            }
//                            adapter.addMoreItem(newDatas, RefreshFootAdapter.PULLUP_LOAD_MORE);
//                        }
//                    }, 2500);
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
//            }
//        });
    }
}
