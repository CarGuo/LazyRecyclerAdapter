package com.shuyu.apprecycler.bind.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;
import com.shuyu.apprecycler.bind.holder.BindClickHolder;
import com.shuyu.apprecycler.bind.holder.BindImageHolder;
import com.shuyu.apprecycler.bind.holder.BindMutliHolder;
import com.shuyu.apprecycler.bind.holder.BindNoDataHolder;
import com.shuyu.apprecycler.bind.holder.BindTextHolder;
import com.shuyu.apprecycler.bind.model.BindClickModel;
import com.shuyu.apprecycler.bind.model.BindImageModel;
import com.shuyu.apprecycler.bind.model.BindMutliModel;
import com.shuyu.apprecycler.bind.model.BindTextModel;
import com.shuyu.apprecycler.bind.utils.BindDataUtils;
import com.shuyu.apprecycler.special.model.ImageModel;
import com.shuyu.apprecycler.special.view.CustomLoadMoreFooter;
import com.shuyu.apprecycler.special.view.CustomRefreshHeader;
import com.shuyu.bind.NormalBindRecyclerAdapter;
import com.shuyu.bind.NormalBindSuperAdapter;
import com.shuyu.bind.NormalBindSuperAdapterManager;
import com.shuyu.bind.listener.LoadingListener;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.NormalBindAdapterManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 使用CommonRecyclerAdapter实现多样式的recycler
 * 没有下拉刷新和上拉刷新
 */
public class NormalActivity extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;


    private List datas = new ArrayList<>();

    private NormalBindSuperAdapter adapter;

    private boolean isLoadMore;

    private final Object lock = new Object();

    private NormalBindSuperAdapterManager normalAdapterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_recycler_layout);
        ButterKnife.bind(this);
        init();
        initDatas();
    }

    public void init() {
        View header = LayoutInflater.from(this).inflate(R.layout.layout_header, null);
        normalAdapterManager = new NormalBindSuperAdapterManager();
        normalAdapterManager
                .bind(BindImageModel.class, BindImageHolder.ID, BindImageHolder.class)
                .bind(BindTextModel.class, BindTextHolder.ID, BindTextHolder.class)
                .bind(BindMutliModel.class, BindMutliHolder.ID, BindMutliHolder.class)
                .bind(BindClickModel.class, BindClickHolder.ID, BindClickHolder.class)
                .bindEmpty(BindNoDataHolder.NoDataModel.class, BindNoDataHolder.ID, BindNoDataHolder.class)
                .setNeedAnimation(true)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Context context, int position) {
                        Toast.makeText(context, "点击了！！　" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .setPullRefreshEnabled(true)
                .setLoadingMoreEnabled(true)
                .setFootView(new CustomLoadMoreFooter(this))
                .setRefreshHeader(new CustomRefreshHeader(this))
                .addHeaderView(header)
                .setLoadingListener(new LoadingListener() {
                    @Override
                    public void onRefresh() {
                        recycler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                refresh();
                            }
                        }, 1000);
                    }

                    @Override
                    public void onLoadMore() {
                        recycler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadMore();
                            }
                        }, 1000);
                    }
                });


        adapter = new NormalBindSuperAdapter(this, normalAdapterManager, datas);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(dip2px(this, 10), DividerItemDecoration.LIST, adapter));
        recycler.setAdapter(adapter);

    }

    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }


    public void initDatas() {
        List list = BindDataUtils.getRefreshData();
        this.datas = list;
        if (adapter != null) {
            adapter.setListData(datas);
        }
    }


    private void refresh() {
        List list = BindDataUtils.getRefreshData();
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            datas = list;
            adapter.setListData(list);
            normalAdapterManager.refreshComplete();
        }

    }

    private void loadMore() {
        List list = BindDataUtils.getLoadMoreData(datas);
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            adapter.addListData(list);
            normalAdapterManager.loadMoreComplete();
        }
    }

}
