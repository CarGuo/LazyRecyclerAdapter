package example.shuyu.recycler.kotlin.chat.utils.img

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Glide图片加载
 * Created by guoshuyu on 2017/9/11.
 */

open class ImageLoaderGlide private constructor() : IImageLoader {

    override fun loadImage(context: Context, loadOption: LoadOption) {
        val requestOptions = RequestOptions()
        if (loadOption.errorRes > 0) {
            requestOptions.error(loadOption.errorRes)
        }
        if (loadOption.placeholderRes > 0) {
            requestOptions.placeholder(loadOption.placeholderRes)
        }
        requestOptions.centerCrop()
        if (loadOption.circleCrop) {
            requestOptions.circleCrop()
        }
        Glide.with(context.applicationContext)
                .setDefaultRequestOptions(requestOptions)
                .load(loadOption.url).into(loadOption.imageView!!)
    }

    companion object {

        fun newInstance(): ImageLoaderGlide {
            return ImageLoaderGlide()
        }
    }
}
