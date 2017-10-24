package example.shuyu.recycler.kotlin.chat.data.factory.vo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by guoshuyu on 2017/9/8.
 */

open class ChatUserModel : RealmObject() {

    @PrimaryKey
    var userId: String? = null

    var userName: String? = null

    var userPic: String? = null
}
