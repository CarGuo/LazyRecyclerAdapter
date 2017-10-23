package com.shuyu.commonrecycler

import android.support.v7.widget.RecyclerView
import android.view.View


import com.shuyu.commonrecycler.listener.OnLoadingListener
import com.shuyu.commonrecycler.xrecycler.ArrowRefreshHeader
import com.shuyu.commonrecycler.xrecycler.LoadingMoreFooter
import com.shuyu.commonrecycler.xrecycler.base.BaseLoadMoreFooter
import com.shuyu.commonrecycler.xrecycler.base.BaseRefreshHeader
import com.shuyu.commonrecycler.xrecycler.other.ProgressStyle

import java.util.ArrayList

/**
 * Created by guoshuyu on 2017/8/30.
 */

open class BindSuperAdapterManager : BindBaseAdapterManager<BindSuperAdapterManager>() {

    val sHeaderTypes : ArrayList<Int>
        get() = sHeaderType

    companion object {

        val HEADER_INIT_INDEX = 10002

        //每个header必须有不同的type,不然滚动的时候顺序会变化
        var sHeaderType = ArrayList<Int>()
    }

    //下拉刷新的loading样式,默认系统
    internal var mRefreshProgressStyle = ProgressStyle.SysProgress

    //上拉加载更多的loading样式，默认系统
    internal var mLoadingMoreProgressStyle = ProgressStyle.SysProgress

    //是否使能下拉刷新
    internal var pullRefreshEnabled = true

    //是否使能加载更多
    internal var loadingMoreEnabled = true

    //沒有数据时是否使能加载更多
    internal var loadingMoreEmptyEnabled = false

    //是否正在加载数据
    internal var isLoadingData = false

    //是否没有更多数据了
    internal var isNoMore = false

    //滚动监听
    internal lateinit var onScrollListener: RecyclerView.OnScrollListener

    //loading监听
    internal var mLoadingListener: OnLoadingListener? = null

    //headerView列表
    internal var mHeaderViews = ArrayList<View>()

    //底部footer
    internal var mFootView: BaseLoadMoreFooter? = null

    //头部刷新类型
    internal var mRefreshHeader: BaseRefreshHeader? = null

    internal lateinit var mTouchListener: View.OnTouchListener

    val headersCount: Int
        get() = mHeaderViews.size

    fun isPullRefreshEnabled(): Boolean {
        return pullRefreshEnabled
    }

    fun isLoadingMoreEnabled(): Boolean {
        return loadingMoreEnabled
    }

    /**
     * 上拉加载更多完成
     */
    fun loadMoreComplete() {
        isLoadingData = false
        if (mFootView != null) {
            mFootView!!.setState(BaseLoadMoreFooter.STATE_COMPLETE)
        }
    }

    /**
     * 是否没有更多数据
     *
     * @param noMore 上拉是否已经没有更多数据了
     */
    fun setNoMore(noMore: Boolean) {
        isLoadingData = false
        isNoMore = noMore
        if (mFootView != null) {
            mFootView!!.setState(if (isNoMore) BaseLoadMoreFooter.STATE_NOMORE else BaseLoadMoreFooter.STATE_COMPLETE)
        }
    }

    /**
     * 重置上下拉
     */
    fun reset() {
        setNoMore(false)
        loadMoreComplete()
        refreshComplete()
    }

    /**
     * 是否正在刷新
     */
    fun setRefreshing(refreshing: Boolean) {
        if (refreshing && pullRefreshEnabled && mLoadingListener != null) {
            if (mRefreshHeader != null) {
                mRefreshHeader!!.state = BaseRefreshHeader.STATE_REFRESHING
                mRefreshHeader!!.onMove(mRefreshHeader!!.measuredHeight.toFloat())
            }
            mLoadingListener!!.onRefresh()
        }
    }

    /**
     * 刷新结束
     */
    fun refreshComplete() {
        if (mRefreshHeader != null) {
            mRefreshHeader!!.refreshComplete()
        }
        setNoMore(false)
    }

    /**
     * 添加头部list
     */
    fun addHeaderView(view: View): BindSuperAdapterManager {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size)
        mHeaderViews.add(view)
        return this
    }

    /**
     * 设置刷新的view
     */
    fun setRefreshHeader(refreshHeader: BaseRefreshHeader): BindSuperAdapterManager {
        mRefreshHeader = refreshHeader
        return this
    }

    /**
     * 设置上拉加载更多view
     */
    fun setFootView(footView: BaseLoadMoreFooter): BindSuperAdapterManager {
        this.mFootView = footView
        return this
    }

    /**
     * 是否使能下拉刷新
     */
    fun setPullRefreshEnabled(enabled: Boolean): BindSuperAdapterManager {
        pullRefreshEnabled = enabled
        return this
    }

    /**
     * 是否使能上拉加载更多
     */
    fun setLoadingMoreEnabled(enabled: Boolean): BindSuperAdapterManager {
        loadingMoreEnabled = enabled
        if (!enabled && mFootView != null) {
            mFootView!!.setState(BaseLoadMoreFooter.STATE_COMPLETE)
        }
        return this
    }

    fun setLoadingMoreEmptyEnabled(enabled: Boolean): BindSuperAdapterManager {
        loadingMoreEmptyEnabled = enabled
        return this
    }

    /**
     * 刷新loading的样式
     */
    fun setRefreshProgressStyle(style: Int): BindSuperAdapterManager {
        mRefreshProgressStyle = style
        if (mRefreshHeader != null) {
            mRefreshHeader!!.setProgressStyle(style)
        }
        return this
    }

    /**
     * 加载更多的loading样式
     */
    fun setLoadingMoreProgressStyle(style: Int): BindSuperAdapterManager {
        mLoadingMoreProgressStyle = style
        if (mFootView != null) {
            mFootView!!.setProgressStyle(style)
        }
        return this
    }

    /**
     * 下拉刷新loading的图标
     */
    fun setArrowImageView(resId: Int): BindSuperAdapterManager {
        if (mRefreshHeader != null) {
            mRefreshHeader!!.setArrowImageView(resId)
        }
        return this
    }

    /**
     * 刷新与加载更多的监听回调
     */
    fun setLoadingListener(listener: OnLoadingListener): BindSuperAdapterManager {
        mLoadingListener = listener
        return this
    }

    /**
     * 滚动监听
     */
    fun setAddOnScrollListener(onScrollListener: RecyclerView.OnScrollListener): BindSuperAdapterManager {
        this.onScrollListener = onScrollListener
        return this
    }


    fun setTouchListener(touchListener: View.OnTouchListener): BindSuperAdapterManager {
        this.mTouchListener = touchListener
        return this
    }


}
