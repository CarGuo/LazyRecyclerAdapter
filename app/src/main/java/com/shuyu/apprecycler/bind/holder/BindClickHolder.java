package com.shuyu.apprecycler.bind.holder;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.model.ClickModel;
import com.shuyu.bind.NormalRecyclerBaseHolder;

/**
 * Created by guoshuyu on 2017/8/29.
 */

public class BindClickHolder extends NormalRecyclerBaseHolder {

    public final static int ID = R.layout.click_item;

    Button itemButton;

    public BindClickHolder(Context context, View v) {
        super(context, v);
    }

    @Override
    public void createView(View v) {
        itemButton = (Button) v.findViewById(R.id.item_button);
    }

    @Override
    public void onBind(Object model, int position) {
        ClickModel clickModel = (ClickModel) model;
        itemButton.setText(clickModel.getBtnText());
    }
}
