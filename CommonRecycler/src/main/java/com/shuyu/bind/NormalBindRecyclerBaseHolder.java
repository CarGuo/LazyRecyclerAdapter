package com.shuyu.bind;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 必须继承的BaseHolder
 * Created by Guo on 2015/11/23.
 */
public abstract class NormalBindRecyclerBaseHolder extends RecyclerView.ViewHolder {

    protected Context context = null;

    protected NormalBindRecyclerAdapter normalCommonRecyclerAdapter = null;

    public NormalBindRecyclerBaseHolder(View v) {
        this(v.getContext(), v);
    }

    public NormalBindRecyclerBaseHolder(Context context, View v) {
        super(v);
        createView(v);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setAdapter(NormalBindRecyclerAdapter commonRecyclerAdapter) {
        this.normalCommonRecyclerAdapter = commonRecyclerAdapter;
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
     * 动画
     */
    public AnimatorSet getAnimator(View view) {
        return null;
    }

}





