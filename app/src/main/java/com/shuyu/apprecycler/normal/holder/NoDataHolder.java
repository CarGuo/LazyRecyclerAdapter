package com.shuyu.apprecycler.normal.holder;

import android.content.Context;
import android.view.View;

import com.shuyu.apprecycler.R;
import com.shuyu.bind.NormalRecyclerBaseHolder;

/**
 * Created by shuyu on 2016/11/23.
 * 没有数据
 */

public class NoDataHolder extends NormalRecyclerBaseHolder {

    public final static int ID = R.layout.no_data;

    public NoDataHolder(Context context, View v) {
        super(context, v);
    }

    @Override
    public void createView(View v) {
    }

    @Override
    public void onBind(Object model, int position) {
    }

    public static class NoDataModel {

    }
}
