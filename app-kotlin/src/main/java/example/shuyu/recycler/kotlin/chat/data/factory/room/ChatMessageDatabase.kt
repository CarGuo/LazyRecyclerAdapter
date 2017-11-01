package example.shuyu.recycler.kotlin.chat.data.factory.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import example.shuyu.recycler.kotlin.chat.data.factory.room.vo.ChatMessageModel
import example.shuyu.recycler.kotlin.chat.data.factory.room.vo.ChatUserModel

/**
 * Created by guoshuyu on 2017/10/31.
 */
@Database(entities = arrayOf(ChatMessageModel::class, ChatUserModel::class), version = 1)
abstract class ChatMessageDatabase : RoomDatabase() {
    abstract fun chatMessageModelDao(): ChatMessageModelDao
    abstract fun chatUserModelDao(): ChatUserModelDao

    companion object {

        @Volatile private var INSTANCE: ChatMessageDatabase? = null

        fun getInstance(context: Context): ChatMessageDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        ChatMessageDatabase::class.java, "Sample.db")
                        .build()
    }
}