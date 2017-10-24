package example.shuyu.recycler.kotlin.chat.detail.dagger

import android.content.Context
import android.view.View
import com.shuyu.commonrecycler.listener.OnItemClickListener

import com.shuyu.textutillib.EmojiLayout
import com.shuyu.textutillib.KeyBoardLockLayout

import javax.inject.Inject

import example.shuyu.recycler.kotlin.chat.detail.ChatDetailPresenter
import example.shuyu.recycler.kotlin.chat.detail.view.ChatDetailBottomView


/**
 * Created by guoshuyu on 2017/9/7.
 */
@ChatDetailSingleton
open class ChatDetailViewControl @Inject
constructor(chatDetailViewOption: ChatDetailViewOption, chatDetailPresenter: ChatDetailPresenter) {

    private val mChatDetailActivityKeyboardLayout: KeyBoardLockLayout

    private val mChatDetailActivitySendEmojiLayout: EmojiLayout

    private val mChatDetailActivityBottomMenu: ChatDetailBottomView

    init {
        mChatDetailActivityBottomMenu = chatDetailViewOption.mChatDetailActivityBottomMenu!!
        mChatDetailActivitySendEmojiLayout = chatDetailViewOption.mChatDetailActivitySendEmojiLayout!!
        mChatDetailActivityKeyboardLayout = chatDetailViewOption.mChatDetailActivityKeyboardLayout!!
        mChatDetailActivitySendEmojiLayout.editTextSmile = chatDetailViewOption.mChatDetailActivityEdit
        mChatDetailActivityKeyboardLayout.setBottomView(mChatDetailActivitySendEmojiLayout)
        mChatDetailActivityBottomMenu.setDataList(chatDetailPresenter.menuList)
    }

    @Inject
    fun initListener(chatDetailPresenter: ChatDetailPresenter) {

        mChatDetailActivityKeyboardLayout.setKeyBoardStateListener { show ->
            if (show) {
                mChatDetailActivitySendEmojiLayout.showKeyboard()
            } else {
                mChatDetailActivitySendEmojiLayout.hideKeyboard()
            }
        }

        mChatDetailActivityBottomMenu.setClickListener(object : OnItemClickListener {
            override fun onItemClick(context: Context, position: Int) {
                chatDetailPresenter.sendMenuItem(position)
            }
        })
    }

    fun showKeyBoradOnly() {
        mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight()
    }

    fun showEmojiOnly() {
        mChatDetailActivityKeyboardLayout.setBottomView(mChatDetailActivitySendEmojiLayout)
        mChatDetailActivityBottomMenu.visibility = View.GONE
        if (mChatDetailActivitySendEmojiLayout.visibility == View.VISIBLE) {
            mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight()
        } else {
            mChatDetailActivityKeyboardLayout.showBottomViewLockHeight()
        }
    }

    fun showBottomMenuOnly() {
        mChatDetailActivityKeyboardLayout.setBottomView(mChatDetailActivityBottomMenu)
        mChatDetailActivitySendEmojiLayout.visibility = View.GONE
        if (mChatDetailActivityBottomMenu.visibility == View.VISIBLE) {
            mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight()
        } else {
            mChatDetailActivityKeyboardLayout.showBottomViewLockHeight()
        }
    }

    fun hideAllMenuAndKebBoard() {
        mChatDetailActivityBottomMenu.visibility = View.GONE
        mChatDetailActivitySendEmojiLayout.visibility = View.GONE
        mChatDetailActivitySendEmojiLayout.hideKeyboard()
    }

    fun interceptBack(): Boolean {
        if (mChatDetailActivitySendEmojiLayout.visibility == View.VISIBLE) {
            mChatDetailActivitySendEmojiLayout.visibility = View.GONE
            return true
        }

        if (mChatDetailActivityBottomMenu.visibility == View.VISIBLE) {
            mChatDetailActivityBottomMenu.visibility = View.GONE
            return true
        }

        return false
    }

}
