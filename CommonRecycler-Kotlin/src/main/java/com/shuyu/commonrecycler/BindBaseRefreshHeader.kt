package com.shuyu.commonrecycler

import android.animation.ValueAnimator
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout

import com.shuyu.commonrecycler.xrecycler.base.BaseRefreshHeader


/**
 * 继承BaseRefreshHeader实现的下拉刷新
 * Created by guoshuyu on 2017/1/8.
 */

open abstract class BindBaseRefreshHeader : BaseRefreshHeader {

    var mScrollTime = 300

    var mResetTime = 500

    var mRefreshCompleteTime = 200

    /**
     * 继承，获取状态
     *
     * @return
     */
    override var state = BaseRefreshHeader.STATE_NORMAL

    lateinit var mContainer: ViewGroup

    /**
     * 返回布局id
     */
    protected abstract val layoutId: Int

    /**
     * item的高度
     */
    protected abstract val currentMeasuredHeight: Int

    /**
     * 继承，view的可视高度
     */
    /**
     * 继承，view的可视高度
     *
     * @param height
     */
    override var visibleHeight: Int
        get() {
            val lp = mContainer.layoutParams as LinearLayout.LayoutParams
            return lp.height
        }
        set(height) {
            var height = height
            if (height < 0) height = 0
            val lp = mContainer.layoutParams as LinearLayout.LayoutParams
            lp.height = height
            mContainer.layoutParams = lp
        }


    constructor(context: Context) : super(context) {
        initView()
    }

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    private fun initView() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = LayoutInflater.from(context)
                .inflate(layoutId, null) as ViewGroup
        addView(mContainer)
    }

    /**
     * 添加view
     */
    protected abstract fun addView(container: ViewGroup)

    /**
     * 继承，必须要时需要实现样式
     *
     * @param style
     */
    override fun setProgressStyle(style: Int) {}

    /**
     * 继承，设置图标
     *
     * @param resid
     */
    override fun setArrowImageView(resid: Int) {}

    /**
     * 继承，刷新解释
     */
    override fun refreshComplete() {
        state = BaseRefreshHeader.STATE_DONE
        Handler().postDelayed({ reset() }, mRefreshCompleteTime.toLong())
    }

    /**
     * 继承，移动距离
     */
    override fun onMove(delta: Float) {
        if (visibleHeight > 0 || delta > 0) {
            visibleHeight = delta.toInt() + visibleHeight
            if (state <= BaseRefreshHeader.STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (visibleHeight > currentMeasuredHeight) {
                    state = BaseRefreshHeader.STATE_RELEASE_TO_REFRESH
                } else {
                    state = BaseRefreshHeader.STATE_NORMAL
                }
            }
        }
    }

    /**
     * 继承，释放动作
     */
    override fun releaseAction(): Boolean {
        var isOnRefresh = false
        val height = visibleHeight
        if (height == 0)
        // not visible.
            isOnRefresh = false

        if (visibleHeight > currentMeasuredHeight && state < BaseRefreshHeader.STATE_REFRESHING) {
            state = BaseRefreshHeader.STATE_REFRESHING
            isOnRefresh = true
        }
        // refreshing and header isn't shown fully. do nothing.
        if (state == BaseRefreshHeader.STATE_REFRESHING && height <= currentMeasuredHeight) {
            //return;
        }
        var destHeight = 0 // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (state == BaseRefreshHeader.STATE_REFRESHING) {
            destHeight = currentMeasuredHeight
        }
        smoothScrollTo(destHeight)

        return isOnRefresh
    }

    /**
     * 继承，重置
     */
    override fun reset() {
        smoothScrollTo(0)
        Handler().postDelayed({ state = BaseRefreshHeader.STATE_NORMAL }, mResetTime.toLong())
    }

    protected fun smoothScrollTo(destHeight: Int) {
        val animator = ValueAnimator.ofInt(visibleHeight, destHeight)
        animator.setDuration(mScrollTime.toLong()).start()
        animator.addUpdateListener { animation -> visibleHeight = animation.animatedValue as Int }
        animator.start()
    }


}
