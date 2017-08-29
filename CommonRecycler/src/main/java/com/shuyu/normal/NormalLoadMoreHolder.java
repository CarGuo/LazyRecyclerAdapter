package com.shuyu.normal;

import android.content.Context;
import android.view.View;

/**
 * Created by guoshuyu on 2017/8/29.
 */

public abstract class NormalLoadMoreHolder extends NormalRecyclerBaseHolder {

    public enum LoadMoreState{
        LOAD_MORE_STATE,
        NULL_DATA_STATE,
        FAIL_STATE
    }


    public NormalLoadMoreHolder(Context context, View v) {
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

    public abstract void loadingState(Object object);

    public abstract void loadedAllState(Object object);

    public abstract void loadedFailState(Object object);

}
