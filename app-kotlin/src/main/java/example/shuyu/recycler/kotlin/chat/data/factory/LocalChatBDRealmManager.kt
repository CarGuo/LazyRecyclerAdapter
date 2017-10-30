package example.shuyu.recycler.kotlin.chat.data.factory


import java.util.ArrayList

import example.shuyu.recycler.kotlin.chat.data.factory.vo.ChatMessageModel
import example.shuyu.recycler.kotlin.chat.data.factory.vo.ChatUserModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatBaseModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatImageModel
import example.shuyu.recycler.kotlin.chat.data.model.ChatTextModel
import example.shuyu.recycler.kotlin.chat.data.model.UserModel
import example.shuyu.recycler.kotlin.chat.utils.ChatConst
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort

/**
 * realm 数据库
 * Created by guoshuyu on 2017/9/8.
 */

open class LocalChatBDRealmManager private constructor() : ILocalChatDBManager {

    companion object {
        const val PAGESIZE = 10
        val newInstance: LocalChatBDRealmManager = LocalChatBDRealmManager()
    }

    private var mRealmDB: Realm? = null

    private val realmDB: Realm?
        get() {
            if (mRealmDB == null) {
                mRealmDB = Realm.getDefaultInstance()
            }
            return mRealmDB
        }


    private val realm: Observable<Realm>
        get() = Observable.create { emitter ->
            val observableRealm = Realm.getDefaultInstance()
            emitter.onNext(observableRealm)
            emitter.onComplete()
        }


    override fun saveChatMessage(baseModel: ChatBaseModel?) {
        realmDB?.executeTransactionAsync { bgRealm ->
            val chatMessage = bgRealm.createObject(ChatMessageModel::class.java)
            val userList = bgRealm.where(ChatUserModel::class.java).equalTo("userId", baseModel?.userModel?.userId).findAll()
            val chatUser = if (userList != null && userList.size > 0) {
                userList[0]
            } else {
                bgRealm.createObject(ChatUserModel::class.java, baseModel?.userModel?.userId)
            }
            cloneToChatMessageModel(chatUser, chatMessage, baseModel)
            if (baseModel is ChatTextModel) {
                chatMessage.content = baseModel.content
            } else if (baseModel is ChatImageModel) {
                chatMessage.content = baseModel.imgUrl
            }
        }
    }

    /**
     * 因为懒加载，所以realm没有分页改变
     * 所以需要对realmResults进行分页读取，读取完毕才可以close当前线程的realm
     * realm有线程限制，比如realmResults在Schedulers.io()中不可用到其他线程
     */
    override fun getChatMessage(chatId: String, page: Int, listener: ILocalChatDetailGetListener) {


        realm.subscribeOn(Schedulers.io())
                .map { realm ->
                    val realmResults = realm.where(ChatMessageModel::class.java)
                            .equalTo("chatId", chatId)
                            .findAll().sort("createTime", Sort.DESCENDING)
                    val list = if (realmResults == null) {
                        ArrayList()
                    } else {
                        resolveMessageList(realmResults, page)
                    }
                    realm.close()
                    list
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { chatBaseModels ->
                    if (chatBaseModels != null && chatBaseModels.isNotEmpty()) {
                        listener.getData(chatBaseModels)
                    } else {
                        listener.getData(ArrayList())
                    }
                }

    }

    override fun closeDB() {
        if (mRealmDB != null) {
            mRealmDB?.close()
            mRealmDB = null
        }
    }

    private fun resolveMessageList(chatMessageModels: RealmResults<ChatMessageModel>?, page: Int): List<ChatBaseModel> {
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
                                chatImg.imgUrl = it.content ?: ""
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

    private fun cloneToChatMessageModel(chatUser: ChatUserModel?, message: ChatMessageModel?, chatBase: ChatBaseModel?) {
        message?.id = chatBase?.id
        message?.chatId = chatBase?.chatId
        message?.type = chatBase?.chatType ?: 0
        message?.createTime = chatBase?.createTime ?: 0
        chatUser?.userName = chatBase?.userModel?.userName
        chatUser?.userPic = chatBase?.userModel?.userPic
        message?.userModel = chatUser
    }
}
