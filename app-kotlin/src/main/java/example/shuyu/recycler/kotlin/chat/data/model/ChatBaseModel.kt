package example.shuyu.recycler.kotlin.chat.data.model

import java.io.Serializable

/**
 * 基础数据model
 * Created by guoshuyu on 2017/9/4.
 */

open class ChatBaseModel : Serializable {

    var chatId: String? = null

    var chatType: Int = 0

    var id: String? = null

    var isMe: Boolean = false

    var createTime: Long = 0

    var userModel: UserModel? = null
}
