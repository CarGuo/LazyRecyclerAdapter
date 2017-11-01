package example.shuyu.recycler.kotlin.chat.data.factory.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

import example.shuyu.recycler.kotlin.chat.data.factory.room.vo.ChatUserModel
import io.reactivex.Flowable

@Dao
interface ChatUserModelDao {


    @Query("SELECT * FROM ChatUserModel WHERE userId = :arg0")
    fun getUserById(userId: String): Flowable<ChatUserModel>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: ChatUserModel)
}

