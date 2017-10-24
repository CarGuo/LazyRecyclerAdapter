package example.shuyu.recycler.kotlin.chat.utils.img

import android.widget.ImageView

/**
 * 加载配置
 * Created by guoshuyu on 2017/9/11.
 */

open class LoadOption {
    var errorRes: Int = 0
    var placeholderRes: Int = 0
    var circleCrop: Boolean = false
    var url: String? = null
    var imageView: ImageView? = null

    fun setErrorRes(errorRes: Int): LoadOption {
        this.errorRes = errorRes
        return this
    }


    fun setPlaceholderRes(placeholderRes: Int): LoadOption {
        this.placeholderRes = placeholderRes
        return this
    }

    fun setUrl(url: String): LoadOption {
        this.url = url
        return this
    }

    fun setImageView(imageView: ImageView): LoadOption {
        this.imageView = imageView
        return this
    }

    fun setCircleCrop(circleCrop: Boolean): LoadOption {
        this.circleCrop = circleCrop
        return this
    }
}
