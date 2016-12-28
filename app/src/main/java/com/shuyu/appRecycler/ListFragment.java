package com.shuyu.apprecycler;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuyu.apprecycler.Holder.EmptyHolder;
import com.shuyu.apprecycler.Holder.MutilHolder;
import com.shuyu.common.CommonRecyclerAdapter;
import com.shuyu.common.CommonRecyclerManager;
import com.shuyu.common.holder.LoadMoreHolder;
import com.shuyu.common.listener.LoadMoreScrollListener;
import com.shuyu.common.model.RecyclerBaseModel;
import com.shuyu.apprecycler.Holder.ClickHolder;
import com.shuyu.apprecycler.Holder.ImageHolder;
import com.shuyu.apprecycler.Holder.TextHolder;
import com.shuyu.apprecycler.Model.ClickModel;
import com.shuyu.apprecycler.Model.ImageModel;
import com.shuyu.apprecycler.Model.TextModel;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;

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
        List<RecyclerBaseModel> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            RecyclerBaseModel recyclerBaseModel = datas.get(i);
            RecyclerBaseModel newModel;
            if (recyclerBaseModel instanceof ClickModel) {
                newModel = new ClickModel();
                ClickModel clickModel = (ClickModel) newModel;
                clickModel.setResLayoutId(recyclerBaseModel.getResLayoutId());
                clickModel.setBtnText("我就老按键哈哈哈哈！！！！！ " + i);
                list.add(newModel);
            } else if (recyclerBaseModel instanceof TextModel) {
                newModel = new TextModel();
                TextModel textModel = (TextModel) newModel;
                textModel.setResLayoutId(recyclerBaseModel.getResLayoutId());
                textModel.setText("我就老文本哈哈哈哈！！！！！ " + i);
                list.add(newModel);
            } else if (recyclerBaseModel instanceof ImageModel) {
                newModel = new ImageModel();
                ImageModel imageModel = (ImageModel) newModel;
                imageModel.setResLayoutId(recyclerBaseModel.getResLayoutId());
                imageModel.setResId(((ImageModel) recyclerBaseModel).getResId());
                list.add(newModel);
            }
        }
        isLoadMore = false;
        adapter.addListData(list);
        adapter.setLoadMoreState(LoadMoreHolder.NULL_DATA_STATE);
    }

    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }

}
