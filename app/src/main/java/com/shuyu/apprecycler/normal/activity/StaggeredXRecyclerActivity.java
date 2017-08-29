package com.shuyu.apprecycler.normal.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.jcodecraeer.xrecyclerview.other.ProgressStyle;
import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.itemDecoration.DividerItemDecoration;
import com.shuyu.apprecycler.normal.holder.ClickHolder;
import com.shuyu.apprecycler.normal.holder.ImageHolder;
import com.shuyu.apprecycler.normal.holder.MutliHolder;
import com.shuyu.apprecycler.normal.holder.TextHolder;
import com.shuyu.apprecycler.normal.model.ClickModel;
import com.shuyu.apprecycler.normal.model.ImageModel;
import com.shuyu.apprecycler.normal.model.MutliModel;
import com.shuyu.apprecycler.normal.model.TextModel;
import com.shuyu.apprecycler.normal.utils.DataUtils;
import com.shuyu.normal.listener.OnItemClickListener;
import com.shuyu.normal.NormalAdapterManager;
import com.shuyu.normal.NormalCommonRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 使用CommonRecyclerAdapter实现多样式的recycler
 * 使用 XRecyclerView 实现上下拉更新
 * 瀑布流
 */
public class StaggeredXRecyclerActivity extends AppCompatActivity {

    @BindView(R.id.xRecycler)
    XRecyclerView xRecycler;

    StaggeredGridLayoutManager staggeredGridLayoutManager;

    private List dataList = new ArrayList<>();

    private NormalCommonRecyclerAdapter commonRecyclerAdapter;

    private final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_xrecycler);

        ButterKnife.bind(this);

        initView();

        refresh();
    }


    private void initView() {
        //瀑布流管理器
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置瀑布流管理器
        xRecycler.setLayoutManager(staggeredGridLayoutManager);
        //添加分割线，注意位置是会从下拉那个Item的实际位置开始的，所以如果支持下拉需要屏蔽下拉和HeaderView
        xRecycler.addItemDecoration(new DividerItemDecoration(dip2px(this, 10), DividerItemDecoration.GRID, 2));

        //是否屏蔽下拉
        //xRecycler.setPullRefreshEnabled(false);
        //上拉加载更多样式，也可以设置下拉
        xRecycler.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
        //设置管理器，关联布局与holder类名，不同id可以管理一个holder
        NormalAdapterManager normalAdapterManager = new NormalAdapterManager();
        normalAdapterManager
                .bind(ImageModel.class, ImageHolder.ID, ImageHolder.class)
                .bind(TextModel.class, TextHolder.ID, TextHolder.class)
                .bind(MutliModel.class, MutliHolder.ID, MutliHolder.class)
                .bind(ClickModel.class, ClickHolder.ID, ClickHolder.class);
        //初始化通用管理器
        commonRecyclerAdapter = new NormalCommonRecyclerAdapter(this, normalAdapterManager, dataList);
        xRecycler.setAdapter(commonRecyclerAdapter);

        //添加头部
        View header = LayoutInflater.from(this).inflate(R.layout.layout_header, null);
        //添加头部
        xRecycler.addHeaderView(header);


        //本身也支持设置空局部
        //xRecycler.setEmptyView();
        xRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                xRecycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                xRecycler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMore();
                    }
                }, 1000);
            }
        });

        commonRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
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


    private void refresh() {
        List list = DataUtils.getRefreshData();
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            dataList = list;
            commonRecyclerAdapter.setListData(list);
            xRecycler.refreshComplete();
        }

    }

    private void loadMore() {
        List list = DataUtils.getLoadMoreData(dataList);
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized (lock) {
            commonRecyclerAdapter.addListData(list);
            xRecycler.loadMoreComplete();
        }
    }
}
