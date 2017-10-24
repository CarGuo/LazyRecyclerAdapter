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


    override fun saveChatMessage(baseModel: ChatBaseModel) {
        realmDB?.executeTransactionAsync { bgRealm ->
            val chatMessage = bgRealm.createObject(ChatMessageModel::class.java)
            val userList = bgRealm.where(ChatUserModel::class.java).equalTo("userId", baseModel.userModel!!.userId).findAll()
            val chatUser: ChatUserModel
            if (userList != null && userList.size > 0) {
                chatUser = userList[0]
            } else {
                chatUser = bgRealm.createObject(ChatUserModel::class.java, baseModel.userModel!!.userId)
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
     * TODO
     * 因为懒加载，所以realm没有分页改变
     * 所以需要对realmResults进行分页读取，读取完毕才可以close当前线程的realm
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
                        resolveMessageList(realmResults)
                    }
                    realm.close()
                    list
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { chatBaseModels ->
                    if (chatBaseModels != null && chatBaseModels.isNotEmpty()) {
                        listener.getData(chatBaseModels)
                    }
                }
    }

    override fun closeDB() {
        if (mRealmDB != null) {
            mRealmDB!!.close()
            mRealmDB = null
        }
    }

    private fun resolveMessageList(chatMessageModels: RealmResults<ChatMessageModel>?): List<ChatBaseModel> {
        val list = ArrayList<ChatBaseModel>()
        for (chatMessage in chatMessageModels!!) {
            when (chatMessage.type) {
                ChatConst.TYPE_TEXT -> {
                    val chatText = ChatTextModel()
                    chatText.content = chatMessage.content
                    cloneChatBaseModel(chatText, chatMessage)
                    list.add(chatText)
                }
                ChatConst.TYPE_IMAGE -> {
                    val chatImg = ChatImageModel()
                    chatImg.imgUrl = chatMessage.content?:""
                    cloneChatBaseModel(chatImg, chatMessage)
                    list.add(chatImg)
                }
            }
        }
        return list
    }

    private fun cloneChatBaseModel(chatBase: ChatBaseModel, chatMessage: ChatMessageModel) {
        chatBase.chatId = chatMessage.chatId
        chatBase.isMe = ChatConst.defaultUser.userId == chatMessage.userModel!!.userId
        chatBase.id = chatMessage.id
        chatBase.chatType = chatMessage.type
        chatBase.createTime = chatMessage.createTime
        val user = UserModel()
        user.userId = chatMessage.userModel!!.userId
        user.userPic = chatMessage.userModel!!.userPic
        user.userName = chatMessage.userModel!!.userName
        chatBase.userModel = user
    }

    private fun cloneToChatMessageModel(chatUser: ChatUserModel, message: ChatMessageModel, chatBase: ChatBaseModel) {
        message.id = chatBase.id
        message.chatId = chatBase.chatId
        message.type = chatBase.chatType
        message.createTime = chatBase.createTime
        chatUser.userName = chatBase.userModel!!.userName
        chatUser.userPic = chatBase.userModel!!.userPic
        message.userModel = chatUser
    }

    companion object {

        fun newInstance(): LocalChatBDRealmManager = LocalChatBDRealmManager()
    }
}
