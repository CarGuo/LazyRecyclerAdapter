package com.shuyu.bind.holder;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.shuyu.bind.BindRecyclerBaseHolder;
import com.shuyu.common.R;

/**
 * 数据为绑定等错误提示
 * Created by guoshuyu on 2017/8/29.
 */

public class BindErrorHolder extends BindRecyclerBaseHolder {

    public final static int ID = R.layout.error_item;

    private TextView errorText;

    public BindErrorHolder(View v) {
        super(v);
    }

    public BindErrorHolder(Context context, View v) {
        super(context, v);
    }

    @Override
    public void createView(View v) {
        errorText = (TextView) v.findViewById(R.id.error_text);
    }

    @Override
    public void onBind(Object model, int position) {
        String text = "model：<font color=\"#000000\"><big>" + model.getClass().getName() + "</big></font>    never bind";
        errorText.setText(Html.fromHtml(text));
    }
}
