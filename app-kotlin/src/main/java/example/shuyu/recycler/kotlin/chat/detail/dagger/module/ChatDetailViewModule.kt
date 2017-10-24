package example.shuyu.recycler.kotlin.chat.detail.dagger.module

import android.support.v7.widget.RecyclerView

import com.shuyu.textutillib.EmojiLayout
import com.shuyu.textutillib.KeyBoardLockLayout
import com.shuyu.textutillib.RichEditText

import dagger.Module
import dagger.Provides
import example.shuyu.recycler.kotlin.chat.detail.dagger.ChatDetailViewOption
import example.shuyu.recycler.kotlin.chat.detail.view.ChatDetailBottomView

/**
 * Created by guoshuyu on 2017/9/7.
 */
@Module
open class ChatDetailViewModule(private val chatDetailViewOption: ChatDetailViewOption) {

    @Provides
    fun provideChatDetailViewOption(): ChatDetailViewOption = chatDetailViewOption

    @Provides
    fun provideRecyclerView(): RecyclerView = chatDetailViewOption.recyclerView!!

    @Provides
    fun provideboardLayout(): KeyBoardLockLayout =
            chatDetailViewOption.mChatDetailActivityKeyboardLayout!!

    @Provides
    fun provideEmojiLayout(): EmojiLayout =
            chatDetailViewOption.mChatDetailActivitySendEmojiLayout!!

    @Provides
    fun provideBottomMenu(): ChatDetailBottomView =
            chatDetailViewOption.mChatDetailActivityBottomMenu!!

    @Provides
    fun provideRichEditText(): RichEditText = chatDetailViewOption.mChatDetailActivityEdit!!
}
