# aw-RecyclerView
This is aw project about how to setting background in android operator system in java code

test：

## 特别说明

    在github上更新代码，不要更新无用的代码，应该做到每一份代码做好相应的README.md，README.md必须要有该项目的功能，再上传至github上面。该awRecyclerView项目为以后上传项目时的一个参考项目，格式严格按照这个标准去实现。下面列出上传项目必要的规范

	时间说明：
	
	创建时间：2016年08月29日;最近修改时间：2016年08月30日。
	
	Tips 
	
1。前言（包含该项目主要实现的功能的简短说明，运行配置；可选）。

2。实现效果（如果没有可以省略，但是建议要包含，因为项目以后自己看到的时候会帮助自己理解）。

3。思路或步骤（代码）。

4。重要知识点（总结，思考）。

5。内容参考（尊重原创）。

6。联系作者。

## -----------------------------woshifengexian------------------------------------##


    创建时间：2016年09月08日;最近修改时间：2016年09月08日。
	
	Tips ： null
	
## -----------------------------woshifengexian------------------------------------##	

## 前言

******************github不应该介绍这么详细，决定今后更博的情况，以个人知乎为详细的讲解（简书不用了），github作为代码仓库简单介绍。***********

****************** 个人知乎主页https://zhuanlan.zhihu.com/qydda 请大家多多支持  *************************************************************

   话说RecyclerView已经面市很久，也在很多应用中得到广泛的使用，在整个开发者圈子里面也拥有很不错的口碑，那说明RecyclerView拥有比ListView,GridView之类控件有很多的优点，例如:数据绑定，Item View创建,View的回收以及重用等机制。
用了这么久，今天也开始封装这个类，在封装的时候写一个demo出来给大家参考一起进步。

该项目主要实现MD RecyclerView 的新功能。

1。RecyclerView+SwpieRefreshLayout实现下拉刷新效果:下拉刷新的功能。（自带技能）
2。实现了ItemView的监听。（View.OnClickListener）
3。RecyclerView设置滚动事件加入上拉加载更多功能。（RefreshRecyclerAdater）
4。升级RecyclerView加入FootView实现上拉加载。（RefreshFootAdapter）
5。普通RecyclerView的使用。（MainRecyclerviewAdapter）

项目配置《分别导入不同的Adapter括号后面的Adapter既可以实现上面五点分别对应的功能，也可以下载源码）

下面会对重要的类和知识点进行讲解。

example 引用列子：

	

## 实现效果

这里说明一下：

再显示数据的时候，需要添加下面的代码。

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
	
效果图 ：-->

| ![](https://github.com/qydq/aw-RecyclerView/blob/master/screenpic/ezgif-1770885463.gif)| 

## 思路和步骤。

1。SwipeRefrshLayout是Google官方更新的一个Widget,可以实现下拉刷新的效果。该控件集成自ViewGroup在support-v4兼容包下，不过我们需要升级supportlibrary的版本到19.1以上。基本使用的方法如下:
setOnRefreshListener(OnRefreshListener):添加下拉刷新监听器
setRefreshing(boolean):显示或者隐藏刷新进度条
isRefreshing():检查是否处于刷新状态
setColorSchemeResources():设置进度条的颜色主题，最多设置四种，setColorScheme()方法已经弃用了。
如上面说明的需要添加的代码。

	
2。SwipeRefreshLayout本身自带下拉刷新的效果，那么我们可以选择在RecyclerView布局外部嵌套一层SwipeRefreshLayout布局即可。

<android.support.v4.widget.SwipeRefreshLayout
        android:id="@+id/swipeRefreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="vertical">
        <!-- cacheColorHint：系统默认拖动过程中列表背景是黑的,设置为透明 -->
        <android.support.v7.widget.RecyclerView
            android:id="@+id/recycler_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:overScrollMode="never" />
    </android.support.v4.widget.SwipeRefreshLayout>

3。接着在Activity中获取SwipeRefreshLayout控件并且设置OnRefreshListener监听器，同时实现里边的onRefresh()方法，在该方法中进行网络请求最新数据，然后刷新RecyclerView列表同时设置SwipeRefreshLayout的进度Bar的隐藏或者显示效果。具体代码如下。

// mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
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
		
4。Java代码。

因为有四种Adapter的实现方式，这里只贴出所有的注释，下面给出RefreshFootAdapter的完整代码，并进行分析。

1）RefreshRecyclerAdater

/**********************************************************
 * @文件名称：RefreshRecyclerAdater
 * @文件作者：孙顺涛
 * @Email：staryumou@163.com邮箱已改qyddai@gmail.com
 * @创建时间：2016/9/8
 * @文件描述：该类实现了Recyclerview上拉刷新下拉加载贡多的功能，但是没有FooterView。
 * @修改历史：2016/9/8
 **********************************************************/
 代码里面已经备注的很详细了。
 
2）RefreshFootAdapter实现思路和步骤。

a.getItemType(),这个就和ListView的Adapter的实现差不多了，那么我们这边可以使用多套布局给RecyclerView加入一个FootView布局即可。
RefreshFootAdapter.java具体实现流程如下: 加入布局状态标志-用来判断此时加载是普通Item还是foot view。

b.重写getItemCount()方法,返回的Item数量在数据的基础上面+1，增加一项FootView布局项。
public intgetItemCount() {
        return mTitles.size()+1;
    }

c.重写getItemViewType方法来判断返回加载的布局的类型.
public int getItemViewType(int position) {
    // 最后一个item设置为footerView
    if (position + 1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
            }
        }
d.接着onCreateViewHolder(ViewGroup parent,int viewType)加载布局的时候根据viewType的类型来选择指定的布局创建，返回即可。
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if(viewType==TYPE_ITEM){
            Viewview=mInflater.inflate(R.layout.item_recycler_layout,parent,false);
            //这边可以做一些属性设置，甚至事件监听绑定
           //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder=new ItemViewHolder(view);
            return itemViewHolder;
        }else if(viewType==TYPE_FOOTER){
            Viewfoot_view=mInflater.inflate(R.layout.recycler_load_more_layout,parent,false);
            //这边可以做一些属性设置，甚至事件监听绑定
           //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder=new FootViewHolder(foot_view);
            return footViewHolder;
        }
       return null;
    }

e.最后进行判断数据的时候(onBindViewHolder)，判断holder的类型来进行判定数据即可。
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder) {
           ((ItemViewHolder)holder).item_tv.setText(mTitles.get(position));
            holder.itemView.setTag(position);
        }else if(holder instanceof FootViewHolder){
            FootViewHolderfootViewHolder=(FootViewHolder)holder;
            switch (load_more_status){
                case PULLUP_LOAD_MORE:
                   footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                   footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
            }
        }
    }
	
类的完整代码，如下：

说明：private static final int TYPE_ITEM =0;  //普通Item View 
   private static final intTYPE_FOOTER = 1;  //顶部FootView 

/**********************************************************
 * @文件名称：RefreshFootAdapter
 * @文件作者：孙顺涛
 * @Email：staryumou@163.com邮箱已改qyddai@gmail.com
 * @创建时间：2016/9/8
 * @文件描述：该Adapter实现上拉加载和下拉刷新的功能（自定义了FooterView布局）
 * @修改历史：2016/9/8
 **********************************************************/

public class RefreshFootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private List<String> mTitles = null;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView

    public RefreshFootAdapter(Context context) {
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.aar_item_recycler_adapter_footview, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view);
            return itemViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.aar_item_recycler_loadmore, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);
            return footViewHolder;
        }
        return null;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).item_tv.setText(mTitles.get(position));
            holder.itemView.setTag(position);
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (load_more_status) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.foot_view_item_tv.setText("上拉加载更多...");
                    break;
                case LOADING_MORE:
                    footViewHolder.foot_view_item_tv.setText("正在加载更多数据...");
                    break;
            }
        }
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        return mTitles.size() + 1;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView item_tv;

        public ItemViewHolder(View view) {
            super(view);
            item_tv = (TextView) view.findViewById(R.id.item_tv);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView foot_view_item_tv;

        public FootViewHolder(View view) {
            super(view);
            foot_view_item_tv = (TextView) view.findViewById(R.id.foot_view_item_tv);
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

    public void addMoreItem(List<String> newDatas, int status) {
        mTitles.addAll(newDatas);
        changeMoreStatus(status);
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}

3。MainRecyclerviewAdapter。

主要是一个简单的系统的RecyclerView的实现效果，我个人在里面定义20条循环的同样的数据。

4。文件名称：DividerItemDecoration是一个添加分割线的工具类。

/**********************************************************
 * @文件名称：DividerItemDecoration
 * @文件作者：孙顺涛
 * @Email：staryumou@163.com邮箱已改qyddai@gmail.com
 * @创建时间：2016/9/8
 * @文件描述：Recyclerview的分割线设置，系统默认的效果。 <!-- Application theme. -->
 * 采用下面这种方式可以制定分割线。
 * <style name="AppTheme" parent="AppBaseTheme">
 * <item name="android:listDivider">@drawable/divider_bg</item>
 * </style>
 * <shape xmlns:android="http://schemas.android.com/apk/res/android"
 * android:shape="rectangle" >
 * <p>
 * <gradient
 * android:centerColor="#ff00ff00"
 * android:endColor="#ff0000ff"
 * android:startColor="#ffff0000"
 * android:type="linear" />
 * <size android:height="4dp"/>
 * <p>
 * </shape>
 * @修改历史：2016/9/8
 **********************************************************/
 
 5。DRecycleViewDivider也是一个添加分割线的工具类。是在网上找的，但是（我在使用的时候发现有点问题）
 
 /**********************************************************
 * @文件名称：DRecycleViewDivider
 * @文件作者：孙顺涛
 * @Email：staryumou@163.com邮箱已改qyddai@gmail.com
 * @创建时间：2016/9/8
 * @文件描述：自定义recycleView的万能分割线。
 * @修改历史：2016/9/8
 **********************************************************/
 
 

## 重要知识点（总结，思考）。

1）在使用RecyclerView时，你会发现RecyclerView并没有支持divider这样的属性。那么怎么办，你可以给Item的布局去设置margin，当然了这种方式不够优雅，我们文章开始说了，我们可以自由的去定制它，当然我们的分割线也是可以定制的。
在上面分割线的代码注释里面已经写出来了，添加这样的代码即可。
mRecyclerView.addItemDecoration() 
该方法的参数为RecyclerView.ItemDecoration，该类为抽象类，google官方目前并没有提供默认的实现类（我觉得最好能提供几个）。 
该类的源码：

public static abstract class ItemDecoration {

public void onDraw(Canvas c, RecyclerView parent, State state) {
            onDraw(c, parent);
 }


public void onDrawOver(Canvas c, RecyclerView parent, State state) {
            onDrawOver(c, parent);
 }

public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            getItemOffsets(outRect, ((LayoutParams) view.getLayoutParams()).getViewLayoutPosition(),
                    parent);
}

@Deprecated
public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            outRect.set(0, 0, 0, 0);
 }
 
 当我们调用mRecyclerView.addItemDecoration()方法添加decoration的时候，RecyclerView在绘制的时候，去会绘制decorator，即调用该类的onDraw和onDrawOver方法，

onDraw方法先于drawChildren
onDrawOver在drawChildren之后，一般我们选择复写其中一个即可。
getItemOffsets 可以通过outRect.set()为每个Item设置一定的偏移量，主要用于绘制Decorator。

接下来我们看一个RecyclerView.ItemDecoration的实现类，该类很好的实现了RecyclerView添加分割线（当使用LayoutManager为LinearLayoutManager时）。 

也就是上面第四点的那个类。该类参考至：鸿洋-- http://blog.csdn.net/lmj623565791/article/details/38173061

    <!-- Application theme. -->
    <style name="AppTheme" parent="AppBaseTheme">
      <item name="android:listDivider">@drawable/divider_bg</item>  
    </style>

	<?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android"
    android:shape="rectangle" >

    <gradient
        android:centerColor="#ff00ff00"
        android:endColor="#ff0000ff"
        android:startColor="#ffff0000"
        android:type="linear" />
    <size android:height="4dp"/>

    </shape>

## 内容参考。

这里RecyclerView的adaper封装是借用鸿翔的 ： https://github.com/hongyangAndroid/baseAdapter 这个封装的很好的

他又是引用的这个开源项目的FastDev4Android :  https://github.com/jiangqqlmj/FastDev4Android

这里刷新的框架用的是 https://github.com/OrangeGangsters/SwipyRefreshLayout

个人觉得无论做什么思想很重要，没有思路你怎么都不会造好轮子。


## 联系作者。

Athor：sunshuntao（qydq）。

Email：qyddai@gmail.com。

知乎地址：https://zhuanlan.zhihu.com/qyddai


Github上面都是开源项目，欢迎大家下载我的项目或者有问题的同学可以发送邮件给我，如果收到邮件我会第一次时间回复处理。


