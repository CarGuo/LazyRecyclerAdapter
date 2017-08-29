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
import com.shuyu.apprecycler.bind.holder.BindNoDataHolder;
import com.shuyu.apprecycler.bind.holder.BindTextHolder;
import com.shuyu.apprecycler.bind.model.BindClickModel;
import com.shuyu.apprecycler.bind.model.BindImageModel;
import com.shuyu.apprecycler.bind.model.BindMutliModel;
import com.shuyu.apprecycler.bind.model.BindTextModel;
import com.shuyu.apprecycler.bind.utils.BindDataUtils;
import com.shuyu.apprecycler.special.model.ImageModel;
import com.shuyu.bind.NormalBindRecyclerAdapter;
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

    private NormalBindRecyclerAdapter adapter;

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

        NormalBindAdapterManager normalAdapterManager = new NormalBindAdapterManager();
        normalAdapterManager
                .bind(BindImageModel.class, BindImageHolder.ID, BindImageHolder.class)
                .bind(BindTextModel.class, BindTextHolder.ID, BindTextHolder.class)
                .bind(BindMutliModel.class, BindMutliHolder.ID, BindMutliHolder.class)
                .bind(BindClickModel.class, BindClickHolder.ID, BindClickHolder.class)
                .bindEmpty(BindNoDataHolder.NoDataModel.class, BindNoDataHolder.ID, BindNoDataHolder.class);


        adapter = new NormalBindRecyclerAdapter(this, normalAdapterManager, datas);

        //设置动画支持打开
        adapter.setNeedAnimation(true);

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
        List list = BindDataUtils.getRefreshData();
        this.datas = list;
        if (adapter != null) {
            adapter.setListData(datas);
        }
    }

}
