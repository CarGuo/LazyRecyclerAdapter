package com.jcodecraeer.xrecyclerview.base;

/**
 * Created by guoshuyu on 2017/1/7.
 */

interface BaseRefreshInterface {

    void onMove(float delta);

    void refreshComplete();

    void setProgressStyle(int style);

    void setArrowImageView(int resid);

    boolean releaseAction();

    int getVisibleHeight();

    int getState();

    void setVisibleHeight(int height);

    void setState(int state);

    void reset();
}
