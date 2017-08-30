package com.shuyu.apprecycler.bind.holder;

import android.view.View;
import android.widget.Button;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.model.BindClickModel;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 * Created by guoshuyu on 2017/8/29.
 */

public class BindClickHolder extends BindRecyclerBaseHolder {

    public final static int ID = R.layout.click_item;

    Button itemButton;

    /**
     * 二选一继承构造方法
     *
     * @param v layoutId实例化view
     */
    public BindClickHolder(View v) {
        super(v);
    }

    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    @Override
    public void createView(View v) {
        itemButton = (Button) v.findViewById(R.id.item_button);
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    @Override
    public void onBind(Object model, int position) {
        BindClickModel clickModel = (BindClickModel) model;
        itemButton.setText(clickModel.getBtnText());
    }
}
