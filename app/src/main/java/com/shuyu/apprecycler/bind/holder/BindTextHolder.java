package com.shuyu.apprecycler.bind.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.model.BindTextModel;
import com.shuyu.bind.NormalBindRecyclerBaseHolder;
/**
 * Created by guoshuyu on 2017/8/29.
 */

public class BindTextHolder extends NormalBindRecyclerBaseHolder {

    public final static int ID = R.layout.text_item;

    TextView itemText;

    public BindTextHolder(Context context, View v) {
        super(context, v);
    }

    @Override
    public void createView(View v) {
        itemText = (TextView) v.findViewById(R.id.item_text);
    }

    @Override
    public void onBind(Object model, int position) {
        BindTextModel textModel = (BindTextModel) (model);
        itemText.setText(textModel.getText());
    }
}
