package com.shuyu.apprecycler.normal.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;
import com.shuyu.apprecycler.normal.holder.ClickHolder;
import com.shuyu.apprecycler.normal.holder.ImageHolder;
import com.shuyu.apprecycler.normal.holder.LoadMoreHolder;
import com.shuyu.apprecycler.normal.holder.MutliHolder;
import com.shuyu.apprecycler.normal.holder.NoDataHolder;
import com.shuyu.apprecycler.normal.holder.TextHolder;
import com.shuyu.apprecycler.normal.model.ClickModel;
import com.shuyu.apprecycler.normal.model.ImageModel;
import com.shuyu.apprecycler.normal.model.MutliModel;
import com.shuyu.apprecycler.normal.model.TextModel;
import com.shuyu.apprecycler.normal.utils.DataUtils;
import com.shuyu.listener.LoadMoreScrollListener;
import com.shuyu.listener.OnItemClickListener;
import com.shuyu.normal.NormalAdapterManager;
import com.shuyu.normal.NormalCommonRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshuyu on 2017/1/7.
 * <p>
 * 使用CommonRecyclerAdapter实现多样式的recycler
 * 系统的下拉刷新
 * CommonRecyclerAdapter的上拉加载更多
 */

public class SystemRefreshActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    private List datas = new ArrayList<>();

    private NormalCommonRecyclerAdapter adapter;

    private final Object lock = new Object();

    private boolean isfresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_refresh_recycler_layout);
        ButterKnife.bind(this);
        init();
        refresh();
    }

    public void init() {

        NormalAdapterManager normalAdapterManager = new NormalAdapterManager();
        normalAdapterManager
                .bind(ImageModel.class, ImageHolder.ID, ImageHolder.class)
                .bind(TextModel.class, TextHolder.ID, TextHolder.class)
                .bind(MutliModel.class, MutliHolder.ID, MutliHolder.class)
                .bind(ClickModel.class, ClickHolder.ID, ClickHolder.class)
                .bindLoadMore(LoadMoreHolder.LoadMoreModel.class, LoadMoreHolder.ID, LoadMoreHolder.class)
                .bindEmpty(NoDataHolder.NoDataModel.class, NoDataHolder.ID, NoDataHolder.class);


        adapter = new NormalCommonRecyclerAdapter(this, normalAdapterManager, datas);

        //设置动画支持打开
        adapter.setNeedAnimation(true);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(dip2px(this, 10), DividerItemDecoration.LIST));
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
                Toast.makeText(context, "点击了！！　" + position, Toast.LENGTH_SHORT).show();
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
        List list = DataUtils.getRefreshData();
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
        List list = DataUtils.getLoadMoreData(datas);
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            //adapter.setLoadMoreState(LoadMoreHolder.NULL_DATA_STATE);
            adapter.addListData(list);
            isfresh = false;
        }
    }
}
