package com.shuyu.apprecycler.normal.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.shuyu.apprecycler.R;
import com.shuyu.common.RecyclerBaseHolder;
import com.shuyu.common.model.RecyclerBaseModel;
import com.shuyu.normal.NormalRecyclerBaseHolder;

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
