package com.shuyu.apprecycler.special.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuyu.apprecycler.special.holder.EmptyHolder;
import com.shuyu.apprecycler.special.holder.MutilHolder;
import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.special.utils.DataUtils;
import com.shuyu.common.CommonRecyclerAdapter;
import com.shuyu.common.CommonRecyclerManager;
import com.shuyu.common.listener.LoadMoreScrollListener;
import com.shuyu.common.model.RecyclerBaseModel;
import com.shuyu.apprecycler.special.holder.ClickHolder;
import com.shuyu.apprecycler.special.holder.ImageHolder;
import com.shuyu.apprecycler.special.holder.TextHolder;
import com.shuyu.apprecycler.common.itemDecoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {


    @BindView(R.id.recycler)
    RecyclerView recycler;

    private List<RecyclerBaseModel> datas = new ArrayList<>();
    private CommonRecyclerAdapter adapter;
    private boolean isLoadMore;

    public ListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list, container, false);
        ButterKnife.bind(this, view);

        CommonRecyclerManager commonRecyclerManager = new CommonRecyclerManager();
        commonRecyclerManager.addType(ImageHolder.ID, ImageHolder.class.getName());
        commonRecyclerManager.addType(TextHolder.ID, TextHolder.class.getName());
        commonRecyclerManager.addType(ClickHolder.ID, ClickHolder.class.getName());
        commonRecyclerManager.addType(MutilHolder.ID, MutilHolder.class.getName());
        //设置空页面的
        commonRecyclerManager.addType(EmptyHolder.ID, EmptyHolder.class.getName());

        adapter = new CommonRecyclerAdapter(getActivity(), commonRecyclerManager, datas);

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

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.addItemDecoration(new DividerItemDecoration(dip2px(getActivity(), 10), DividerItemDecoration.LIST));
        recycler.setAdapter(adapter);

        recycler.addOnScrollListener(new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                //注意加锁
                if (!isLoadMore) {
                    isLoadMore = true;
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

        return view;
    }


    public void setDatas(List datas) {
        this.datas = datas;
        if (adapter != null) {
            adapter.setListData(datas);
        }
    }

    private void loadMore() {
        List<RecyclerBaseModel> list = DataUtils.getLoadMoreData(datas);
        isLoadMore = false;
        adapter.addListData(list);
    }

    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

}
