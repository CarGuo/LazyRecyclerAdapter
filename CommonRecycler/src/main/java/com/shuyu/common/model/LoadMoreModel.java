package com.shuyu.common.model;


import android.graphics.drawable.Drawable;

/**
 * Created by shuyu on 2016/11/23.
 */

public class LoadMoreModel extends RecyclerBaseModel {
    private int color = -11;

    private Drawable progressColor;

    private String loadText = "加载中···";

    private String failText = "拉去失败了";

    private String nullText = "下面没有了";

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Drawable getProgressDrawable() {
        return progressColor;
    }

    public void setProgressDrawable(Drawable progressDrawable) {
        this.progressColor = progressDrawable;
    }

    public String getLoadText() {
        return loadText;
    }

    public void setLoadText(String loadText) {
        this.loadText = loadText;
    }

    public String getFailText() {
        return failText;
    }

    public void setFailText(String failText) {
        this.failText = failText;
    }

    public String getNullText() {
        return nullText;
    }

    public void setNullText(String nullText) {
        this.nullText = nullText;
    }
}
