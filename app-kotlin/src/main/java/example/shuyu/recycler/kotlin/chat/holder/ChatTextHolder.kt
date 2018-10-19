package example.shuyu.recycler.kotlin.chat.holder

import android.view.View
import android.widget.TextView

import com.shuyu.textutillib.RichTextBuilder
import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.chat.data.model.ChatTextModel

/**
 * 聊天文本
 * Created by guoshuyu on 2017/9/4.
 */

open class ChatTextHolder(v: View) : ChatBaseHolder(v) {

    private lateinit var mChatDetailHolderText: TextView

    override fun createView(v: View) {
        super.createView(v)
        mChatDetailHolderText = v.findViewById(R.id.chat_detail_holder_text)
    }

    override fun onBind(model: Any, position: Int) {
        super.onBind(model, position)
        val chatTextModel = model as ChatTextModel
        val richTextBuilder = RichTextBuilder(context)
        richTextBuilder.setContent(chatTextModel.content)
                .setNeedUrl(false)
                .setNeedNum(false)
                .setTextView(mChatDetailHolderText)
                .build()
    }
}
