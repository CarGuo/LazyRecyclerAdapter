package example.shuyu.recycler.kotlin.chat.data.factory


import example.shuyu.recycler.kotlin.chat.data.model.ChatBaseModel

/**
 * 获取本地聊天数据毁掉
 * Created by guoshuyu on 2017/9/11.
 */

open interface ILocalChatDetailGetListener {
    fun getData(datList: List<ChatBaseModel>)
}
