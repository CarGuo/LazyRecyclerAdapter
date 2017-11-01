package example.shuyu.recycler.kotlin.chat.data.factory.room.vo

/**
 * Created by guoshuyu on 2017/10/31.
 */

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ChatMessageModel")
open class ChatMessageModel {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = ""

    @ColumnInfo(name = "chatId")
    var chatId: String = ""

    @ColumnInfo(name = "content")
    var content: String = ""

    @Embedded
    var userModel: ChatUserModel = ChatUserModel()

    @ColumnInfo(name = "type")
    var type: Int = 0

    @ColumnInfo(name = "createTime")
    var createTime: Long = 0;

}