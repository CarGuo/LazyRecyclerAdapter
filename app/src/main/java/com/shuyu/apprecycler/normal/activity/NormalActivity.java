package com.shuyu.apprecycler.normal.activity;

import android.content.Context;
import android.os.Bundle;
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
import com.shuyu.listener.OnItemClickListener;
import com.shuyu.normal.NormalAdapterManager;
import com.shuyu.normal.NormalCommonRecyclerAdapter;

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

    private NormalCommonRecyclerAdapter adapter;

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

        NormalAdapterManager normalAdapterManager = new NormalAdapterManager();
        normalAdapterManager
                .bind(ImageModel.class, ImageHolder.ID, ImageHolder.class)
                .bind(TextModel.class, TextHolder.ID, TextHolder.class)
                .bind(MutliModel.class, MutliHolder.ID, MutliHolder.class)
                .bind(ClickModel.class, ClickHolder.ID, ClickHolder.class)
                .bindEmpty(NoDataHolder.NoDataModel.class, NoDataHolder.ID, NoDataHolder.class);



        adapter = new NormalCommonRecyclerAdapter(this, normalAdapterManager, datas);

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
        List list = DataUtils.getRefreshData();
        this.datas = list;
        if (adapter != null) {
            adapter.setListData(datas);
        }
    }

}
