package com.shuyu.commonrecycler.listener

/**
 * 加载回调
 */
open interface OnLoadingListener {
    //刷新
    fun onRefresh()

    //加载更多
    fun onLoadMore()
}