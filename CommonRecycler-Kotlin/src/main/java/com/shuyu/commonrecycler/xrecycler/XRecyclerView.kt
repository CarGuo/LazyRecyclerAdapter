package com.shuyu.commonrecycler.xrecycler

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent


import com.shuyu.commonrecycler.xrecycler.base.BaseLoadMoreFooter
import com.shuyu.commonrecycler.xrecycler.base.BaseRefreshHeader
import com.shuyu.commonrecycler.xrecycler.other.AppBarStateChangeListener
import com.shuyu.commonrecycler.xrecycler.other.ProgressStyle

import java.util.ArrayList

open class XRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    //下拉刷新的loading样式,默认系统
    private var mRefreshProgressStyle = ProgressStyle.SysProgress

    //上拉加载更多的loading样式，默认系统
    private var mLoadingMoreProgressStyle = ProgressStyle.SysProgress

    //触摸下拉移动的距离
    private var mLastY = -1f

    //是否使能下拉刷新
    /**
     * 是否使能下拉刷新
     */
    var isPullRefreshEnabled = true

    //是否使能加载更多
    /**
     * 是否使能上拉加载更多
     */
    var isLoadingMoreEnabled = true
        set(enabled) {
            field = enabled
            if (!enabled) {
                mFootView!!.setState(BaseLoadMoreFooter.STATE_COMPLETE)
            }
        }

    //是否正在加载数据
    private var isLoadingData = false

    //是否没有更多数据了
    private var isNoMore = false

    //用在内外数据变化更新的同步更新
    private val mDataObserver = DataObserver()

    //adapter没有数据的时候显示,类似于listView的emptyView
    /**
     * 没有数据的时候空view
     */
    var emptyView: View? = null
        set(emptyView) {
            field = emptyView
            mDataObserver.onChanged()
        }

    //底部footer
    private var mFootView: BaseLoadMoreFooter? = null

    //头部刷新类型
    private var mRefreshHeader: BaseRefreshHeader? = null

    //兼容 appbar
    private var appbarState: AppBarStateChangeListener.State = AppBarStateChangeListener.State.EXPANDED

    //滚动监听
    private var onScrollListenerT: RecyclerView.OnScrollListener? = null

    //loading监听
    private var mLoadingListener: LoadingListener? = null

    //headerView列表
    private val mHeaderViews = ArrayList<View>()

    //内部和外部adapter的切换
    private var mWrapAdapter: WrapAdapter? = null

    /**
     * 是否顶部
     */
    private val isOnTop: Boolean
        get() = if (mRefreshHeader!!.parent != null) {
            true
        } else {
            false
        }


    val headersCount: Int
        get() = mHeaderViews.size


    interface LoadingListener {
        fun onRefresh()

        fun onLoadMore()
    }

    init {
        init()
    }

    private fun init() {
        if (isPullRefreshEnabled) {
            mRefreshHeader = ArrowRefreshHeader(context)
            mRefreshHeader!!.setProgressStyle(mRefreshProgressStyle)
        }
        val footView = LoadingMoreFooter(context)
        footView.setProgressStyle(mLoadingMoreProgressStyle)
        mFootView = footView
        mFootView!!.visibility = View.GONE
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<ViewHolder>) {
        mWrapAdapter = WrapAdapter(adapter)
        super.setAdapter(mWrapAdapter)
        adapter.registerAdapterDataObserver(mDataObserver)
        mDataObserver.onChanged()
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        if (onScrollListenerT != null) {
            onScrollListenerT!!.onScrollStateChanged(this, state)
        }
        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadingListener != null && !isLoadingData && isLoadingMoreEnabled) {
            val layoutManager = layoutManager
            val lastVisibleItemPosition: Int
            if (layoutManager is GridLayoutManager) {
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val into = IntArray(layoutManager.spanCount)
                layoutManager.findLastVisibleItemPositions(into)
                lastVisibleItemPosition = findMax(into)
            } else {
                lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
            if (layoutManager.childCount > 0
                    && lastVisibleItemPosition >= layoutManager.itemCount - 1 && layoutManager.itemCount > layoutManager.childCount && !isNoMore && mRefreshHeader!!.state < BaseRefreshHeader.STATE_REFRESHING) {
                isLoadingData = true
                mFootView!!.setState(BaseLoadMoreFooter.STATE_LOADING)
                mLoadingListener!!.onLoadMore()
            }
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (mLastY == -1f) {
            mLastY = ev.rawY
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mLastY = ev.rawY
            MotionEvent.ACTION_MOVE -> {
                val deltaY = ev.rawY - mLastY
                mLastY = ev.rawY
                if (isOnTop && isPullRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    mRefreshHeader!!.onMove(deltaY / DRAG_RATE)
                    if (mRefreshHeader!!.visibleHeight > 0 && mRefreshHeader!!.state < BaseRefreshHeader.STATE_REFRESHING) {
                        return false
                    }
                }
            }
            else -> {
                mLastY = -1f // reset
                if (isOnTop && isPullRefreshEnabled && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (mRefreshHeader!!.releaseAction()) {
                        if (mLoadingListener != null) {
                            mLoadingListener!!.onRefresh()
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(ev)
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        //解决和CollapsingToolbarLayout冲突的问题
        var appBarLayout: AppBarLayout? = null
        var p: ViewParent? = parent
        while (p != null) {
            if (p is CoordinatorLayout) {
                break
            }
            p = p.parent
        }
        if (p is CoordinatorLayout) {
            val coordinatorLayout = p as CoordinatorLayout?
            val childCount = coordinatorLayout!!.childCount
            for (i in childCount - 1 downTo 0) {
                val child = coordinatorLayout.getChildAt(i)
                if (child is AppBarLayout) {
                    appBarLayout = child
                    break
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                    override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                        appbarState = state
                    }
                })
            }
        }
    }

    /**
     * 根据header的ViewType判断是哪个header
     */
    private fun getHeaderViewByType(itemType: Int): View? {
        return if (!isHeaderType(itemType)) {
            null
        } else mHeaderViews[itemType - HEADER_INIT_INDEX]
    }

    /**
     * 判断一个type是否为HeaderType
     */
    private fun isHeaderType(itemViewType: Int): Boolean {
        return mHeaderViews.size > 0 && sHeaderTypes.contains(itemViewType)
    }

    /**
     * 判断是否是XRecyclerView保留的itemViewType
     */
    private fun isReservedItemViewType(itemViewType: Int): Boolean {
        return if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_FOOTER || sHeaderTypes.contains(itemViewType)) {
            true
        } else {
            false
        }
    }

    /**
     * 瀑布流里最后的一个item的位置
     */
    private fun findMax(lastPositions: IntArray): Int {
        var max = lastPositions[0]
        for (value in lastPositions) {
            if (value > max) {
                max = value
            }
        }
        return max
    }

    /**
     * 外部adapter更新的时候，同步更新内部adapter
     */
    private inner class DataObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            val adapter = adapter
            if (adapter != null && emptyView != null) {
                var emptyCount = 0
                if (isPullRefreshEnabled) {
                    emptyCount++
                }
                if (isLoadingMoreEnabled) {
                    emptyCount++
                }
                if (adapter.itemCount == emptyCount) {
                    emptyView!!.visibility = View.VISIBLE
                    this@XRecyclerView.visibility = View.GONE
                } else {
                    emptyView!!.visibility = View.GONE
                    this@XRecyclerView.visibility = View.VISIBLE
                }
            }
            if (mWrapAdapter != null) {
                mWrapAdapter!!.notifyDataSetChanged()
            }
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mWrapAdapter!!.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mWrapAdapter!!.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mWrapAdapter!!.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mWrapAdapter!!.notifyItemMoved(fromPosition, toPosition)
        }
    }


    /**
     * 内部adapter
     */
    inner class WrapAdapter(private val adapter: RecyclerView.Adapter<ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val headersCount: Int
            get() = mHeaderViews.size

        fun isHeader(position: Int): Boolean {
            val startHeader = if (isPullRefreshEnabled) 1 else 0
            val endHeader = if (isPullRefreshEnabled) mHeaderViews.size + 1 else mHeaderViews.size
            return position >= startHeader && position < endHeader
        }

        fun isFooter(position: Int): Boolean {
            return if (isLoadingMoreEnabled) {
                position == itemCount - 1
            } else {
                false
            }
        }

        fun isRefreshHeader(position: Int): Boolean {
            return position == 0 && isPullRefreshEnabled
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == TYPE_REFRESH_HEADER) {
                return SimpleViewHolder(mRefreshHeader!!)
            } else if (isHeaderType(viewType)) {
                return SimpleViewHolder(getHeaderViewByType(viewType)!!)
            } else if (viewType == TYPE_FOOTER) {
                return SimpleViewHolder(mFootView!!)
            }
            return adapter!!.onCreateViewHolder(parent, viewType)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return
            }
            val adjPosition = position - if (isPullRefreshEnabled) headersCount + 1 else headersCount
            val adapterCount: Int
            if (adapter != null) {
                adapterCount = adapter.itemCount
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition)
                    return
                }
            }
        }

        override fun getItemCount(): Int {
            return if (isLoadingMoreEnabled) {
                if (adapter != null) {
                    if (isPullRefreshEnabled)
                        headersCount + adapter.itemCount + 2
                    else
                        headersCount + adapter.itemCount + 1
                } else {
                    headersCount + 2
                }
            } else {
                if (adapter != null) {
                    if (isPullRefreshEnabled) {
                        headersCount + adapter.itemCount + 1
                    } else {
                        headersCount + adapter.itemCount
                    }
                } else {
                    headersCount + 1
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            var position = position
            val adjPosition = position - if (isPullRefreshEnabled) headersCount + 1 else headersCount
            if (isReservedItemViewType(adapter.getItemViewType(adjPosition))) {
                throw IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ")
            }
            if (isPullRefreshEnabled && isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER
            }
            if (isHeader(position)) {
                if (isPullRefreshEnabled) {
                    position = position - 1
                }
                return sHeaderTypes[position]
            }
            if (isFooter(position)) {
                return TYPE_FOOTER
            }

            val adapterCount: Int
            if (adapter != null) {
                adapterCount = adapter.itemCount
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition)
                }
            }
            return 0
        }

        override fun getItemId(position: Int): Long {
            if (position >= headersCount + 1) {
                val adjPosition = position - (headersCount + 1)
                if (adjPosition < adapter.itemCount) {
                    return adapter.getItemId(adjPosition)
                }
            }
            return -1
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
            super.onAttachedToRecyclerView(recyclerView)
            adapter.onAttachedToRecyclerView(recyclerView)
            val manager = recyclerView!!.layoutManager
            if (manager is GridLayoutManager) {
                manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                            manager.spanCount
                        else
                            1
                    }
                }
            }
        }

        override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
            adapter.onDetachedFromRecyclerView(recyclerView)
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
            super.onViewAttachedToWindow(holder)
            val lp = holder!!.itemView.layoutParams
            if (lp != null
                    && lp is StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.layoutPosition) || isRefreshHeader(holder.layoutPosition) || isFooter(holder.layoutPosition))) {
                lp.isFullSpan = true
            }
            adapter.onViewAttachedToWindow(holder)
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
            adapter.onViewDetachedFromWindow(holder)
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
            adapter.onViewRecycled(holder)
        }

        override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder?): Boolean {
            return adapter.onFailedToRecycleView(holder)
        }

        override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
            adapter.unregisterAdapterDataObserver(observer)
        }

        override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
            adapter.registerAdapterDataObserver(observer)
        }

        private inner class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    /**
     * 添加头部list
     */
    fun addHeaderView(view: View) {
        sHeaderTypes.add(HEADER_INIT_INDEX + mHeaderViews.size)
        mHeaderViews.add(view)
    }

    /**
     * 上拉加载更多完成
     */
    fun loadMoreComplete() {
        isLoadingData = false
        mFootView!!.setState(BaseLoadMoreFooter.STATE_COMPLETE)
    }

    /**
     * 是否没有更多数据
     *
     * @param noMore 上拉是否已经没有更多数据了
     */
    fun setNoMore(noMore: Boolean) {
        isLoadingData = false
        isNoMore = noMore
        mFootView!!.setState(if (isNoMore) BaseLoadMoreFooter.STATE_NOMORE else BaseLoadMoreFooter.STATE_COMPLETE)
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
        if (refreshing && isPullRefreshEnabled && mLoadingListener != null) {
            mRefreshHeader!!.state = BaseRefreshHeader.STATE_REFRESHING
            mRefreshHeader!!.onMove(mRefreshHeader!!.measuredHeight.toFloat())
            mLoadingListener!!.onRefresh()
        }
    }

    /**
     * 刷新结束
     */
    fun refreshComplete() {
        mRefreshHeader!!.refreshComplete()
        setNoMore(false)
    }

    /**
     * 设置刷新的view
     */
    fun setRefreshHeader(refreshHeader: BaseRefreshHeader) {
        mRefreshHeader = refreshHeader
    }

    /**
     * 设置上拉加载更多view
     */
    fun setFootView(footView: BaseLoadMoreFooter) {
        this.mFootView = footView
    }

    /**
     * 刷新loading的样式
     */
    fun setRefreshProgressStyle(style: Int) {
        mRefreshProgressStyle = style
        if (mRefreshHeader != null) {
            mRefreshHeader!!.setProgressStyle(style)
        }
    }

    /**
     * 加载更多的loading样式
     */
    fun setLoadingMoreProgressStyle(style: Int) {
        mLoadingMoreProgressStyle = style
        mFootView!!.setProgressStyle(style)

    }

    /**
     * 下拉刷新loading的图标
     */
    fun setArrowImageView(resId: Int) {
        if (mRefreshHeader != null) {
            mRefreshHeader!!.setArrowImageView(resId)
        }
    }

    /**
     * 刷新与加载更多的监听回调
     */
    fun setLoadingListener(listener: LoadingListener) {
        mLoadingListener = listener
    }

    /**
     * 滚动监听
     */
    fun setAddOnScrollListener(onScrollListener: RecyclerView.OnScrollListener) {
        this.onScrollListenerT = onScrollListener
    }

    companion object {

        //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。
        //不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
        //设置一个很大的数字,尽可能避免和用户的adapter冲突
        private val TYPE_REFRESH_HEADER = 10000

        private val TYPE_FOOTER = 10001

        private val HEADER_INIT_INDEX = 10002

        //下拉刷新的移动系数
        private val DRAG_RATE = 3f

        //
        private val sHeaderTypes = ArrayList<Int>()//每个header必须有不同的type,不然滚动的时候顺序会变化
    }
}