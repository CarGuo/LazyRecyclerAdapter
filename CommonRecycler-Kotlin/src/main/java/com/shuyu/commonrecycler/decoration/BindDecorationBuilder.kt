package com.shuyu.commonrecycler.decoration

import android.graphics.Paint

import com.shuyu.commonrecycler.BindRecyclerAdapter

/**
 * 构造分割线
 * Created by guoshuyu on 2017/8/31.
 */

open class BindDecorationBuilder(private val bindRecyclerAdapter: BindRecyclerAdapter) {

    private var paint: Paint? = null

    private var space = -1

    private var color = -1

    private var needGridRightLeftEdge = true

    private var needFirstTopEdge = false

    /**
     * 画笔，设置画笔后颜色无效
     */
    fun setPaint(paint: Paint): BindDecorationBuilder {
        this.paint = paint
        return this
    }

    /**
     * 间隔大小
     */
    fun setSpace(space: Int): BindDecorationBuilder {
        this.space = space
        return this
    }

    /**
     * 设置颜色
     */
    fun setColor(color: Int): BindDecorationBuilder {
        this.color = color
        return this
    }

    /**
     * 网格类型左右是否需要边距
     */
    fun setNeedGridRightLeftEdge(needGridRightLeftEdge: Boolean): BindDecorationBuilder {
        this.needGridRightLeftEdge = needGridRightLeftEdge
        return this
    }

    /**
     * 第一行是否需要边距
     */
    fun setNeedFirstTopEdge(needFirstTopEdge: Boolean): BindDecorationBuilder {
        this.needFirstTopEdge = needFirstTopEdge
        return this
    }

    fun builder(): BindItemDecoration {
        val bindItemDecoration = BindItemDecoration(bindRecyclerAdapter)
        if (space != -1) {
            bindItemDecoration.setSpace(space)
        }
        if (color != -1) {
            bindItemDecoration.setColor(color)
        }
        if (paint != null) {
            bindItemDecoration.setPaint(paint!!)
        }
        bindItemDecoration.setNeedGridRightLeftEdge(needGridRightLeftEdge)
        bindItemDecoration.setNeedFirstTopEdge(needFirstTopEdge)
        return bindItemDecoration
    }
}
