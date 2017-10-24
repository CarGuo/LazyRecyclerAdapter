package example.shuyu.recycler.kotlin.chat.utils

import android.content.Context

import example.shuyu.recycler.kotlin.chat.data.model.UserModel
import example.shuyu.recycler.kotlin.chat.detail.view.ChatDetailEmojiLayout
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by guoshuyu on 2017/9/5.
 */

public object ChatConst {
    val TYPE_TEXT = 1
    val TYPE_IMAGE = 2

    val CHAT_ID = "2017-0101010aabbccjjekdj"

    private var sDefaultUser: UserModel? = null
    private var sReplayUser: UserModel? = null

    val defaultUser: UserModel
        get() {
            if (sDefaultUser == null) {
                sDefaultUser = UserModel()
                sDefaultUser!!.userId = "test_user"
                sDefaultUser!!.userName = "大喵"
                sDefaultUser!!.userPic = "http://img1.imgtn.bdimg.com/it/u=2165802874,1472309307&fm=214&gp=0.jpg"
            }
            return sDefaultUser!!
        }

    val replayUser: UserModel
        get() {
            if (sReplayUser == null) {
                sReplayUser = UserModel()
                sReplayUser!!.userId = "replay_user"
                sReplayUser!!.userName = "二喵"
                sReplayUser!!.userPic = "http://osvlwlt4g.bkt.clouddn.com/17-9-6/50017724.jpg"
            }
            return sReplayUser!!
        }

    fun ChatInit(context: Context) {
        ChatDetailEmojiLayout.initEmoji(context)
        Realm.init(context)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
    }

}
