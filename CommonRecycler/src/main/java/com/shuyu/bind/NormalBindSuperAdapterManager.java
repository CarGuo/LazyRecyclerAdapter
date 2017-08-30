package com.shuyu.bind;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.base.BaseLoadMoreFooter;
import com.jcodecraeer.xrecyclerview.base.BaseRefreshHeader;
import com.jcodecraeer.xrecyclerview.other.ProgressStyle;
import com.shuyu.bind.listener.LoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoshuyu on 2017/8/30.
 */

public class NormalBindSuperAdapterManager extends BaseBindAdapterManager<NormalBindSuperAdapterManager> {

    public static final int HEADER_INIT_INDEX = 10002;

    //下拉刷新的loading样式,默认系统
    int mRefreshProgressStyle = ProgressStyle.SysProgress;

    //上拉加载更多的loading样式，默认系统
    int mLoadingMoreProgressStyle = ProgressStyle.SysProgress;

    //是否使能下拉刷新
    boolean pullRefreshEnabled = true;

    //是否使能加载更多
    boolean loadingMoreEnabled = true;

    //是否正在加载数据
    boolean isLoadingData = false;

    //是否没有更多数据了
    boolean isNoMore = false;

    //每个header必须有不同的type,不然滚动的时候顺序会变化
    static List<Integer> sHeaderTypes = new ArrayList<>();

    //adapter没有数据的时候显示,类似于listView的emptyView
    View mEmptyView;

    //滚动监听
    RecyclerView.OnScrollListener onScrollListener;

    //loading监听
    LoadingListener mLoadingListener;

    //headerView列表
    ArrayList<View> mHeaderViews = new ArrayList<>();

    //底部footer
    BaseLoadMoreFooter mFootView;

    //头部刷新类型
    BaseRefreshHeader mRefreshHeader;

    public int getHeadersCount() {
        return mHeaderViews.size();
    }

    public boolean isPullRefreshEnabled() {
        return pullRefreshEnabled;
    }

    public boolean isLoadingMoreEnabled() {
        return loadingMoreEnabled;
    }

    /**
     * 上拉加载更多完成
     */
    public void loadMoreComplete() {
        isLoadingData = false;
        if (mFootView != null) {
            mFootView.setState(LoadingMoreFooter.STATE_COMPLETE);
        }
    }

    /**
     * 是否没有更多数据
     *
     * @param noMore 上拉是否已经没有更多数据了
     */
    public void setNoMore(boolean noMore) {
        isLoadingData = false;
        isNoMore = noMore;
        if (mFootView != null) {
            mFootView.setState(isNoMore ? LoadingMoreFooter.STATE_NOMORE : LoadingMoreFooter.STATE_COMPLETE);
        }
    }

    /**
     * 重置上下拉
     */
    public void reset() {
        setNoMore(false);
        loadMoreComplete();
        refreshComplete();
    }

    /**
     * 是否正在刷新
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && pullRefreshEnabled && mLoadingListener != null) {
            if (mRefreshHeader != null) {
                mRefreshHeader.setState(ArrowRefreshHeader.STATE_REFRESHING);
                mRefreshHeader.onMove(mRefreshHeader.getMeasuredHeight());
            }
            mLoadingListener.onRefresh();
        }
    }

    /**
     * 刷新结束
     */
    public void refreshComplete() {
        if (mRefreshHeader != null) {
            mRefreshHeader.refreshComplete();
        }
        setNoMore(false);
    }

    /**
     * 没有数据的时候空view
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 添加头部list
     */
    public NormalBindSuperAdapterManager addHeaderView(View view) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size());
        mHeaderViews.add(view);
        return this;
    }

    /**
     * 设置刷新的view
     */
    public NormalBindSuperAdapterManager setRefreshHeader(BaseRefreshHeader refreshHeader) {
        mRefreshHeader = refreshHeader;
        return this;
    }

    /**
     * 设置上拉加载更多view
     */
    public NormalBindSuperAdapterManager setFootView(BaseLoadMoreFooter footView) {
        this.mFootView = footView;
        return this;
    }

    /**
     * 是否使能下拉刷新
     */
    public NormalBindSuperAdapterManager setPullRefreshEnabled(boolean enabled) {
        pullRefreshEnabled = enabled;
        return this;
    }

    /**
     * 是否使能上拉加载更多
     */
    public NormalBindSuperAdapterManager setLoadingMoreEnabled(boolean enabled) {
        loadingMoreEnabled = enabled;
        if (!enabled && mFootView != null) {
            mFootView.setState(LoadingMoreFooter.STATE_COMPLETE);
        }
        return this;
    }

    /**
     * 刷新loading的样式
     */
    public NormalBindSuperAdapterManager setRefreshProgressStyle(int style) {
        mRefreshProgressStyle = style;
        if (mRefreshHeader != null) {
            mRefreshHeader.setProgressStyle(style);
        }
        return this;
    }

    /**
     * 加载更多的loading样式
     */
    public NormalBindSuperAdapterManager setLoadingMoreProgressStyle(int style) {
        mLoadingMoreProgressStyle = style;
        if (mFootView != null) {
            mFootView.setProgressStyle(style);
        }
        return this;
    }

    /**
     * 下拉刷新loading的图标
     */
    public NormalBindSuperAdapterManager setArrowImageView(int resId) {
        if (mRefreshHeader != null) {
            mRefreshHeader.setArrowImageView(resId);
        }
        return this;
    }

    public View getEmptyView() {
        return mEmptyView;
    }

    /**
     * 刷新与加载更多的监听回调
     */
    public NormalBindSuperAdapterManager setLoadingListener(LoadingListener listener) {
        mLoadingListener = listener;
        return this;
    }

    /**
     * 滚动监听
     */
    public NormalBindSuperAdapterManager setAddOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
        return this;
    }



}
