package com.shuyu.apprecycler.Model;

import com.shuyu.CommonRecyclerAdapter.model.RecyclerBaseModel;

/**
 * Created by shuyu on 2016/11/23.
 */

public class TextModel extends RecyclerBaseModel {
    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
