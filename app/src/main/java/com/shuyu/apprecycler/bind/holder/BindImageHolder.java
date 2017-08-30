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
import com.shuyu.apprecycler.bind.model.BindMutliModel;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 * Created by guoshuyu on 2017/8/29.
 */

public class BindImageHolder extends BindRecyclerBaseHolder {

    public final static int ID = R.layout.image_item;

    private ImageView itemImage;

    /**
     * 二选一继承构造方法
     *
     * @param v layoutId实例化view
     */
    public BindImageHolder(Context context, View v) {
        super(context, v);
    }


    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    @Override
    public void createView(View v) {
        itemImage = (ImageView) v.findViewById(R.id.item_image);
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    @Override
    public void onBind(Object model, int position) {
        if (model instanceof BindMutliModel) {
            BindMutliModel imageModel = (BindMutliModel) model;
            itemImage.setImageResource(imageModel.getRes2());
        } else if (model instanceof BindImageModel) {
            BindImageModel imageModel = (BindImageModel) model;
            itemImage.setImageResource(imageModel.getResId());
        }
    }

    /**
     * 选择继承，默认返回null，实现后可返回item动画
     */
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
