package example.shuyu.recycler.kotlin.chat.data.factory.room

import android.util.Log
import example.shuyu.recycler.kotlin.chat.data.factory.ILocalChatDBManager
import example.shuyu.recycler.kotlin.chat.data.factory.ILocalChatDetailGetListener
import example.shuyu.recycler.kotlin.chat.data.factory.room.vo.ChatMessageModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatBaseModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatImageModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatTextModel
import example.shuyu.recycler.kotlin.chat.data.model.UserModel
import example.shuyu.recycler.kotlin.chat.utils.ChatConst
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by guoshuyu on 2017/10/31.
 */

class LocalChatDBRoomManager private constructor() : ILocalChatDBManager {


    companion object {
        const val PAGESIZE = 10
        val newInstance: LocalChatDBRoomManager = LocalChatDBRoomManager()
    }

    private val disposable = CompositeDisposable()

    private var messageDao: ChatMessageModelDao? = null
    private var userDao: ChatUserModelDao? = null

    init {
        messageDao = Injection.provideChatMessageModelSource()
        userDao = Injection.provideChatUserModelSource()
    }

    override fun saveChatMessage(baseModel: ChatBaseModel?) {

    }

    override fun getChatMessage(chatId: String, page: Int, listener: ILocalChatDetailGetListener) {
        disposable.add(messageDao!!.getChatMessageList(chatId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listener.getData(resolveMessageList(it, 0))
                }, { error -> Log.e("LocalChatDBRoomManager", "Unable to get username", error) }))
    }

    override fun closeDB() {

    }


    private fun resolveMessageList(chatMessageModels: List<ChatMessageModel>?, page: Int): List<ChatBaseModel> {
        val list = ArrayList<ChatBaseModel>()
        chatMessageModels?.let {
            val startIndex = PAGESIZE * page
            val end = PAGESIZE * (page + 1) - 1
            val dataLength = chatMessageModels.size - 1
            val endIndex = if (end > dataLength) dataLength else end
            (startIndex..endIndex)
                    .map { i -> it[i] }
                    .forEach {
                        when (it.type) {
                            ChatConst.TYPE_TEXT -> {
                                val chatText = ChatTextModel()
                                chatText.content = it.content
                                cloneChatBaseModel(chatText, it)
                                list.add(chatText)
                            }
                            ChatConst.TYPE_IMAGE -> {
                                val chatImg = ChatImageModel()
                                chatImg.imgUrl = it.content!!
                                cloneChatBaseModel(chatImg, it)
                                list.add(chatImg)
                            }
                        }
                    }
        }
        return list
    }

    private fun cloneChatBaseModel(chatBase: ChatBaseModel?, chatMessage: ChatMessageModel?) {
        chatBase?.chatId = chatMessage?.chatId
        chatBase?.isMe = ChatConst.defaultUser?.userId == chatMessage?.userModel?.userId
        chatBase?.id = chatMessage?.id
        chatBase?.chatType = chatMessage?.type ?: 0
        chatBase?.createTime = chatMessage?.createTime ?: 0
        val user = UserModel()
        user.userId = chatMessage?.userModel?.userId
        user.userPic = chatMessage?.userModel?.userPic
        user.userName = chatMessage?.userModel?.userName
        chatBase?.userModel = user
    }
}
