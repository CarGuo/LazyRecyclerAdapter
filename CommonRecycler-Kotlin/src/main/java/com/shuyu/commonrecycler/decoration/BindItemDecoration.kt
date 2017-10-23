package com.shuyu.commonrecycler.decoration


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View

import com.shuyu.commonrecycler.BindRecyclerAdapter
import com.shuyu.commonrecycler.BindSuperAdapter

/**
 * 实现分割线
 */
open class BindItemDecoration(private val bindSuperAdapter: BindRecyclerAdapter) : RecyclerView.ItemDecoration() {

    private var paint: Paint? = Paint(Paint.ANTI_ALIAS_FLAG)

    private var space = 50

    private var spanCount: Int = 0

    private var orientation: Int = 0

    private var dataSize: Int = 0

    private var offsetPosition: Int = 0

    private var endDataPosition: Int = 0

    //GRID左右是否需要边距
    private var needGridRightLeftEdge = true

    //第一行是否需要边距
    private var needFirstTopEdge = false

    private var color = Color.BLACK

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint!!.color = color
        paint!!.style = Paint.Style.FILL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        if (parent.layoutManager != null) {
            val layoutManager = parent.layoutManager

            //获取下拉刷新和头的偏移位置
            initOffsetPosition()
            if (dataSize == 0) {
                return
            }
            if (layoutManager is GridLayoutManager || layoutManager is StaggeredGridLayoutManager) {
                orientation = getOrientation(layoutManager)
                spanCount = getSpanCount(layoutManager)
                if (orientation == OrientationHelper.HORIZONTAL) {
                    gridRectHorizontal(parent, view, outRect)
                } else {
                    gridRect(parent, view, outRect)
                }
            } else if (layoutManager is LinearLayoutManager) {
                orientation = layoutManager.orientation
                linearRect(layoutManager, parent, view, outRect)
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        if (dataSize == 0) {
            return
        }
        if (parent.layoutManager != null) {
            val layoutManager = parent.layoutManager
            //根据布局管理器绘制边框
            if (layoutManager is LinearLayoutManager && layoutManager !is GridLayoutManager) {
                //线性的
                if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                    drawLineVertical(c, parent)
                } else {
                    drawLineHorizontal(c, parent)
                }
            } else {
                if (getOrientation(layoutManager) == OrientationHelper.HORIZONTAL) {
                    drawGridHorizontal(c, parent)
                } else {
                    drawGridVertical(c, parent)
                }
            }
        }
    }

    /**
     * 初始化偏移位置，不包含刷新、头部、上拉等item
     */
    fun initOffsetPosition() {
        //获取下拉刷新和头的偏移位置
        if (bindSuperAdapter is BindSuperAdapter) {
            offsetPosition = bindSuperAdapter.absFirstPosition()
        }
        dataSize = if (bindSuperAdapter.dataList != null) bindSuperAdapter.dataList!!.size else 0
        endDataPosition = offsetPosition + dataSize
        if (endDataPosition < 0) {
            endDataPosition = 0
        }
    }

    /**
     * 配置linear模式的item rect
     */
    fun linearRect(layoutManager: RecyclerView.LayoutManager, parent: RecyclerView, view: View, outRect: Rect) {
        val linearLayoutManager = layoutManager as LinearLayoutManager
        val currentPosition = parent.getChildAdapterPosition(view)
        //去掉header，上下拉item
        if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
            if (linearLayoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                if (linearLayoutManager.reverseLayout) {
                    outRect.left = space
                } else {
                    outRect.right = space
                }
            } else {
                if (linearLayoutManager.reverseLayout) {
                    outRect.top = space
                } else {
                    outRect.bottom = space
                }
            }
        }
        //第一行顶部间隔
        if (needFirstTopEdge && currentPosition == offsetPosition && currentPosition != endDataPosition) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (linearLayoutManager.reverseLayout) {
                    outRect.right = space
                } else {
                    outRect.left = space
                }
            } else {
                if (linearLayoutManager.reverseLayout) {
                    outRect.bottom = space
                } else {
                    outRect.top = space
                }
            }
        }
    }

    /**
     * 配置grid模式的item rect
     */
    fun gridRect(parent: RecyclerView, view: View, outRect: Rect) {
        val currentPosition = parent.getChildAdapterPosition(view)
        val spanIndex = getSpanIndex(parent, view, view.layoutParams as RecyclerView.LayoutParams)
        //去掉header，上下拉item
        if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
            if (spanIndex == spanCount - 1) {
                if (getCurReverseLayout(parent.layoutManager)) {
                    outRect.top = space
                } else {
                    outRect.bottom = space
                }
                if (needGridRightLeftEdge) {
                    outRect.right = space
                }
            } else if (spanIndex == 0) {
                outRect.right = space
                if (getCurReverseLayout(parent.layoutManager)) {
                    outRect.top = space
                } else {
                    outRect.bottom = space
                }
                if (needGridRightLeftEdge) {
                    outRect.left = space
                }
            } else {
                if (getCurReverseLayout(parent.layoutManager)) {
                    outRect.top = space
                } else {
                    outRect.bottom = space
                }
                outRect.right = space
            }
        }

        //第一行顶部间隔
        if (needFirstTopEdge && currentPosition >= offsetPosition
                && currentPosition < offsetPosition + spanCount
                && currentPosition != endDataPosition) {
            if (getCurReverseLayout(parent.layoutManager)) {
                outRect.bottom = space
            } else {
                outRect.top = space
            }
        }

    }

    /**
     * 配置橫向grid模式的item rect
     */
    fun gridRectHorizontal(parent: RecyclerView, view: View, outRect: Rect) {
        val currentPosition = parent.getChildAdapterPosition(view)
        val spanIndex = getSpanIndex(parent, view, view.layoutParams as RecyclerView.LayoutParams)
        //去掉header，上下拉item
        if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
            if (spanIndex == spanCount - 1) {
                if (getCurReverseLayout(parent.layoutManager)) {
                    outRect.left = space
                } else {
                    outRect.right = space
                }
                if (needGridRightLeftEdge) {
                    outRect.bottom = space
                }
            } else if (spanIndex == 0) {
                outRect.bottom = space
                if (getCurReverseLayout(parent.layoutManager)) {
                    outRect.left = space
                } else {
                    outRect.right = space
                }
                if (needGridRightLeftEdge) {
                    outRect.top = space
                }
            } else {
                if (getCurReverseLayout(parent.layoutManager)) {
                    outRect.left = space
                } else {
                    outRect.right = space
                }
                outRect.bottom = space
            }
        }

        //第一行顶部间隔
        if (needFirstTopEdge && currentPosition >= offsetPosition
                && currentPosition < offsetPosition + spanCount
                && currentPosition != endDataPosition) {
            if (getCurReverseLayout(parent.layoutManager)) {
                outRect.right = space
            } else {
                outRect.left = space
            }
        }

    }

    fun drawLineVertical(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.measuredHeight - parent.paddingBottom
        val childSize = parent.childCount
        val linearLayoutManager = parent.layoutManager as LinearLayoutManager
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val currentPosition = parent.getChildAdapterPosition(child)
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                if (linearLayoutManager.reverseLayout) {
                    if (paint != null) {
                        val left = child.left - space
                        val right = child.left
                        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                    }
                } else {
                    if (paint != null) {
                        val left = child.right
                        val right = left + space
                        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                    }
                }
                //第一行顶部间隔
                if (needFirstTopEdge && currentPosition == offsetPosition && currentPosition != endDataPosition) {
                    if (linearLayoutManager.reverseLayout) {
                        if (paint != null) {
                            canvas.drawRect(child.right.toFloat(), top.toFloat(), (child.right + space).toFloat(), bottom.toFloat(), paint!!)
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect((child.left - space).toFloat(), top.toFloat(), child.left.toFloat(), bottom.toFloat(), paint!!)
                        }
                    }
                }
            }
        }
    }

    fun drawLineHorizontal(canvas: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.measuredWidth - parent.paddingRight
        val childSize = parent.childCount
        val linearLayoutManager = parent.layoutManager as LinearLayoutManager
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val currentPosition = parent.getChildAdapterPosition(child)
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                if (linearLayoutManager.reverseLayout) {
                    val top = child.top - space
                    val bottom = child.top
                    if (paint != null) {
                        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                    }
                } else {
                    val top = child.bottom
                    val bottom = top + space
                    if (paint != null) {
                        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                    }
                }
            }
            //第一行顶部间隔
            if (needFirstTopEdge && currentPosition == offsetPosition && currentPosition != endDataPosition) {
                if (linearLayoutManager.reverseLayout) {
                    if (paint != null) {
                        canvas.drawRect(left.toFloat(), child.bottom.toFloat(), right.toFloat(), (child.bottom + space).toFloat(), paint!!)
                    }
                } else {
                    if (paint != null) {
                        canvas.drawRect(left.toFloat(), (child.top - space).toFloat(), right.toFloat(), child.top.toFloat(), paint!!)
                    }
                }
            }
        }
    }

    fun drawGridVertical(canvas: Canvas, parent: RecyclerView) {
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val currentPosition = parent.getChildAdapterPosition(child)
            val spanIndex = getSpanIndex(parent, child, child.layoutParams as RecyclerView.LayoutParams)
            //去掉header，上下拉item
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                if (spanIndex == spanCount - 1) {
                    val top = child.top
                    val bottomTop = child.bottom
                    val bottom = bottomTop + space
                    val left = child.left
                    val right = child.right
                    val rightRight = right + space

                    if (getCurReverseLayout(parent.layoutManager)) {
                        if (paint != null) {
                            canvas.drawRect((left - space).toFloat(), (top - space).toFloat(), (right + space).toFloat(), top.toFloat(), paint!!)
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(left.toFloat(), bottomTop.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                        }
                    }

                    if (paint != null && needGridRightLeftEdge) {
                        canvas.drawRect(right.toFloat(), top.toFloat(), rightRight.toFloat(), bottom.toFloat(), paint!!)
                    }

                } else if (spanIndex == 0) {
                    val top = child.top
                    val bottomTop = child.bottom
                    val bottom = bottomTop + space
                    val left = child.left
                    val leftRight = left + space
                    val right = child.right
                    val rightRight = right + space

                    if (getCurReverseLayout(parent.layoutManager)) {
                        if (paint != null) {
                            canvas.drawRect((left - space).toFloat(), (top - space).toFloat(), (right + space).toFloat(), top.toFloat(), paint!!)
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(left.toFloat(), bottomTop.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                        }
                    }

                    if (paint != null) {
                        canvas.drawRect(right.toFloat(), top.toFloat(), rightRight.toFloat(), bottom.toFloat(), paint!!)
                    }

                    if (paint != null && needGridRightLeftEdge) {
                        canvas.drawRect((left - space).toFloat(), top.toFloat(), (leftRight - space).toFloat(), bottom.toFloat(), paint!!)
                    }
                } else {
                    val top = child.top
                    val bottomTop = child.bottom
                    val bottom = bottomTop + space
                    val left = child.left
                    val right = child.right
                    val rightRight = right + space

                    if (getCurReverseLayout(parent.layoutManager)) {
                        if (paint != null) {
                            canvas.drawRect((left - space).toFloat(), (top - space).toFloat(), (right + space).toFloat(), top.toFloat(), paint!!)
                        }
                    } else {

                        if (paint != null) {
                            canvas.drawRect(left.toFloat(), bottomTop.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                        }
                    }

                    if (paint != null) {
                        canvas.drawRect(right.toFloat(), top.toFloat(), rightRight.toFloat(), bottom.toFloat(), paint!!)
                    }
                }
                stagFixEnd(canvas, parent, child, i, currentPosition)
            }

            //第一行顶部间隔
            if (needFirstTopEdge && currentPosition >= offsetPosition
                    && currentPosition < offsetPosition + spanCount
                    && currentPosition != endDataPosition) {
                val top = child.top - space
                val topBottom = child.top
                val left = child.left - space
                val right = child.right + space
                if (getCurReverseLayout(parent.layoutManager)) {
                    if (paint != null) {
                        canvas.drawRect(left.toFloat(), child.bottom.toFloat(), right.toFloat(), (child.bottom + space).toFloat(), paint!!)
                    }
                } else {
                    if (paint != null) {
                        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), topBottom.toFloat(), paint!!)
                    }
                }
            }
        }
    }

    fun drawGridHorizontal(canvas: Canvas, parent: RecyclerView) {
        val childSize = parent.childCount
        for (i in 0 until childSize) {
            val child = parent.getChildAt(i)
            val currentPosition = parent.getChildAdapterPosition(child)
            val spanIndex = getSpanIndex(parent, child, child.layoutParams as RecyclerView.LayoutParams)

            //去掉header，上下拉item
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                if (spanIndex == 0) {
                    val top = child.top
                    val bottomTop = child.bottom
                    val bottom = bottomTop + space
                    val left = child.left
                    val right = child.right
                    val rightRight = right + space

                    if (paint != null) {
                        canvas.drawRect(left.toFloat(), bottomTop.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                    }

                    if (getCurReverseLayout(parent.layoutManager)) {
                        if (paint != null) {
                            canvas.drawRect((left - space).toFloat(), (top - space).toFloat(), left.toFloat(), bottom.toFloat(), paint!!)
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(right.toFloat(), top.toFloat(), rightRight.toFloat(), bottom.toFloat(), paint!!)
                        }
                    }

                    if (paint != null && needGridRightLeftEdge) {
                        canvas.drawRect(left.toFloat(), (top - space).toFloat(), (right + space).toFloat(), top.toFloat(), paint!!)
                    }

                } else if (spanIndex == spanCount - 1) {

                    val top = child.top
                    val bottomTop = child.bottom
                    val bottom = bottomTop + space
                    val left = child.left
                    val right = child.right
                    val rightRight = right + space

                    if (getCurReverseLayout(parent.layoutManager)) {
                        if (paint != null) {
                            canvas.drawRect((left - space).toFloat(), (top - space).toFloat(), left.toFloat(), bottom.toFloat(), paint!!)
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(right.toFloat(), top.toFloat(), rightRight.toFloat(), bottom.toFloat(), paint!!)
                        }
                    }

                    if (paint != null && needGridRightLeftEdge) {
                        canvas.drawRect(left.toFloat(), bottomTop.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                    }

                } else {
                    val top = child.top
                    val bottomTop = child.bottom
                    val bottom = bottomTop + space
                    val left = child.left
                    val right = child.right
                    val rightRight = right + space

                    if (paint != null) {
                        canvas.drawRect(left.toFloat(), bottomTop.toFloat(), right.toFloat(), bottom.toFloat(), paint!!)
                    }

                    if (getCurReverseLayout(parent.layoutManager)) {
                        if (paint != null) {
                            canvas.drawRect((left - space).toFloat(), (top - space).toFloat(), left.toFloat(), bottom.toFloat(), paint!!)
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(right.toFloat(), top.toFloat(), rightRight.toFloat(), bottom.toFloat(), paint!!)
                        }
                    }
                }
                stagFixEnd(canvas, parent, child, i, currentPosition)
            }

            //第一行顶部间隔
            if (needFirstTopEdge && currentPosition >= offsetPosition
                    && currentPosition < offsetPosition + spanCount
                    && currentPosition != endDataPosition) {
                if (getCurReverseLayout(parent.layoutManager)) {
                    if (paint != null) {
                        canvas.drawRect(child.right.toFloat(), (child.top - space).toFloat(), (child.right + space).toFloat(), (child.bottom + space).toFloat(), paint!!)
                    }
                } else {
                    if (paint != null) {
                        canvas.drawRect((child.left - space).toFloat(), (child.top - space).toFloat(), child.left.toFloat(), (child.bottom + space).toFloat(), paint!!)
                    }
                }
            }

        }
    }

    /**
     * 针对瀑布流的优化处理，因为瀑布流有长短边
     */
    fun stagFixEnd(canvas: Canvas, parent: RecyclerView, child: View, i: Int, currentPosition: Int) {
        if (parent.layoutManager !is StaggeredGridLayoutManager) {
            return
        }
        if (orientation == OrientationHelper.HORIZONTAL) {
            if (i > 0) {
                val row = Math.ceil(((dataSize - 1).toFloat() / spanCount).toDouble()).toInt()
                val curRow = Math.ceil(((currentPosition - offsetPosition) / spanCount.toFloat()).toDouble()).toInt()
                if (row == curRow) {
                    stagFixHorizontal(canvas, parent, child, i - 1)
                }
            }
        } else {
            if (i > 0) {
                val row = Math.ceil(((dataSize - 1).toFloat() / spanCount).toDouble()).toInt()
                val curRow = Math.ceil(((currentPosition - offsetPosition) / spanCount.toFloat()).toDouble()).toInt()
                if (row == curRow) {
                    stagFixVertical(canvas, parent, child, i)
                }
            }
        }
    }

    /**
     * 补齐那些缺少的瀑布流，存在缺角需要补齐
     */
    fun stagFixVertical(canvas: Canvas, parent: RecyclerView, view: View, position: Int) {
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager) {
            val spanIndex = getSpanIndex(parent, view, view.layoutParams as RecyclerView.LayoutParams)
            if (spanIndex != 0) {
                if (getCurReverseLayout(layoutManager)) {
                    //倒过来的，最后的旁边那个，应该加1，因为减1就是loadmre的view了
                    val preView = parent.getChildAt(position + 1)
                    if (preView.top > view.top) {
                        canvas.drawRect((view.left - space).toFloat(), (view.top - space).toFloat(), view.left.toFloat(), preView.top.toFloat(), paint!!)
                    }
                } else {
                    val preView = parent.getChildAt(position - 1)
                    if (preView.bottom < view.bottom) {
                        canvas.drawRect((view.left - space).toFloat(), preView.bottom.toFloat(), view.left.toFloat(), (view.bottom + space).toFloat(), paint!!)
                    }
                }
            }
        }
    }

    /**
     * 补齐那些缺少的瀑布流，存在缺角需要补齐
     */
    fun stagFixHorizontal(canvas: Canvas, parent: RecyclerView, view: View, position: Int) {
        val layoutManager = parent.layoutManager
        if (layoutManager is StaggeredGridLayoutManager) {
            val spanIndex = getSpanIndex(parent, view, view.layoutParams as RecyclerView.LayoutParams)
            if (spanIndex != 0) {
                val preView = parent.getChildAt(position)
                if (getCurReverseLayout(layoutManager)) {
                    if (preView.left > view.left) {
                        canvas.drawRect(view.left.toFloat(), (view.top - space).toFloat(), preView.left.toFloat(), view.top.toFloat(), paint!!)
                    }
                } else {
                    if (preView.right < view.right) {
                        canvas.drawRect(preView.right.toFloat(), (view.top - space).toFloat(), (view.right + space).toFloat(), view.top.toFloat(), paint!!)
                    }
                }
            }
        }
    }

    protected fun getSpanIndex(parent: RecyclerView, view: View, layoutParams: RecyclerView.LayoutParams): Int {
        if (layoutParams is GridLayoutManager.LayoutParams) {
            val currentPosition = parent.getChildAdapterPosition(view)
            return (currentPosition - offsetPosition) % spanCount
        } else if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
            return layoutParams.spanIndex
        }
        return 0
    }

    protected fun getOrientation(layoutManager: RecyclerView.LayoutManager): Int {
        if (layoutManager is GridLayoutManager) {
            return layoutManager.orientation
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return layoutManager.orientation
        }
        return 0
    }

    protected fun getSpanCount(layoutManager: RecyclerView.LayoutManager): Int {
        if (layoutManager is GridLayoutManager) {
            return layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return layoutManager.spanCount
        }
        return 0
    }

    private fun getCurReverseLayout(layoutManager: RecyclerView.LayoutManager): Boolean {
        if (layoutManager is GridLayoutManager) {
            return layoutManager.reverseLayout
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return layoutManager.reverseLayout
        } else if (layoutManager is LinearLayoutManager) {
            return layoutManager.reverseLayout
        }
        return false
    }


    fun setPaint(paint: Paint) {
        this.paint = paint
    }

    fun setSpace(space: Int) {
        this.space = space
    }

    fun setColor(color: Int) {
        this.color = color
        if (paint != null) {
            paint!!.color = color
        }
    }

    fun setNeedGridRightLeftEdge(needGridRightLeftEdge: Boolean) {
        this.needGridRightLeftEdge = needGridRightLeftEdge
    }

    fun setNeedFirstTopEdge(needFirstTopEdge: Boolean) {
        this.needFirstTopEdge = needFirstTopEdge
    }
}