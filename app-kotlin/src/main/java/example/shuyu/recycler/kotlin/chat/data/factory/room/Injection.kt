package example.shuyu.recycler.kotlin.chat.data.factory.room

import example.shuyu.recycler.kotlin.LazyApplication

/**
 * Created by guoshuyu on 2017/10/31.
 */

/**
 * Enables injection of data sources.
 */
object Injection {

    fun provideChatUserModelSource(): ChatUserModelDao {
        val database = ChatMessageDatabase.getInstance(LazyApplication.INSTANCE)
        return database.chatUserModelDao()
    }

    fun provideChatMessageModelSource(): ChatMessageModelDao {
        val database = ChatMessageDatabase.getInstance(LazyApplication.INSTANCE)
        return database.chatMessageModelDao()
    }
}
