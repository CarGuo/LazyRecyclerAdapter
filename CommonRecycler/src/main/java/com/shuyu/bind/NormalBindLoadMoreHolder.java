package com.shuyu.bind;

import android.content.Context;
import android.view.View;

/**
 * 普通recycler加载更多的基类holder
 * Created by guoshuyu on 2017/8/29.
 */

public abstract class NormalBindLoadMoreHolder extends NormalBindRecyclerBaseHolder {

    public enum LoadMoreState {
        LOAD_MORE_STATE,
        NULL_DATA_STATE,
        FAIL_STATE
    }

    public NormalBindLoadMoreHolder(View v) {
        super(v);
    }

    public NormalBindLoadMoreHolder(Context context, View v) {
        super(context, v);
    }

    public void switchLoadMore(Object model, LoadMoreState loadMoreState) {
        switch (loadMoreState) {
            case LOAD_MORE_STATE:
                loadingState(model);
                break;
            case NULL_DATA_STATE:
                loadedAllState(model);
                break;
            case FAIL_STATE:
                loadedFailState(model);
                break;
        }
    }

    /**
     * 加载中
     */
    public abstract void loadingState(Object object);
    /**
     * 已经全部加载
     */
    public abstract void loadedAllState(Object object);
    /**
     * 加载失败
     */
    public abstract void loadedFailState(Object object);

}
