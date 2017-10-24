package example.shuyu.recycler.kotlin.chat.data.factory


import example.shuyu.recycler.kotlin.chat.data.model.ChatBaseModel

/**
 * 本地数据库接口
 * Created by guoshuyu on 2017/9/11.
 */

open interface ILocalChatDBManager {

    fun saveChatMessage(baseModel: ChatBaseModel)

    fun getChatMessage(chatId: String, page: Int, listener: ILocalChatDetailGetListener)

    fun closeDB()
}
