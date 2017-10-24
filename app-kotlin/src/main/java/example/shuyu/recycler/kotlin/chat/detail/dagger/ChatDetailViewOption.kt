package example.shuyu.recycler.kotlin.chat.detail.dagger

import android.support.v7.widget.RecyclerView

import com.shuyu.textutillib.EmojiLayout
import com.shuyu.textutillib.KeyBoardLockLayout
import com.shuyu.textutillib.RichEditText

import example.shuyu.recycler.kotlin.chat.detail.view.ChatDetailBottomView


/**
 * view option
 * Created by guoshuyu on 2017/9/7.
 */

open class ChatDetailViewOption {

    var mChatDetailActivityEdit: RichEditText? = null
    var mChatDetailActivityKeyboardLayout: KeyBoardLockLayout? = null
    var mChatDetailActivitySendEmojiLayout: EmojiLayout? = null
    var mChatDetailActivityBottomMenu: ChatDetailBottomView? = null
    var recyclerView: RecyclerView? = null

    fun setChatDetailActivityEdit(chatDetailActivityEdit: RichEditText): ChatDetailViewOption {
        this.mChatDetailActivityEdit = chatDetailActivityEdit
        return this
    }


    fun setChatDetailActivityKeyboardLayout(chatDetailActivityKeyboardLayout: KeyBoardLockLayout): ChatDetailViewOption {
        this.mChatDetailActivityKeyboardLayout = chatDetailActivityKeyboardLayout
        return this
    }

    fun setChatDetailActivitySendEmojiLayout(chatDetailActivitySendEmojiLayout: EmojiLayout): ChatDetailViewOption {
        this.mChatDetailActivitySendEmojiLayout = chatDetailActivitySendEmojiLayout
        return this
    }


    fun setChatDetailActivityBottomMenu(chatDetailActivityBottomMenu: ChatDetailBottomView): ChatDetailViewOption {
        this.mChatDetailActivityBottomMenu = chatDetailActivityBottomMenu
        return this
    }

    fun setRecyclerViewOption(recyclerView: RecyclerView): ChatDetailViewOption {
        this.recyclerView = recyclerView
        return this
    }
}
