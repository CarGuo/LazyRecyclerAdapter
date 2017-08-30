package com.shuyu.bind;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 必须继承的BaseHolder
 * Created by Guo on 2015/11/23.
 */
public abstract class BindRecyclerBaseHolder extends RecyclerView.ViewHolder {

    protected Context context = null;

    protected BindRecyclerAdapter normalCommonRecyclerAdapter = null;

    /**
     * 必须继承其中之一
     */
    public BindRecyclerBaseHolder(View v) {
        this(v.getContext(), v);
    }

    /**
     * 必须继承其中之一
     */
    public BindRecyclerBaseHolder(Context context, View v) {
        super(v);
        createView(v);
        this.context = context;
    }

    /**
     * 必须继承
     */
    public abstract void createView(View v);

    /**
     * 必须继承
     */
    public abstract void onBind(Object model, final int position);

    /**
     * 动画，默认为返回null，继承后可返回动画
     */
    public AnimatorSet getAnimator(View view) {
        return null;
    }

    public Context getContext() {
        return context;
    }

    public BindRecyclerAdapter getNormalCommonRecyclerAdapter() {
        return normalCommonRecyclerAdapter;
    }

    void setAdapter(BindRecyclerAdapter commonRecyclerAdapter) {
        this.normalCommonRecyclerAdapter = commonRecyclerAdapter;
    }

}





