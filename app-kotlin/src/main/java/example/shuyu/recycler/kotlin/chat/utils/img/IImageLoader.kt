package example.shuyu.recycler.kotlin.chat.utils.img

import android.content.Context

/**
 * 图片加载接口
 * Created by guoshuyu on 2017/9/11.
 */

public interface IImageLoader {
    fun loadImage(context: Context, loadOption: LoadOption)
}
