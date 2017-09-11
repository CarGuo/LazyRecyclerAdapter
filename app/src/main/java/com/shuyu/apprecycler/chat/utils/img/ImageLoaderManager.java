package com.shuyu.apprecycler.chat.utils.img;

/**
 * 图片加载管理器
 * Created by guoshuyu on 2017/9/11.
 */

public class ImageLoaderManager {
    public static IImageLoader getImageLoaderManager() {
        return ImageLoaderGlide.newInstance();
    }
}
