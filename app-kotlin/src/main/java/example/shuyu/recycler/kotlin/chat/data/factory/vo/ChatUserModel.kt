package example.shuyu.recycler.kotlin.chat.data.factory.vo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

/**
 * Created by guoshuyu on 2017/9/8.
 */
@RealmClass
open class ChatUserModel : RealmObject() {

    @PrimaryKey
    open var userId: String? = null

    open var userName: String? = null

    open var userPic: String? = null
}
