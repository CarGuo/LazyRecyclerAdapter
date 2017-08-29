package com.shuyu.apprecycler.special.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.shuyu.apprecycler.special.Holder.ClickHolder;
import com.shuyu.apprecycler.special.Holder.EmptyHolder;
import com.shuyu.apprecycler.special.Holder.ImageHolder;
import com.shuyu.apprecycler.special.Holder.MutilHolder;
import com.shuyu.apprecycler.special.Holder.TextHolder;
import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;
import com.shuyu.apprecycler.special.utils.DataUtils;
import com.shuyu.common.CommonRecyclerAdapter;
import com.shuyu.common.CommonRecyclerManager;
import com.shuyu.common.listener.OnItemClickListener;
import com.shuyu.common.model.RecyclerBaseModel;

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


    private List<RecyclerBaseModel> datas = new ArrayList<>();

    private CommonRecyclerAdapter adapter;

    private boolean isLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_recycler_layout);
        ButterKnife.bind(this);
        init();
        initDatas();
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
        adapter.setNeedLoadMore(false);

        //需要显示空数据页面
        adapter.setShowNoData(true);

        //设置动画支持打开
        adapter.setNeedAnimation(true);

        //配置你自定义的空页面效果，不配置显示默认
        adapter.setNoDataLayoutId(EmptyHolder.ID);
        RecyclerBaseModel recyclerBaseModel = new RecyclerBaseModel();
        recyclerBaseModel.setResLayoutId(EmptyHolder.ID);
        adapter.setNoDataModel(recyclerBaseModel);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new DividerItemDecoration(dip2px(this, 10), DividerItemDecoration.LIST));
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Context context, int position) {
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


    public void initDatas() {
        List<RecyclerBaseModel> list = DataUtils.getRefreshData();
        this.datas = list;
        if (adapter != null) {
            adapter.setListData(datas);
        }
    }

}
