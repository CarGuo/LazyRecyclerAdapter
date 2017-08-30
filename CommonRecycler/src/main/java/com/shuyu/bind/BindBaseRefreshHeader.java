package com.shuyu.bind;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.base.BaseRefreshHeader;


/**
 * 继承BaseRefreshHeader实现的下拉刷新
 * Created by guoshuyu on 2017/1/8.
 */

public abstract class BindBaseRefreshHeader extends BaseRefreshHeader {

    public int mScrollTime = 300;

    public int mResetTime = 500;

    public int mRefreshCompleteTime = 200;

    public int mState = STATE_NORMAL;

    public ViewGroup mContainer;


    public BindBaseRefreshHeader(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public BindBaseRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (ViewGroup) LayoutInflater.from(getContext())
                .inflate(getLayoutId(), null);
        addView(mContainer);
    }

    /**
     * 返回布局id
     */
    protected abstract int getLayoutId();

    /**
     * item的高度
     */
    protected abstract int getCurrentMeasuredHeight();

    /**
     * 添加view
     */
    protected abstract void addView(ViewGroup container);

    /**
     * 继承，必须要时需要实现样式
     *
     * @param style
     */
    @Override
    public void setProgressStyle(int style) {
    }

    /**
     * 继承，设置图标
     *
     * @param resid
     */
    @Override
    public void setArrowImageView(int resid) {
    }


    /**
     * 继承，获取状态
     *
     * @return
     */
    @Override
    public int getState() {
        return mState;
    }

    /**
     * 继承，刷新解释
     */
    @Override
    public void refreshComplete() {
        setState(STATE_DONE);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                reset();
            }
        }, mRefreshCompleteTime);
    }

    /**
     * 继承，view的可视高度
     *
     * @param height
     */
    @Override
    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    /**
     * 继承，view的可视高度
     */
    @Override
    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    /**
     * 继承，移动距离
     */
    @Override
    public void onMove(float delta) {
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) delta + getVisibleHeight());
            if (mState <= STATE_RELEASE_TO_REFRESH) { // 未处于刷新状态，更新箭头
                if (getVisibleHeight() > getCurrentMeasuredHeight()) {
                    setState(STATE_RELEASE_TO_REFRESH);
                } else {
                    setState(STATE_NORMAL);
                }
            }
        }
    }

    /**
     * 继承，释放动作
     */
    @Override
    public boolean releaseAction() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0) // not visible.
            isOnRefresh = false;

        if (getVisibleHeight() > getCurrentMeasuredHeight() && mState < STATE_REFRESHING) {
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }
        // refreshing and header isn't shown fully. do nothing.
        if (mState == STATE_REFRESHING && height <= getCurrentMeasuredHeight()) {
            //return;
        }
        int destHeight = 0; // default: scroll back to dismiss header.
        // is refreshing, just scroll back to show all the header.
        if (mState == STATE_REFRESHING) {
            destHeight = getCurrentMeasuredHeight();
        }
        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    /**
     * 继承，重置
     */
    @Override
    public void reset() {
        smoothScrollTo(0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                setState(STATE_NORMAL);
            }
        }, mResetTime);
    }

    protected void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(mScrollTime).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }


}
