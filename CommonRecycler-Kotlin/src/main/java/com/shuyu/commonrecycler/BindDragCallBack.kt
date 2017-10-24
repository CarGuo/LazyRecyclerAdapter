package com.shuyu.commonrecycler

import android.graphics.Canvas
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper

import java.util.Collections

/**
 * 拖动回调，适配BindSuperAdapter
 * Created by guoshuyu on 2017/9/11.
 */

open class BindDragCallBack : ItemTouchHelper.Callback {

    private var mAdapter: BindSuperAdapter? = null

    private var mSwipeLength = -1

    //是否可以拖动
    private var mDragEnabled = true

    //是否可以swipe
    private var mSwipeEnabled = false

    //限制首位不能拖动
    private var mLimitStartPosition = false

    //限制结束不能拖动
    private var mLimitEndPosition = false

    //是否滑动
    private var isSwiped = true

    //是否拖拽
    private var isDraged = true

    //swipe滑动是否出发删除
    private var isSwipedDelete = true

    //drag门槛
    private var mMoveThreshold = 0.5f

    //swipe门槛
    private var mSwipeThreshold = 0.5f

    //滑动方向
    private var mSwipeFlags = ItemTouchHelper.START

    //拖动回调
    private var mMoveListener: DragMoveListener? = null

    //滑动回调
    private var mSwipeListener: SwipeListener? = null

    constructor(adapter: BindSuperAdapter?, limitStartPosition: Boolean, limitEndPosition: Boolean) : super() {
        init(adapter, limitStartPosition, limitEndPosition)
    }

    constructor(adapter: BindSuperAdapter?) : super() {
        init(adapter, false, false)
    }

    private fun init(adapter: BindSuperAdapter?, limitStartPosition: Boolean, limitEndPosition: Boolean) {
        mLimitEndPosition = limitEndPosition
        mLimitStartPosition = limitStartPosition
        mAdapter = adapter
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags: Int
        val swipeFlags: Int
        if (recyclerView.layoutManager is GridLayoutManager || recyclerView.layoutManager is StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            swipeFlags = 0
        } else {
            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            swipeFlags = mSwipeFlags
        }
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }


    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        val fromPosition = viewHolder.adapterPosition
        val toPosition = target.adapterPosition
        if (mLimitStartPosition && (fromPosition == 0 || toPosition == 0)) {
            return false
        }
        return viewHolder !is BindSuperAdapter.WrapAdapter.SimpleViewHolder
    }

    override fun onMoved(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, fromPos: Int, target: RecyclerView.ViewHolder, toPos: Int, x: Int, y: Int) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        val fromPosition = viewHolder.adapterPosition - mAdapter!!.absFirstPosition()
        val toPosition = target.adapterPosition - mAdapter!!.absFirstPosition()
        val dataList = mAdapter?.dataList
        if (fromPosition >= 0 && toPosition >= 0 && dataList != null
                && toPosition < dataList.size && fromPosition < dataList.size) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    if (fromPosition < dataList.size && toPosition < dataList.size) {
                        Collections.swap(dataList, i, i + 1)
                    }
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    if (fromPosition < dataList.size && toPosition < dataList.size) {
                        Collections.swap(dataList, i, i - 1)
                    }
                }
            }
            //数据和item的位置不是一致的
            mAdapter?.notifyItemMoved(fromPos, toPos)
            mMoveListener?.onMoved(fromPosition, toPosition)
            isDraged = true
        }
        isSwiped = false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is BindSuperAdapter.WrapAdapter.SimpleViewHolder) {
            return
        }

        val pos = viewHolder.adapterPosition - mAdapter!!.absFirstPosition()
        if (pos >= 0 && pos <= mAdapter!!.dataList!!.size - 1) {
            val position = viewHolder.adapterPosition
            if (isSwipedDelete) {
                mAdapter?.dataList?.removeAt(pos)
                mAdapter?.notifyItemRemoved(position)
                mAdapter?.notifyItemRangeChanged(position, mAdapter!!.itemCount - position)
            }
            if (mSwipeListener != null && mSwipeEnabled) {
                mSwipeListener?.onSwiped(pos)
            }
            isSwiped = true
        }
        isDraged = false
    }

    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     */
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            return
        }
        mMoveListener?.onMoveStart()
    }

    /**
     * /当手指松开的时候（拖拽完成的时候）调用
     */
    override fun clearView(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is BindSuperAdapter.WrapAdapter.SimpleViewHolder) {
            return
        }

        if (isSwiped) {
            isSwiped = false
            return
        }

        if (isDraged) {
            mMoveListener?.onMoveEnd()
            try {
                mAdapter?.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dXT: Float, dYT: Float, actionState: Int, isCurrentlyActive: Boolean) {
        var dX = dXT
        var dY = dYT
        val position = viewHolder.adapterPosition - mAdapter!!.absFirstPosition()
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (position >= 0 && position <= mAdapter!!.dataList!!.size - 1 && viewHolder !is BindSuperAdapter.WrapAdapter.SimpleViewHolder) {
                if (mSwipeLength != -1) {
                    dX = SwipeLimited(dX)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        } else {
            dY = DragLimited(viewHolder, dY)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun isLongPressDragEnabled(): Boolean = mDragEnabled


    override fun isItemViewSwipeEnabled(): Boolean = mSwipeEnabled

    override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder?): Float = mMoveThreshold

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder?): Float = mSwipeThreshold

    private fun SwipeLimited(dXT: Float): Float {
        var dX = dXT
        if (Math.abs(dX) > Math.abs(mSwipeLength)) {
            dX = if (dX < 0) {
                (-Math.abs(mSwipeLength)).toFloat()
            } else {
                Math.abs(mSwipeLength).toFloat()
            }
        }
        return dX
    }

    private fun DragLimited(viewHolder: RecyclerView.ViewHolder, dY: Float): Float {
        val position = viewHolder.adapterPosition - mAdapter!!.absFirstPosition()
        if (position < 0) {
            return if (dY < 0) 0f else dY
        }
        if (position > mAdapter!!.dataList!!.size - 1) {
            return if (dY < 0) 0f else dY
        }
        if (mLimitStartPosition && position == 0) {
            return if (dY < 0) 0f else dY
        }
        return if (mLimitEndPosition && position == mAdapter!!.dataList!!.size - 1) {
            if (dY > 0) 0f else dY
        } else dY
    }

    /**
     * 是否使能拖动
     */
    fun setDragMoveEnabled(dragEnabled: Boolean) {
        mDragEnabled = dragEnabled
    }

    /**
     * 拖动监听
     */
    fun setDragMoveListener(moveListener: DragMoveListener) {
        mMoveListener = moveListener
    }

    /**
     * 滑动监听
     */
    fun setSwipeListener(swipeListener: SwipeListener) {
        this.mSwipeListener = swipeListener
    }

    /**
     * 滑动方向
     */
    fun setSwipeFlags(swipeFlags: Int) {
        this.mSwipeFlags = swipeFlags
    }

    /**
     * Swipe移动门槛
     */
    fun setSwipeThreshold(swipeThreshold: Float) {
        mSwipeThreshold = swipeThreshold
    }


    /**
     * Drag移动门槛
     */
    fun setMoveThreshold(moveThreshold: Float) {
        mMoveThreshold = moveThreshold
    }


    /**
     * 滑动是否删除
     */
    fun setSwipedDelete(swipedDelete: Boolean) {
        isSwipedDelete = swipedDelete
    }

    /**
     * 使能滑动
     */
    fun setSwipeEnabled(swipeEnabled: Boolean) {
        this.mSwipeEnabled = swipeEnabled
    }

    /**
     * 滑动最大距离，默认-1
     */
    fun setSwipeLength(swipeLength: Int) {
        this.mSwipeLength = swipeLength
    }

    interface DragMoveListener {
        /**
         * Item 切换位置
         *
         * @param fromPosition 开始位置
         * @param toPosition   结束位置
         */
        fun onMoved(fromPosition: Int, toPosition: Int)

        /**
         * 开始移动
         */
        fun onMoveStart()

        /**
         * 停止移动
         */
        fun onMoveEnd()
    }

    interface SwipeListener {

        fun onSwiped(position: Int)
    }

}
