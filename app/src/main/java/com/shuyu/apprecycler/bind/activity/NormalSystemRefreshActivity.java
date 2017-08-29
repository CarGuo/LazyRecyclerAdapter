package com.shuyu.apprecycler.bind.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;
import com.shuyu.apprecycler.bind.holder.ClickHolder;
import com.shuyu.apprecycler.bind.holder.ImageHolder;
import com.shuyu.apprecycler.bind.holder.LoadMoreHolder;
import com.shuyu.apprecycler.bind.holder.MutliHolder;
import com.shuyu.apprecycler.bind.holder.TextHolder;
import com.shuyu.apprecycler.bind.model.ClickModel;
import com.shuyu.apprecycler.bind.model.ImageModel;
import com.shuyu.apprecycler.bind.model.MutliModel;
import com.shuyu.apprecycler.bind.model.TextModel;
import com.shuyu.apprecycler.bind.utils.DataUtils;
import com.shuyu.bind.listener.LoadMoreScrollListener;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.NormalAdapterManager;
import com.shuyu.bind.NormalBindDataChooseListener;
import com.shuyu.bind.NormalCommonRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoshuyu on 2017/1/7.
 * <p>
 * 使用NormalCommonRecyclerAdapter实现多样式的recycler
 * 系统的下拉刷新
 * NormalCommonRecyclerAdapter的上拉加载更多
 */

public class NormalSystemRefreshActivity extends AppCompatActivity {


    private RecyclerView recycler;

    private SwipeRefreshLayout refresh;

    private List datas = new ArrayList<>();

    private NormalCommonRecyclerAdapter adapter;

    private final Object lock = new Object();

    private boolean isfresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_refresh_recycler_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh);

        init();
        refresh();
    }

    public void init() {

        NormalAdapterManager normalAdapterManager = new NormalAdapterManager();

        //注意，一个manager中，一个id只能绑定一个holder
        //一个model class可以绑定多对id + Holder
        normalAdapterManager.bind(TextModel.class, TextHolder.ID, TextHolder.class)
                .bind(ImageModel.class, ImageHolder.ID, ImageHolder.class)
                .bind(MutliModel.class, ImageHolder.ID, ImageHolder.class)
                .bind(MutliModel.class, MutliHolder.ID, MutliHolder.class)
                .bind(ClickModel.class, ClickHolder.ID, ClickHolder.class)
                .bindLoadMore(LoadMoreHolder.LoadMoreModel.class, LoadMoreHolder.ID, LoadMoreHolder.class)
                .bingChooseListener(new NormalBindDataChooseListener() {
                    @Override
                    public int getCurrentDataLayoutId(Object object, Class classType, int position, List<Integer> ids) {
                        if (object instanceof MutliModel && ids.size() > 1) {
                            MutliModel mutliModel = (MutliModel) object;
                            if (mutliModel.getType() > 1) {
                                return MutliHolder.ID;
                            } else {
                                return ImageHolder.ID;
                            }
                        }
                        return ids.get(ids.size() - 1);
                    }
                });


        adapter = new NormalCommonRecyclerAdapter(this, normalAdapterManager, datas);

        //设置动画支持打开
        adapter.setNeedAnimation(true);

        //配置你自定义的空页面效果，不配置显示默认
        /*adapter.setNoDataLayoutId(EmptyHolder.ID);
        RecyclerBaseModel recyclerBaseModel = new RecyclerBaseModel();
        recyclerBaseModel.setResLayoutId(EmptyHolder.ID);
        adapter.setNoDataModel(recyclerBaseModel);*/

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

    @SuppressWarnings("unchecked")
    private void refresh() {
        List list = DataUtils.getRefreshData();

        MutliModel mutliModel = new MutliModel();

        mutliModel.setResId(R.drawable.a1);
        mutliModel.setRes2(R.drawable.a2);
        mutliModel.setType(1);
        list.add(0, mutliModel);

        mutliModel = new MutliModel();

        mutliModel.setResId(R.drawable.a1);
        mutliModel.setRes2(R.drawable.a2);
        mutliModel.setType(2);
        list.add(1, mutliModel);

        mutliModel = new MutliModel();

        mutliModel.setResId(R.drawable.a1);
        mutliModel.setRes2(R.drawable.a2);
        mutliModel.setType(1);
        list.add(4, mutliModel);


        mutliModel = new MutliModel();
        mutliModel.setResId(R.drawable.a1);
        mutliModel.setRes2(R.drawable.a2);
        mutliModel.setType(2);
        list.add(7, mutliModel);

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
