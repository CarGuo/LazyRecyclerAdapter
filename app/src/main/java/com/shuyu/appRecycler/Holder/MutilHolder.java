package com.shuyu.apprecycler.Holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.shuyu.apprecycler.Model.MutilModel;
import com.shuyu.apprecycler.R;
import com.shuyu.common.RecyclerBaseHolder;
import com.shuyu.common.model.RecyclerBaseModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuyu on 2016/11/24.
 */

public class MutilHolder extends RecyclerBaseHolder {

    public final static int ID = R.layout.mutil_item;
    @BindView(R.id.item_image_1)
    ImageView itemImage1;
    @BindView(R.id.item_image_2)
    ImageView itemImage2;


    public MutilHolder(Context context, View v) {
        super(context, v);
    }

    @Override
    public void createView(View v) {
        ButterKnife.bind(this, v);
    }


    @Override
    public void onBind(RecyclerBaseModel model, int position) {
        MutilModel mutilModel = (MutilModel) model;
        itemImage1.setImageResource(mutilModel.getImage1());
        itemImage2.setImageResource(mutilModel.getImage2());
    }
}
