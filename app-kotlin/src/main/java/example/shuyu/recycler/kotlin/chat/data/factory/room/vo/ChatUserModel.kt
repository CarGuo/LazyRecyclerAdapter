package example.shuyu.recycler.kotlin.chat.data.factory.room.vo

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by guoshuyu on 2017/10/31.
 */


@Entity(tableName = "ChatUserModel")
open class ChatUserModel {
    @PrimaryKey
    @ColumnInfo(name = "userId")
    var userId: String = ""
    @ColumnInfo(name = "userName")
    var userName: String = ""
    @ColumnInfo(name = "userPic")
    var userPic: String = ""


}