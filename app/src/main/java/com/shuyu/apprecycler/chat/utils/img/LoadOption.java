package com.shuyu.apprecycler.chat.utils.img;

import android.widget.ImageView;

/**
 * 加载配置
 * Created by guoshuyu on 2017/9/11.
 */

public class LoadOption {
    private int errorRes;
    private int placeholderRes;
    private boolean circleCrop;
    private String url;
    private ImageView imageView;

    public int getErrorRes() {
        return errorRes;
    }

    public LoadOption setErrorRes(int errorRes) {
        this.errorRes = errorRes;
        return this;
    }

    public int getPlaceholderRes() {
        return placeholderRes;
    }

    public LoadOption setPlaceholderRes(int placeholderRes) {
        this.placeholderRes = placeholderRes;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public LoadOption setUrl(String url) {
        this.url = url;
        return this;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public LoadOption setImageView(ImageView imageView) {
        this.imageView = imageView;
        return this;
    }

    public boolean isCircleCrop() {
        return circleCrop;
    }

    public LoadOption setCircleCrop(boolean circleCrop) {
        this.circleCrop = circleCrop;
        return this;
    }
}
