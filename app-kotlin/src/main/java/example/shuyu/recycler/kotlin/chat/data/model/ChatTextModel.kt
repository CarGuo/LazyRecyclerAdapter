package example.shuyu.recycler.kotlin.chat.data.model

import java.io.Serializable

/**
 * 文本model
 * Created by guoshuyu on 2017/9/4.
 */

open class ChatTextModel : ChatBaseModel(), Serializable {

    var content: String? = null
}
