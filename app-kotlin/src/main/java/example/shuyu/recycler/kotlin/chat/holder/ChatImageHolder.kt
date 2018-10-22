package example.shuyu.recycler.kotlin.chat.holder

import android.view.View
import android.widget.ImageView
import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.chat.data.model.ChatImageModel
import example.shuyu.recycler.kotlin.chat.utils.img.ImageLoaderManager
import example.shuyu.recycler.kotlin.chat.utils.img.LoadOption


/**
 * 聊天图片
 * Created by guoshuyu on 2017/9/4.
 */

open class ChatImageHolder(v: View) : ChatBaseHolder(v) {

    private lateinit var mChatDetailHolderImage: ImageView

    override fun createView(v: View) {
        super.createView(v)
        mChatDetailHolderImage = v.findViewById(R.id.chat_detail_holder_image)
    }

    override fun onBind(model: Any, position: Int) {
        super.onBind(model, position)
        val chatImageModel = model as ChatImageModel
        ImageLoaderManager.imageLoaderManager.loadImage(context!!,
                LoadOption()
                        .setCircleCrop(true)
                        .setPlaceholderRes(R.drawable.a2)
                        .setErrorRes(R.drawable.a1)
                        .setImageView(mChatDetailHolderImage)
                        .setUrl(chatImageModel.imgUrl))
    }
}
