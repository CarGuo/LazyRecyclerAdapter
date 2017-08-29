package com.shuyu.common.listener;


public interface OnLoadMoreListener {
    void onLoadMore();

    void onScrolled(int firstVisible);
}
