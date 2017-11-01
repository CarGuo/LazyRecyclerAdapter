package example.shuyu.recycler.kotlin.chat.data.factory.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import example.shuyu.recycler.kotlin.chat.data.factory.room.vo.ChatMessageModel
import io.reactivex.Flowable

@Dao
interface ChatMessageModelDao {


    @Query("SELECT * FROM ChatMessageModel WHERE chatId = :arg0")
    fun getChatMessageList(chatId: String): Flowable<List<ChatMessageModel>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(user: ChatMessageModel)
}