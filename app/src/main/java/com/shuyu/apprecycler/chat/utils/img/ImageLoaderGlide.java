package com.shuyu.apprecycler.chat.utils.img;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Glide图片加载
 * Created by guoshuyu on 2017/9/11.
 */

public class ImageLoaderGlide implements IImageLoader {
    private ImageLoaderGlide() {

    }

    public static ImageLoaderGlide newInstance() {
        return new ImageLoaderGlide();
    }

    @Override
    public void loadImage(Context context, LoadOption loadOption) {
        RequestOptions requestOptions = new RequestOptions();
        if (loadOption.getErrorRes() > 0) {
            requestOptions.error(loadOption.getErrorRes());
        }
        if (loadOption.getPlaceholderRes() > 0) {
            requestOptions.placeholder(loadOption.getPlaceholderRes());
        }
        requestOptions.centerCrop();
        if (loadOption.isCircleCrop()) {
            requestOptions.circleCrop();
        }
        Glide.with(context.getApplicationContext())
                .setDefaultRequestOptions(requestOptions)
                .load(loadOption.getUrl()).into(loadOption.getImageView());
    }
}
