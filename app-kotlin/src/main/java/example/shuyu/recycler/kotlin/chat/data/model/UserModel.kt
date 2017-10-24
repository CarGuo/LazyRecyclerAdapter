package example.shuyu.recycler.kotlin.chat.data.model

import java.io.Serializable

/**
 * 用户数据
 * Created by guoshuyu on 2017/9/4.
 */

open class UserModel : Serializable {

    var userId: String? = null

    var userName: String? = null

    var userPic: String? = null
}
