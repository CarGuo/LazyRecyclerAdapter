package com.shuyu.apprecycler;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.shuyu.CommonRecyclerAdapter.CommonRecyclerAdapter;
import com.shuyu.CommonRecyclerAdapter.CommonRecyclerManager;
import com.shuyu.CommonRecyclerAdapter.model.RecyclerBaseModel;
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
 * Created by shuyu on 2016/11/23.
 */

public class List2Fragment extends Fragment {


    @BindView(R.id.xRecycler)
    XRecyclerView xRecycler;

    StaggeredGridLayoutManager staggeredGridLayoutManager;

    private List<RecyclerBaseModel> dataList = new ArrayList<>();

    private CommonRecyclerAdapter commonRecyclerAdapter;

    public List2Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_list_2, container, false);

        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        xRecycler.setLayoutManager(staggeredGridLayoutManager);
        xRecycler.addItemDecoration(new DividerItemDecoration(dip2px(getActivity(),  10), DividerItemDecoration.GRID));
        xRecycler.setPullRefreshEnabled(false);
        xRecycler.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);

        CommonRecyclerManager commonRecyclerManager = new CommonRecyclerManager();
        commonRecyclerManager.addType(ImageHolder.ID, ImageHolder.class.getName());
        commonRecyclerManager.addType(TextHolder.ID, TextHolder.class.getName());
        commonRecyclerManager.addType(ClickHolder.ID, ClickHolder.class.getName());

        commonRecyclerAdapter = new CommonRecyclerAdapter(getActivity(), commonRecyclerManager, dataList);
        xRecycler.setAdapter(commonRecyclerAdapter);


        xRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                xRecycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();
                    }
                }, 2000);
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

    public void setDatas(List datas) {
        this.dataList = datas;
        if (commonRecyclerAdapter != null) {
            commonRecyclerAdapter.setListData(datas);
        }
    }

    private void loadMore() {
        List<RecyclerBaseModel> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            RecyclerBaseModel recyclerBaseModel = dataList.get(i);
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
        commonRecyclerAdapter.addListData(list);
        xRecycler.loadMoreComplete();
    }

}
