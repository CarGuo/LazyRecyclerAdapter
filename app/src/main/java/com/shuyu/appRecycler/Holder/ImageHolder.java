package com.shuyu.apprecycler.Holder;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.shuyu.CommonRecyclerAdapter.RecyclerBaseHolder;
import com.shuyu.CommonRecyclerAdapter.model.RecyclerBaseModel;
import com.shuyu.apprecycler.Model.ImageModel;
import com.shuyu.apprecycler.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuyu on 2016/11/23.
 */

public class ImageHolder extends RecyclerBaseHolder {

    public final static int ID = R.layout.image_item;

    @BindView(R.id.item_image)
    ImageView itemImage;

    public ImageHolder(Context context, View v) {
        super(context, v);
    }


    @Override
    public void createView(View v) {
        ButterKnife.bind(this, v);
    }

    @Override
    public void onBind(RecyclerBaseModel model, int position) {
        ImageModel imageModel = (ImageModel) model;
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
