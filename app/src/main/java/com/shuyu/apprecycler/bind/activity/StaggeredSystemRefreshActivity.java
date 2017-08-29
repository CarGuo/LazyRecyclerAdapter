package com.shuyu.apprecycler.bind.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;

import com.shuyu.apprecycler.bind.holder.BindClickHolder;
import com.shuyu.apprecycler.bind.holder.BindImageHolder;
import com.shuyu.apprecycler.bind.holder.BindLoadMoreHolder;
import com.shuyu.apprecycler.bind.holder.BindMutliHolder;
import com.shuyu.apprecycler.bind.holder.BindNoDataHolder;
import com.shuyu.apprecycler.bind.holder.BindTextHolder;
import com.shuyu.apprecycler.bind.model.ClickModel;
import com.shuyu.apprecycler.bind.model.ImageModel;
import com.shuyu.apprecycler.bind.model.MutliModel;
import com.shuyu.apprecycler.bind.model.TextModel;
import com.shuyu.apprecycler.bind.utils.BindDataUtils;
import com.shuyu.bind.listener.LoadMoreScrollListener;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.NormalBindAdapterManager;
import com.shuyu.bind.NormalBindRecyclerAdapter;

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
 * 瀑布流
 */
public class StaggeredSystemRefreshActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;


    private List datas = new ArrayList<>();

    private NormalBindRecyclerAdapter adapter;

    private final Object lock = new Object();

    private boolean isfresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_system_refresh);
        ButterKnife.bind(this);
        init();
        refresh();
    }


    public void init() {

        NormalBindAdapterManager normalAdapterManager = new NormalBindAdapterManager();
        normalAdapterManager
                .bind(ImageModel.class, BindImageHolder.ID, BindImageHolder.class)
                .bind(TextModel.class, BindTextHolder.ID, BindTextHolder.class)
                .bind(MutliModel.class, BindMutliHolder.ID, BindMutliHolder.class)
                .bind(ClickModel.class, BindClickHolder.ID, BindClickHolder.class)
                .bindLoadMore(BindLoadMoreHolder.LoadMoreModel.class, BindLoadMoreHolder.ID, BindLoadMoreHolder.class)
                .bindEmpty(BindNoDataHolder.NoDataModel.class, BindNoDataHolder.ID, BindNoDataHolder.class);

        adapter = new NormalBindRecyclerAdapter(this, normalAdapterManager, datas);

        //设置动画支持打开
        adapter.setNeedAnimation(true);


        //瀑布流管理器
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

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
        List list = BindDataUtils.getRefreshData();
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
        List list = BindDataUtils.getLoadMoreData(datas);
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            //adapter.setLoadMoreState(BindLoadMoreHolder.NULL_DATA_STATE);
            adapter.addListData(list);
            isfresh = false;
        }
    }


}
