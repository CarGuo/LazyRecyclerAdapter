package example.shuyu.recycler.kotlin.chat.holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.shuyu.commonrecycler.BindRecyclerBaseHolder
import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.chat.data.model.ChatBaseModel
import example.shuyu.recycler.kotlin.chat.utils.img.ImageLoaderManager
import example.shuyu.recycler.kotlin.chat.utils.img.LoadOption

/**
 * holder基类，处理头像加载和姓名显示
 * Created by guoshuyu on 2017/9/5.
 */

open abstract class ChatBaseHolder(v: View) : BindRecyclerBaseHolder(v) {


    private lateinit  var mChatDetailHolderAvatar: ImageView
    private lateinit var mChatDetailHolderName: TextView

    override fun createView(v: View) {
        mChatDetailHolderName = v.findViewById(R.id.chat_detail_holder_name) as TextView
        mChatDetailHolderAvatar = v.findViewById(R.id.chat_detail_holder_avatar) as ImageView
    }

    override fun onBind(model: Any, position: Int) {
        val chatBaseModel = model as ChatBaseModel
        mChatDetailHolderName.text = chatBaseModel.userModel?.userName
        ImageLoaderManager.imageLoaderManager.loadImage(context!!,
                LoadOption()
                        .setCircleCrop(true)
                        .setPlaceholderRes(R.drawable.a2)
                        .setErrorRes(R.drawable.a1)
                        .setImageView(mChatDetailHolderAvatar)
                        .setUrl(chatBaseModel.userModel?.userPic!!))
    }

}
