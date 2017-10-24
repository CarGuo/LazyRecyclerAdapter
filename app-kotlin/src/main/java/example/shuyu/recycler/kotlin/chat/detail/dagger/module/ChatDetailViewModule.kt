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
open class ChatDetailViewModule(val mChatDetailViewOption: ChatDetailViewOption) {

    @Provides
    fun provideChatDetailViewOption(): ChatDetailViewOption {
        return mChatDetailViewOption
    }

    @Provides
    fun provideRecyclerView(): RecyclerView {
        return mChatDetailViewOption.recyclerView!!
    }

    @Provides
    fun provideboardLayout(): KeyBoardLockLayout {
        return mChatDetailViewOption.mChatDetailActivityKeyboardLayout!!
    }

    @Provides
    fun provideEmojiLayout(): EmojiLayout {
        return mChatDetailViewOption.mChatDetailActivitySendEmojiLayout!!
    }

    @Provides
    fun provideBottomMenu(): ChatDetailBottomView {
        return mChatDetailViewOption.mChatDetailActivityBottomMenu!!
    }

    @Provides
    fun provideRichEditText(): RichEditText {
        return mChatDetailViewOption.mChatDetailActivityEdit!!
    }
}
