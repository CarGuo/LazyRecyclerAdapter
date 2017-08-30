package com.shuyu.apprecycler.bind.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;
import com.shuyu.apprecycler.bind.holder.BindClickHolder;
import com.shuyu.apprecycler.bind.holder.BindImageHolder;
import com.shuyu.apprecycler.bind.holder.BindMutliHolder;
import com.shuyu.apprecycler.bind.holder.BindTextHolder;
import com.shuyu.apprecycler.bind.model.BindClickModel;
import com.shuyu.apprecycler.bind.model.BindImageModel;
import com.shuyu.apprecycler.bind.model.BindMutliModel;
import com.shuyu.apprecycler.bind.model.BindTextModel;
import com.shuyu.apprecycler.bind.utils.BindDataUtils;
import com.shuyu.bind.BindSuperAdapter;
import com.shuyu.bind.BindSuperAdapterManager;
import com.shuyu.bind.listener.OnLoadingListener;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.listener.OnBindDataChooseListener;

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

    private List datas = new ArrayList<>();

    private BindSuperAdapter adapter;

    private BindSuperAdapterManager normalAdapterManager;

    private final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_recycler_layout);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        init();
        refresh();
    }

    public void init() {

        normalAdapterManager = new BindSuperAdapterManager();

        //注意，一个manager中，一个id只能绑定一个holder
        //一个model class可以绑定多对id + Holder
        normalAdapterManager.bind(BindTextModel.class, BindTextHolder.ID, BindTextHolder.class)
                .bind(BindImageModel.class, BindImageHolder.ID, BindImageHolder.class)
                .bind(BindMutliModel.class, BindImageHolder.ID, BindImageHolder.class)
                .bind(BindMutliModel.class, BindMutliHolder.ID, BindMutliHolder.class)
                .bind(BindClickModel.class, BindClickHolder.ID, BindClickHolder.class)
                .bingChooseListener(new OnBindDataChooseListener() {
                    //一种model类型对应多个Holder时，根据model实体判断选择holder
                    @Override
                    public int getCurrentDataLayoutId(Object object, Class classType, int position, List<Integer> ids) {
                        if (object instanceof BindMutliModel && ids.size() > 1) {
                            BindMutliModel mutliModel = (BindMutliModel) object;
                            if (mutliModel.getType() > 1) {
                                return BindMutliHolder.ID;
                            } else {
                                return BindImageHolder.ID;
                            }
                        }
                        return ids.get(ids.size() - 1);
                    }
                })
                .setNeedAnimation(true)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Context context, int position) {
                        //需要减去你的header和刷新的view的数量
                        Toast.makeText(context, "点击了！！　" + (position), Toast.LENGTH_SHORT).show();
                    }
                })
                .setPullRefreshEnabled(true)
                .setLoadingMoreEnabled(true)
                .setLoadingListener(new OnLoadingListener() {
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


        adapter = new BindSuperAdapter(this, normalAdapterManager, datas);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(dip2px(this, 10), DividerItemDecoration.LIST));
        recycler.setAdapter(adapter);
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
        List list = BindDataUtils.getRefreshData();

        BindMutliModel mutliModel = new BindMutliModel();

        mutliModel.setResId(R.drawable.a1);
        mutliModel.setRes2(R.drawable.a2);
        mutliModel.setType(1);
        list.add(0, mutliModel);

        mutliModel = new BindMutliModel();

        mutliModel.setResId(R.drawable.a1);
        mutliModel.setRes2(R.drawable.a2);
        mutliModel.setType(2);
        list.add(1, mutliModel);

        mutliModel = new BindMutliModel();

        mutliModel.setResId(R.drawable.a1);
        mutliModel.setRes2(R.drawable.a2);
        mutliModel.setType(1);
        list.add(4, mutliModel);


        mutliModel = new BindMutliModel();
        mutliModel.setResId(R.drawable.a1);
        mutliModel.setRes2(R.drawable.a2);
        mutliModel.setType(2);
        list.add(7, mutliModel);

        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            datas = list;
            adapter.setListData(datas);
            normalAdapterManager.refreshComplete();
        }

    }

    private void loadMore() {
        List list = BindDataUtils.getLoadMoreData(datas);
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            //adapter.setLoadMoreState(BindLoadMoreHolder.NULL_DATA_STATE);
            adapter.addListData(list);
            normalAdapterManager.loadMoreComplete();
        }
    }
}
