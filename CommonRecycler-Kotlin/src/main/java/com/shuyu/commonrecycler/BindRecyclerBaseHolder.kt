package com.shuyu.commonrecycler

import android.animation.AnimatorSet
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 必须继承的BaseHolder
 * Created by Guo on 2015/11/23.
 */
open abstract class BindRecyclerBaseHolder
/**
 * 必须继承其中之一
 */
(context: Context, v: View) : RecyclerView.ViewHolder(v) {

    var context: Context? = null
        protected set

    var normalCommonRecyclerAdapter: BindRecyclerAdapter? = null
        protected set

    /**
     * 必须继承其中之一
     */
    constructor(v: View) : this(v.context, v) {}

    init {
        createView(v)
        this.context = context
    }

    /**
     * 必须继承
     */
    abstract fun createView(v: View)

    /**
     * 必须继承
     */
    abstract fun onBind(model: Any, position: Int)

    /**
     * 动画，默认为返回null，继承后可返回动画
     */
    open fun getAnimator(view: View): AnimatorSet? {
        return null
    }

    internal fun setAdapter(commonRecyclerAdapter: BindRecyclerAdapter) {
        this.normalCommonRecyclerAdapter = commonRecyclerAdapter
    }

}





