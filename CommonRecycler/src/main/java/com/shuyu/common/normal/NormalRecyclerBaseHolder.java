package com.shuyu.common.normal;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Guo on 2015/11/23.
 */
public abstract class NormalRecyclerBaseHolder extends RecyclerView.ViewHolder {

    protected Context context = null;

    protected NormalCommonRecyclerAdapter normalCommonRecyclerAdapter = null;

    public NormalRecyclerBaseHolder(Context context, View v) {
        super(v);
        createView(v);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setAdapter(NormalCommonRecyclerAdapter commonRecyclerAdapter) {
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





