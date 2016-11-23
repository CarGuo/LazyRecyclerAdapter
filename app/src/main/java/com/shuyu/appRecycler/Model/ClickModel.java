package com.shuyu.apprecycler.Model;

import com.shuyu.CommonRecyclerAdapter.model.RecyclerBaseModel;

/**
 * Created by shuyu on 2016/11/23.
 */

public class ClickModel extends RecyclerBaseModel {
    private String btnText;

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String text) {
        this.btnText = text;
    }
}
