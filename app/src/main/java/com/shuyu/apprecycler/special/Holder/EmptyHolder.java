package com.shuyu.apprecycler.special.Holder;

import android.content.Context;
import android.view.View;

import com.shuyu.apprecycler.R;
import com.shuyu.common.RecyclerBaseHolder;
import com.shuyu.common.model.RecyclerBaseModel;

/**
 * Created by shuyu on 2016/11/24.
 */

public class EmptyHolder extends RecyclerBaseHolder {

    public final static int ID = R.layout.empty_layout;

    public EmptyHolder(Context context, View v) {
        super(context, v);
    }

    @Override
    public void onBind(RecyclerBaseModel model, int position) {

    }

    @Override
    public void createView(View v) {

    }

}
