package example.shuyu.recycler.kotlin.chat.data.factory.vo



import io.realm.RealmObject

/**
 * Created by guoshuyu on 2017/9/8.
 */

open class ChatMessageModel : RealmObject() {

    var id: String? = null

    var chatId: String? = null

    var content: String? = null

    var userModel: ChatUserModel? = null

    var type: Int = 0

    var createTime: Long = 0
}
