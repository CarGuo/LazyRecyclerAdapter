package com.shuyu.apprecycler.normal.holder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.normal.model.TextModel;
import com.shuyu.common.normal.NormalRecyclerBaseHolder;
/**
 * Created by guoshuyu on 2017/8/29.
 */

public class TextHolder extends NormalRecyclerBaseHolder {

    public final static int ID = R.layout.text_item;

    TextView itemText;

    public TextHolder(Context context, View v) {
        super(context, v);
    }

    @Override
    public void createView(View v) {
        itemText = (TextView) v.findViewById(R.id.item_text);
    }

    @Override
    public void onBind(Object model, int position) {
        TextModel textModel = (TextModel) (model);
        itemText.setText(textModel.getText());
    }
}
