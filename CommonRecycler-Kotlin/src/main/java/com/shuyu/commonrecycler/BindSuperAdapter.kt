package com.shuyu.commonrecycler

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent


import com.shuyu.commonrecycler.xrecycler.ArrowRefreshHeader
import com.shuyu.commonrecycler.xrecycler.LoadingMoreFooter
import com.shuyu.commonrecycler.xrecycler.other.AppBarStateChangeListener

import android.view.View.GONE
import com.shuyu.commonrecycler.BindSuperAdapterManager.Companion.HEADER_INIT_INDEX
import com.shuyu.commonrecycler.xrecycler.base.BaseLoadMoreFooter
import com.shuyu.commonrecycler.xrecycler.base.BaseRefreshHeader


/**
 * Created by guoshuyu on 2017/8/30.
 */

open class BindSuperAdapter(private val context: Context, private val normalAdapterManager: BindSuperAdapterManager, dataList: ArrayList<Any>) : BindRecyclerAdapter(context, normalAdapterManager, dataList), View.OnTouchListener {

    //兼容 appbar
    private var appbarState: AppBarStateChangeListener.State = AppBarStateChangeListener.State.EXPANDED

    //用在内外数据变化更新的同步更新
    private val mDataObserver = DataObserver()

    private var mWrapAdapter: WrapAdapter? = null

    private var mRecyclerView: RecyclerView? = null

    //触摸下拉移动的距离
    private var mLastY = -1f

    private var mLastX = -1f

    /**
     * 滑动监听
     */
    private val mScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, state: Int) {
            super.onScrollStateChanged(recyclerView, state)
            if (normalAdapterManager.onScrollListener != null) {
                normalAdapterManager.onScrollListener?.onScrollStateChanged(recyclerView, state)
            }
            if (state == RecyclerView.SCROLL_STATE_IDLE && normalAdapterManager.mLoadingListener != null && !normalAdapterManager.isLoadingData && normalAdapterManager.loadingMoreEnabled) {
                val layoutManager = recyclerView?.layoutManager
                val lastVisibleItemPosition = when (layoutManager) {
                    is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                    is StaggeredGridLayoutManager -> {
                        val into = IntArray(layoutManager.spanCount)
                        layoutManager.findLastVisibleItemPositions(into)
                        findMax(into)
                    }
                    else -> (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                }
                if (layoutManager.childCount > 0
                        && lastVisibleItemPosition >= layoutManager.itemCount - 1
                        && layoutManager.itemCount > layoutManager.childCount
                        && !normalAdapterManager.isNoMore
                        && (normalAdapterManager.mRefreshHeader == null || normalAdapterManager.mRefreshHeader!!.state < BaseRefreshHeader.STATE_REFRESHING)) {
                    if (emptyForLoadMore()) {
                        return
                    }
                    normalAdapterManager.isLoadingData = true
                    normalAdapterManager.mFootView?.setState(BaseLoadMoreFooter.STATE_LOADING)
                    normalAdapterManager.mLoadingListener?.onLoadMore()
                }
            }
        }
    }

    /**
     * 是否顶部
     */
    private val isOnTop: Boolean
        get() {
            return if (normalAdapterManager.mRefreshHeader == null) {
                true
            } else {
                normalAdapterManager.mRefreshHeader?.parent != null
            }
        }

    init {
        init()
    }

    private fun init() {
        if (normalAdapterManager.pullRefreshEnabled && normalAdapterManager.mRefreshHeader == null) {
            normalAdapterManager.mRefreshHeader = ArrowRefreshHeader(context)
            normalAdapterManager.mRefreshHeader?.setProgressStyle(normalAdapterManager.mRefreshProgressStyle)
        }
        if (normalAdapterManager.mFootView == null) {
            val footView = LoadingMoreFooter(context)
            footView.setProgressStyle(normalAdapterManager.mLoadingMoreProgressStyle)
            normalAdapterManager.mFootView = footView
        }
        normalAdapterManager.mFootView?.visibility = GONE
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        val adapter = recyclerView?.adapter
        mRecyclerView = recyclerView
        if (adapter is BindSuperAdapter) {
            mWrapAdapter = WrapAdapter(adapter)
            recyclerView.adapter = mWrapAdapter
            recyclerView.setOnTouchListener(this)
            recyclerView.addOnScrollListener(mScrollListener)
            adapter.registerAdapterDataObserver(mDataObserver)
            mDataObserver.onChanged()
        }
    }

    override fun onTouch(v: View, ev: MotionEvent): Boolean {
        if (normalAdapterManager.mTouchListener != null) {
            return normalAdapterManager.mTouchListener?.onTouch(v, ev)!!
        }
        return if (getOrientation(mRecyclerView?.layoutManager) == OrientationHelper.HORIZONTAL) {
            touchX(ev)
        } else touchY(ev)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
        super.onViewAttachedToWindow(holder) //解决和CollapsingToolbarLayout冲突的问题
        var appBarLayout: AppBarLayout? = null
        var p: ViewParent? = mRecyclerView?.parent
        while (p != null) {
            if (p is CoordinatorLayout) {
                break
            }
            p = p.parent
        }
        if (p is CoordinatorLayout) {
            val coordinatorLayout = p as CoordinatorLayout?
            val childCount = coordinatorLayout?.childCount
            if (childCount != null) {
                for (i in childCount - 1 downTo 0) {
                    val child = coordinatorLayout.getChildAt(i)
                    if (child is AppBarLayout) {
                        appBarLayout = child
                        break
                    }
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


    override fun curPosition(position: Int): Int {
        val count = normalAdapterManager.headersCount
        val refresh = normalAdapterManager.isPullRefreshEnabled()
        return position - count - if (refresh) 1 else 0
    }

    fun absFirstPosition(): Int {
        val count = normalAdapterManager.headersCount
        val refresh = normalAdapterManager.isPullRefreshEnabled()
        return count + if (refresh) 1 else 0
    }

    fun absFirstPositionWithoutHeader(): Int {
        val refresh = normalAdapterManager.isPullRefreshEnabled()
        return if (refresh) 1 else 0
    }

    fun emptyForLoadMore(): Boolean =
            (dataList == null || dataList?.size == 0) && !normalAdapterManager.loadingMoreEmptyEnabled

    private fun touchX(ev: MotionEvent): Boolean {
        if (mLastX == -1f) {
            mLastX = ev.rawX
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mLastX = ev.rawX
            MotionEvent.ACTION_MOVE -> {
                val deltaX = ev.rawX - mLastX
                mLastX = ev.rawX
                if (normalAdapterManager.pullRefreshEnabled && isOnTop && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    var moveD = deltaX / DRAG_RATE
                    if (getCurReverseLayout(mRecyclerView?.layoutManager)) {
                        moveD = -moveD
                    }
                    normalAdapterManager.mRefreshHeader?.onMove(moveD)
                    if (normalAdapterManager.mRefreshHeader!!.visibleHeight > 0 && normalAdapterManager.mRefreshHeader!!.state < BaseRefreshHeader.STATE_REFRESHING) {
                        return false
                    }
                }
            }
            else -> {
                mLastX = -1f // reset
                if (normalAdapterManager.pullRefreshEnabled && isOnTop && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (normalAdapterManager.mRefreshHeader!!.releaseAction()) {
                        normalAdapterManager.mLoadingListener?.onRefresh()
                    }
                }
            }
        }
        return mRecyclerView!!.onTouchEvent(ev)
    }


    private fun touchY(ev: MotionEvent): Boolean {
        if (mLastY == -1f) {
            mLastY = ev.rawY
        }
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mLastY = ev.rawY
            MotionEvent.ACTION_MOVE -> {
                val deltaY = ev.rawY - mLastY
                mLastY = ev.rawY
                if (normalAdapterManager.pullRefreshEnabled && isOnTop && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    var moveD = deltaY / DRAG_RATE
                    if (getCurReverseLayout(mRecyclerView?.layoutManager)) {
                        moveD = -moveD
                    }
                    normalAdapterManager.mRefreshHeader?.onMove(moveD)
                    if (normalAdapterManager.mRefreshHeader!!.visibleHeight > 0 && normalAdapterManager.mRefreshHeader!!.state < BaseRefreshHeader.STATE_REFRESHING) {
                        return false
                    }
                }
            }
            else -> {
                mLastY = -1f // reset
                if (normalAdapterManager.pullRefreshEnabled && isOnTop && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (normalAdapterManager.mRefreshHeader!!.releaseAction()) {
                        normalAdapterManager.mLoadingListener?.onRefresh()
                    }
                }
            }
        }
        return mRecyclerView!!.onTouchEvent(ev)
    }

    /**
     * 根据header的ViewType判断是哪个header
     */
    private fun getHeaderViewByType(itemType: Int): View? {
        return if (!isHeaderType(itemType)) {
            null
        } else normalAdapterManager.mHeaderViews[itemType - HEADER_INIT_INDEX]
    }

    /**
     * 判断一个type是否为HeaderType
     */
    private fun isHeaderType(itemViewType: Int): Boolean =
            normalAdapterManager.mHeaderViews.size > 0 && normalAdapterManager.sHeaderTypes.contains(itemViewType)

    /**
     * 判断是否是XRecyclerView保留的itemViewType
     */
    private fun isReservedItemViewType(itemViewType: Int?): Boolean =
            itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_FOOTER || normalAdapterManager.sHeaderTypes.contains(itemViewType)

    /**
     * 瀑布流里最后的一个item的位置
     */
    private fun findMax(lastPositions: IntArray): Int {
        return lastPositions.max()
                ?: lastPositions[0]
    }


    /**
     * 外部adapter更新的时候，同步更新内部adapter
     */
    private inner class DataObserver : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            mWrapAdapter?.notifyDataSetChanged()

        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            mWrapAdapter?.notifyItemRangeInserted(positionStart, itemCount)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            mWrapAdapter?.notifyItemRangeChanged(positionStart, itemCount)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            mWrapAdapter?.notifyItemRangeRemoved(positionStart, itemCount)
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            mWrapAdapter?.notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            mWrapAdapter?.notifyItemRangeChanged(positionStart, itemCount, payload)
        }
    }


    /**
     * 内部adapter
     */
    inner class WrapAdapter internal constructor(val bindRecyclerAdapter: BindRecyclerAdapter?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val headersCount: Int
            get() = normalAdapterManager.mHeaderViews.size

        internal fun isHeader(position: Int): Boolean {
            val startHeader = if (normalAdapterManager.pullRefreshEnabled) 1 else 0
            val endHeader = if (normalAdapterManager.pullRefreshEnabled) normalAdapterManager.mHeaderViews.size + 1 else normalAdapterManager.mHeaderViews.size
            return position in startHeader..(endHeader - 1)
        }

        internal fun isFooter(position: Int): Boolean {
            return if (normalAdapterManager.loadingMoreEnabled) {
                position == itemCount - 1
            } else {
                false
            }
        }

        internal fun isRefreshHeader(position: Int): Boolean =
                position == 0 && normalAdapterManager.pullRefreshEnabled

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
            return when {
                viewType == TYPE_REFRESH_HEADER -> SimpleViewHolder(normalAdapterManager.mRefreshHeader)
                isHeaderType(viewType) -> SimpleViewHolder(getHeaderViewByType(viewType))
                viewType == TYPE_FOOTER -> SimpleViewHolder(normalAdapterManager.mFootView)
                else -> bindRecyclerAdapter?.onCreateViewHolder(parent, viewType)
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return
            }
            val adjPosition = position - if (normalAdapterManager.pullRefreshEnabled) headersCount + 1 else headersCount
            val adapterCount: Int
            if (bindRecyclerAdapter != null) {
                adapterCount = bindRecyclerAdapter.itemCount
                if (adjPosition < adapterCount) {
                    bindRecyclerAdapter.onBindViewHolder(holder, adjPosition)
                }
            }
        }

        override fun getItemCount(): Int {
            return if (normalAdapterManager.loadingMoreEnabled) {
                if (bindRecyclerAdapter != null) {
                    if (normalAdapterManager.pullRefreshEnabled)
                        headersCount + bindRecyclerAdapter.itemCount + 2
                    else
                        headersCount + bindRecyclerAdapter.itemCount + 1
                } else {
                    headersCount + 2
                }
            } else {
                if (bindRecyclerAdapter != null) {
                    if (normalAdapterManager.pullRefreshEnabled) {
                        headersCount + bindRecyclerAdapter.itemCount + 1
                    } else {
                        headersCount + bindRecyclerAdapter.itemCount
                    }
                } else {
                    headersCount + 1
                }
            }
        }

        override fun getItemViewType(positionT: Int): Int {
            var position = positionT
            val adjPosition = position - if (normalAdapterManager.pullRefreshEnabled) headersCount + 1 else headersCount
            if (isReservedItemViewType(bindRecyclerAdapter?.getItemViewType(adjPosition))) {
                throw IllegalStateException("require itemViewType in adapter should be less than 10000 ")
            }
            if (normalAdapterManager.pullRefreshEnabled && isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER
            }
            if (isHeader(position)) {
                if (normalAdapterManager.pullRefreshEnabled) {
                    position -= 1
                }
                return normalAdapterManager.sHeaderTypes[position]
            }
            if (isFooter(position)) {
                return TYPE_FOOTER
            }

            val adapterCount: Int
            if (bindRecyclerAdapter != null) {
                adapterCount = bindRecyclerAdapter.itemCount
                if (adjPosition < adapterCount) {
                    return bindRecyclerAdapter.getItemViewType(adjPosition)
                }
            }
            return 0
        }

        override fun getItemId(position: Int): Long {
            if (bindRecyclerAdapter != null && position >= headersCount + 1) {
                val adjPosition = position - (headersCount + 1)
                if (adjPosition < bindRecyclerAdapter.itemCount) {
                    return bindRecyclerAdapter.getItemId(adjPosition)
                }
            }
            return -1
        }

        override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
            super.onAttachedToRecyclerView(recyclerView)
            bindRecyclerAdapter?.onAttachedToRecyclerView(recyclerView)
            val manager = recyclerView?.layoutManager
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
            bindRecyclerAdapter?.onDetachedFromRecyclerView(recyclerView)
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder?) {
            super.onViewAttachedToWindow(holder)
            val lp = holder?.itemView?.layoutParams
            if (lp != null
                    && lp is StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.layoutPosition) || isRefreshHeader(holder.layoutPosition) || isFooter(holder.layoutPosition))) {
                lp.isFullSpan = true
            }
            bindRecyclerAdapter?.onViewAttachedToWindow(holder)
        }

        override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder?) {
            bindRecyclerAdapter?.onViewDetachedFromWindow(holder)
        }

        override fun onViewRecycled(holder: RecyclerView.ViewHolder?) {
            bindRecyclerAdapter?.onViewRecycled(holder)
        }

        override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder?): Boolean =
                bindRecyclerAdapter!!.onFailedToRecycleView(holder)

        override fun unregisterAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
            bindRecyclerAdapter?.unregisterAdapterDataObserver(observer)
        }

        override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
            bindRecyclerAdapter?.registerAdapterDataObserver(observer)
        }

        inner class SimpleViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
    }

    private fun getOrientation(layoutManager: RecyclerView.LayoutManager?): Int =
            when (layoutManager) {
                is GridLayoutManager -> layoutManager.orientation
                is StaggeredGridLayoutManager -> layoutManager.orientation
                is LinearLayoutManager -> layoutManager.orientation
                else -> 0
            }

    private fun getCurReverseLayout(layoutManager: RecyclerView.LayoutManager?): Boolean =
            when (layoutManager) {
                is GridLayoutManager -> layoutManager.reverseLayout
                is StaggeredGridLayoutManager -> layoutManager.reverseLayout
                is LinearLayoutManager -> layoutManager.reverseLayout
                else -> false
            }

    companion object {

        //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。
        //不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
        //设置一个很大的数字,尽可能避免和用户的adapter冲突
        private val TYPE_REFRESH_HEADER = 10000

        private val TYPE_FOOTER = 10001

        //下拉刷新的移动系数
        private val DRAG_RATE = 3f
    }
}
