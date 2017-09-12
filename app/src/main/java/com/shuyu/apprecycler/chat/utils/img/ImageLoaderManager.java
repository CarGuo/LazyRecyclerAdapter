package com.shuyu.apprecycler.chat.utils.img;

/**
 * 图片加载管理器
 * Created by guoshuyu on 2017/9/11.
 */

public class ImageLoaderManager {
    public static IImageLoader getImageLoaderManager() {
        //需要替换图片加载框架，只需要替换这个实例化即可
        return ImageLoaderGlide.newInstance();
    }
}
