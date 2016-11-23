package com.shuyu.common;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shuyu.common.model.RecyclerBaseModel;

/**
 * Created by Guo on 2015/11/23.
 */
public abstract class RecyclerBaseHolder extends RecyclerView.ViewHolder {

    protected Context context = null;

    protected CommonRecyclerAdapter commonRecyclerAdapter = null;

    public RecyclerBaseHolder(Context context, View v) {
        super(v);
        createView(v);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setAdapter(CommonRecyclerAdapter commonRecyclerAdapter) {
        this.commonRecyclerAdapter = commonRecyclerAdapter;
    }

    /**
     * 必须继承
     */
    public abstract void createView(View v);

    /**
     * 必须继承
     */
    public abstract void onBind(RecyclerBaseModel model, final int position);

    /**
     * 动画
     */
    public AnimatorSet getAnimator(View view) {
        return null;
    }

}





