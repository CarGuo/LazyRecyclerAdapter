package example.shuyu.recycler.kotlin.chat.detail

import android.os.Handler
import android.text.TextUtils


import java.util.Date
import java.util.UUID

import javax.inject.Inject

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.chat.data.factory.ILocalChatDetailGetListener
import example.shuyu.recycler.kotlin.chat.data.factory.LocalChatDBFactory
import example.shuyu.recycler.kotlin.chat.data.model.ChatBaseModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatImageModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatTextModel
import example.shuyu.recycler.kotlin.chat.data.model.UserModel
import example.shuyu.recycler.kotlin.chat.detail.dagger.ChatDetailSingleton
import example.shuyu.recycler.kotlin.chat.detail.view.ChatDetailBottomView
import example.shuyu.recycler.kotlin.chat.utils.ChatConst

/**
 * 聊天想起处理逻辑
 * Created by guoshuyu on 2017/9/4.
 */

@ChatDetailSingleton
open class ChatDetailPresenter @Inject
constructor(val mView: ChatDetailContract.IChatDetailView) : ChatDetailContract.IChatDetailPresenter<ChatBaseModel> {

    private val mDataList = ArrayList<Any>()

    private val mMenuList = ArrayList<ChatDetailBottomView.ChatDetailBottomMenuModel>()

    private val mHandler = Handler()

    override val dataList: ArrayList<Any>
        get() = mDataList

    override val menuList: List<ChatDetailBottomView.ChatDetailBottomMenuModel>
        get() = mMenuList

    init {
        init()
    }

    override fun loadMoreData(page: Int) {

    }

    override fun sendMsg(text: String) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        //发送文本
        val textModel = ChatTextModel()
        textModel.content = text
        resolveBaseData(textModel, ChatConst.defaultUser, ChatConst.TYPE_TEXT, true)
        resolveAutoReply(ChatConst.TYPE_TEXT)
    }

    override fun sendMenuItem(position: Int) {
        when (position) {
            0 -> {
                //发送图片
                val chatImageModel = ChatImageModel()
                chatImageModel.imgUrl = "http://osvlwlt4g.bkt.clouddn.com/17-9-6/50017724.jpg"
                resolveBaseData(chatImageModel, ChatConst.defaultUser, ChatConst.TYPE_IMAGE, true)
                resolveAutoReply(ChatConst.TYPE_IMAGE)
            }
        }

    }

    override fun release() {
        LocalChatDBFactory.chatDBManager.closeDB()
    }

    private fun init() {
        //初始化底部menu
        mMenuList.add(ChatDetailBottomView.ChatDetailBottomMenuModel("图片", R.mipmap.ic_launcher))
        //初始化读取本地数据库
        LocalChatDBFactory.chatDBManager.getChatMessage(ChatConst.CHAT_ID, 0, object : ILocalChatDetailGetListener{
            override fun getData(datList: List<ChatBaseModel>) {
                if (datList.isNotEmpty()) {
                    mDataList.addAll(datList)
                    mView.notifyView()
                }
            }
        })
    }

    /**
     * 获取图片
     */
    private fun replyImgMsg() {
        val chatImageModel = ChatImageModel()
        chatImageModel.imgUrl = "http://osvlwlt4g.bkt.clouddn.com/17-9-6/50017724.jpg"
        resolveBaseData(chatImageModel, ChatConst.replayUser, ChatConst.TYPE_IMAGE, false)
    }

    /**
     * 回复文本
     */
    private fun replyTextMsg() {
        val textModel = ChatTextModel()
        textModel.content = "我回复你啦，萌萌哒"
        resolveBaseData(textModel, ChatConst.replayUser, ChatConst.TYPE_TEXT, false)
    }

    /**
     * 聊天基础数据同意处理
     */
    private fun resolveBaseData(chatBaseModel: ChatBaseModel, chatUserModel: UserModel, type: Int, isMe: Boolean) {
        chatBaseModel.chatId = ChatConst.CHAT_ID
        chatBaseModel.chatType = type
        chatBaseModel.id = UUID.randomUUID().toString()
        chatBaseModel.isMe = isMe
        chatBaseModel.createTime = Date().time
        chatBaseModel.userModel = chatUserModel
        mDataList.add(0, chatBaseModel)
        mView.sendSuccess()
        LocalChatDBFactory.chatDBManager.saveChatMessage(chatBaseModel)
    }

    /**
     * 自动回复
     */
    private fun resolveAutoReply(type: Int) {
        mHandler.postDelayed({
            when (type) {
                ChatConst.TYPE_TEXT -> replyTextMsg()
                ChatConst.TYPE_IMAGE -> replyImgMsg()
            }
        }, 500)
    }

}
