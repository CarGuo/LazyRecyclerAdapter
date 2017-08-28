package com.shuyu.apprecycler.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.shuyu.apprecycler.Holder.ClickHolder;
import com.shuyu.apprecycler.Holder.EmptyHolder;
import com.shuyu.apprecycler.Holder.ImageHolder;
import com.shuyu.apprecycler.Holder.MutilHolder;
import com.shuyu.apprecycler.Holder.TextHolder;
import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;
import com.shuyu.apprecycler.utils.DataUtils;
import com.shuyu.common.CommonRecyclerAdapter;
import com.shuyu.common.CommonRecyclerManager;
import com.shuyu.common.listener.LoadMoreScrollListener;
import com.shuyu.common.listener.OnItemClickListener;
import com.shuyu.common.model.RecyclerBaseModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshuyu on 2017/1/7.
 *
 * 使用CommonRecyclerAdapter实现多样式的recycler
 * 系统的下拉刷新
 * CommonRecyclerAdapter的上拉加载更多
 * 瀑布流
 */
public class GridSystemRefreshActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    private List<RecyclerBaseModel> datas = new ArrayList<>();

    private CommonRecyclerAdapter adapter;

    private final Object lock = new Object();

    private boolean isfresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_system_refresh);
        ButterKnife.bind(this);
        init();
        refresh();
    }


    public void init() {

        CommonRecyclerManager commonRecyclerManager = new CommonRecyclerManager();
        commonRecyclerManager.addType(ImageHolder.ID, ImageHolder.class.getName());
        commonRecyclerManager.addType(TextHolder.ID, TextHolder.class.getName());
        commonRecyclerManager.addType(ClickHolder.ID, ClickHolder.class.getName());
        commonRecyclerManager.addType(MutilHolder.ID, MutilHolder.class.getName());
        //设置空页面的
        commonRecyclerManager.addType(EmptyHolder.ID, EmptyHolder.class.getName());

        adapter = new CommonRecyclerAdapter(this, commonRecyclerManager, datas);

        //需要加载更多
        adapter.setNeedLoadMore(true);

        //需要显示空数据页面
        adapter.setShowNoData(true);

        //设置动画支持打开
        adapter.setNeedAnimation(true);

        //配置你自定义的空页面效果，不配置显示默认
        adapter.setNoDataLayoutId(EmptyHolder.ID);
        RecyclerBaseModel recyclerBaseModel = new RecyclerBaseModel();
        recyclerBaseModel.setResLayoutId(EmptyHolder.ID);
        adapter.setNoDataModel(recyclerBaseModel);

        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(this, 2);

        recycler.setLayoutManager(staggeredGridLayoutManager);
        recycler.addItemDecoration(new DividerItemDecoration(dip2px(this, 10), DividerItemDecoration.GRID));
        recycler.setAdapter(adapter);

        recycler.addOnScrollListener(new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                //注意加锁
                if (!isfresh) {
                    isfresh = true;
                    recycler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMore();
                        }

                    }, 2000);
                }
            }

            @Override
            public void onScrolled(int firstPosition) {
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (!isfresh) {
                    isfresh = true;
                    recycler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refresh();
                        }

                    }, 2000);
                }
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Context context, int position) {
                //需要减去你的header和刷新的view的数量
                Toast.makeText(context, "点击了！！　" + (position - 2), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

    private void refresh() {
        List<RecyclerBaseModel> list = DataUtils.getRefreshData();
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            datas = list;
            adapter.setListData(datas);
            refresh.setRefreshing(false);
            isfresh = false;
        }

    }

    private void loadMore() {
        List<RecyclerBaseModel> list = DataUtils.getLoadMoreData(datas);
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            //adapter.setLoadMoreState(LoadMoreHolder.NULL_DATA_STATE);
            adapter.addListData(list);
            isfresh = false;
        }
    }


}
