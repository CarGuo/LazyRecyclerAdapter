package example.shuyu.recycler.kotlin.chat.detail.dagger

import android.content.Context
import android.view.View

import com.shuyu.commonrecycler.BindSuperAdapterManager
import com.shuyu.commonrecycler.listener.OnBindDataChooseListener
import com.shuyu.commonrecycler.listener.OnItemClickListener
import com.shuyu.commonrecycler.listener.OnLoadingListener

import javax.inject.Inject

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.bind.view.BindCustomLoadMoreFooter
import example.shuyu.recycler.kotlin.chat.data.model.ChatImageModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatTextModel
import example.shuyu.recycler.kotlin.chat.holder.ChatImageHolder
import example.shuyu.recycler.kotlin.chat.holder.ChatTextHolder

/**
 * 继承之后实现注入与初始化
 * Created by guoshuyu on 2017/9/5.
 */

@ChatDetailSingleton
open class ChatSuperAdapterManager @Inject constructor() : BindSuperAdapterManager() {

    @Inject
    fun initManager(context: Context, onItemClickListener: OnItemClickListener, loadingListener: OnLoadingListener, onTouchListener: View.OnTouchListener) {
        bind(ChatImageModel::class.java, R.layout.chat_layout_image_left, ChatImageHolder::class.java)
                .bind(ChatImageModel::class.java, R.layout.chat_layout_image_right, ChatImageHolder::class.java)
                .bind(ChatTextModel::class.java, R.layout.chat_layout_text_left, ChatTextHolder::class.java)
                .bind(ChatTextModel::class.java, R.layout.chat_layout_text_right, ChatTextHolder::class.java)
                .bingChooseListener(object : OnBindDataChooseListener {
                    override fun getCurrentDataLayoutId(`object`: Any, classType: Class<*>, position: Int, ids: List<Int>): Int {
                        if (`object` is ChatTextModel) {
                            return if (`object`.isMe) R.layout.chat_layout_text_right else R.layout.chat_layout_text_left
                        } else if (`object` is ChatImageModel) {
                            return if (`object`.isMe) R.layout.chat_layout_image_right else R.layout.chat_layout_image_left
                        }
                        return ids[ids.size - 1]
                    }
                })
                .setPullRefreshEnabled(false)
                .setLoadingMoreEnabled(false)
                .setFootView(BindCustomLoadMoreFooter(context))
                .setNeedAnimation(true)
                .setTouchListener(onTouchListener)
                .setOnItemClickListener(onItemClickListener)
                .setLoadingListener(loadingListener)
    }

}
