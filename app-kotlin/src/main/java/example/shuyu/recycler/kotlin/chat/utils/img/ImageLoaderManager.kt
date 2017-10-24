package example.shuyu.recycler.kotlin.chat.utils.img

/**
 * 图片加载管理器
 * Created by guoshuyu on 2017/9/11.
 */

public object ImageLoaderManager {
    //需要替换图片加载框架，只需要替换这个实例化即可
    val imageLoaderManager: IImageLoader
        get() = ImageLoaderGlide.newInstance()
}
