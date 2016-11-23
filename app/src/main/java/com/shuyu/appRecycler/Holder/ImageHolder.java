package com.shuyu.apprecycler.Holder;

import android.animation.AnimatorSet;
import android.content.Context;
import android.view.View;
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
        return null;
    }
}
