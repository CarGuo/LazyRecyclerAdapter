package com.shuyu.apprecycler.bind.holder;

import android.content.Context;
import android.view.View;

import com.shuyu.apprecycler.R;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 * <p>
 * 空数据Holder
 * Created by shuyu on 2016/11/23.
 */

public class BindNoDataHolder extends BindRecyclerBaseHolder {

    public final static int ID = R.layout.no_data;

    /**
     * 二选一继承构造方法
     *
     * @param v layoutId实例化view
     */
    public BindNoDataHolder(Context context, View v) {
        super(context, v);
    }

    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    @Override
    public void createView(View v) {
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    @Override
    public void onBind(Object model, int position) {
    }

    public static class NoDataModel {

    }
}
