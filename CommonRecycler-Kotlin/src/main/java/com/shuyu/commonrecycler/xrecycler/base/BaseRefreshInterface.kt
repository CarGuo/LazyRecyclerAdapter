package com.shuyu.commonrecycler.xrecycler.base

/**
 * Created by guoshuyu on 2017/1/7.
 */

open  interface BaseRefreshInterface {

    var visibleHeight: Int

    var state: Int

    fun onMove(delta: Float)

    fun refreshComplete()

    fun setProgressStyle(style: Int)

    fun setArrowImageView(resid: Int)

    fun releaseAction(): Boolean

    fun reset()
}
