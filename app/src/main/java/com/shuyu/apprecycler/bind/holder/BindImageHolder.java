package com.shuyu.apprecycler.bind.holder;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.model.BindImageModel;
import com.shuyu.bind.NormalBindRecyclerBaseHolder;

/**
 * Created by guoshuyu on 2017/8/29.
 */

public class BindImageHolder extends NormalBindRecyclerBaseHolder {

    public final static int ID = R.layout.image_item;

    private ImageView itemImage;

    public BindImageHolder(Context context, View v) {
        super(context, v);
    }


    @Override
    public void createView(View v) {
        itemImage = (ImageView) v.findViewById(R.id.item_image);
    }

    @Override
    public void onBind(Object model, int position) {
        BindImageModel imageModel = (BindImageModel) model;
        itemImage.setImageResource(imageModel.getResId());
    }

    @Override
    public AnimatorSet getAnimator(View view) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator animator = ObjectAnimator.ofFloat(view, "translationY", dip2px(context, 80), 0);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator(.5f));

        Animator animatorSx = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator(.5f));

        Animator animatorSy = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);
        animator.setDuration(500);
        animator.setInterpolator(new OvershootInterpolator(.5f));

        animatorSet.playTogether(animator, animatorSx, animatorSy);
        return animatorSet;
    }

    /**
     * dip转为PX
     */
    public static int dip2px(Context context, float dipValue) {
        float fontScale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * fontScale + 0.5f);
    }
}
