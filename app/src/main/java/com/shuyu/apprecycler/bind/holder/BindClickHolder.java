package com.shuyu.apprecycler.bind.holder;

import android.view.View;
import android.widget.Button;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.model.BindClickModel;
import com.shuyu.bind.NormalBindRecyclerBaseHolder;

/**
 * Created by guoshuyu on 2017/8/29.
 */

public class BindClickHolder extends NormalBindRecyclerBaseHolder {

    public final static int ID = R.layout.click_item;

    Button itemButton;

    public BindClickHolder(View v) {
        super(v);
    }

    @Override
    public void createView(View v) {
        itemButton = (Button) v.findViewById(R.id.item_button);
    }

    @Override
    public void onBind(Object model, int position) {
        BindClickModel clickModel = (BindClickModel) model;
        itemButton.setText(clickModel.getBtnText());
    }
}
