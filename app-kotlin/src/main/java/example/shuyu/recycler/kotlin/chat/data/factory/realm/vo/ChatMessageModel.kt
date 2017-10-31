package example.shuyu.recycler.kotlin.chat.data.factory.realm.vo


import io.realm.RealmObject
import io.realm.annotations.RealmClass

/**
 * Created by guoshuyu on 2017/9/8.
 */

@RealmClass
open class ChatMessageModel : RealmObject() {


    open var id: String? = null

    open var chatId: String? = null

    open var content: String? = null

    open var userModel: ChatUserModel? = null

    open var type: Int = 0

    open var createTime: Long = 0
}
