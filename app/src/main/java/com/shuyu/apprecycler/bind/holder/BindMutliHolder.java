package com.shuyu.apprecycler.bind.holder;

import android.view.View;
import android.widget.ImageView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.model.BindMutliModel;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 * Created by guoshuyu on 2017/8/29.
 */

public class BindMutliHolder extends BindRecyclerBaseHolder {

    public final static int ID = R.layout.mutil_item;

    ImageView itemImage1;

    ImageView itemImage2;

    /**
     * 二选一继承构造方法
     *
     * @param v layoutId实例化view
     */
    public BindMutliHolder(View v) {
        super(v);
    }

    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    @Override
    public void createView(View v) {
        itemImage1 = (ImageView) v.findViewById(R.id.item_image_1);
        itemImage2 = (ImageView) v.findViewById(R.id.item_image_2);
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    @Override
    public void onBind(Object model, int position) {
        BindMutliModel mutliModel = (BindMutliModel) model;
        itemImage1.setImageResource(mutliModel.getResId());
        itemImage2.setImageResource(mutliModel.getRes2());
    }
}
