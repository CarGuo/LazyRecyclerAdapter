package example.shuyu.recycler.kotlin.chat.detail

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.View

import com.shuyu.commonrecycler.listener.OnItemClickListener
import com.shuyu.commonrecycler.listener.OnLoadingListener
import com.shuyu.textutillib.EmojiLayout

import javax.inject.Inject

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.chat.detail.dagger.ChatDetailAdapter
import example.shuyu.recycler.kotlin.chat.detail.dagger.ChatDetailViewControl
import example.shuyu.recycler.kotlin.chat.detail.dagger.ChatDetailViewOption
import example.shuyu.recycler.kotlin.chat.detail.dagger.ChatSuperAdapterManager
import example.shuyu.recycler.kotlin.chat.detail.dagger.component.DaggerChatDetailComponent
import example.shuyu.recycler.kotlin.chat.detail.dagger.module.ChatDetailManagerModule
import example.shuyu.recycler.kotlin.chat.detail.dagger.module.ChatDetailPresenterModule
import example.shuyu.recycler.kotlin.chat.detail.dagger.module.ChatDetailViewModule
import example.shuyu.recycler.kotlin.chat.detail.view.ChatDetailBottomView
import kotlinx.android.synthetic.main.activity_chat_detail.*


/**
 * 聊天详情
 * Created by guoshuyu on 2017/9/4.
 */

class ChatDetailActivity : AppCompatActivity(), ChatDetailContract.IChatDetailView, OnItemClickListener, OnLoadingListener, View.OnTouchListener, View.OnClickListener {

    @Inject
    lateinit var mPresenter: ChatDetailPresenter
    @Inject
    lateinit var mNormalAdapterManager: ChatSuperAdapterManager
    @Inject
    lateinit var mAdapter: ChatDetailAdapter
    @Inject
    lateinit var mChatDetailViewControl: ChatDetailViewControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_detail)
        initTitle()
        initActivity()
        initListener()
    }

    private fun initTitle() {
        chat_detail_activity_toolbar?.title = "聊天详情"
        chat_detail_activity_toolbar?.setTitleTextColor(Color.WHITE)
        setSupportActionBar(chat_detail_activity_toolbar)
    }

    private fun initActivity() {

        val option = ChatDetailViewOption()
        option.setChatDetailActivityBottomMenu(chat_detail_activity_bottom_menu as ChatDetailBottomView)
                .setChatDetailActivityKeyboardLayout(chat_detail_activity_keyboard_layout)
                .setChatDetailActivitySendEmojiLayout(chat_detail_activity_send_emojiLayout as EmojiLayout)
                .setChatDetailActivityEdit(chat_detail_activity_edit)
                .setRecyclerViewOption(chat_detail_activity_recycler)

        DaggerChatDetailComponent.builder()
                .chatDetailPresenterModule(ChatDetailPresenterModule(this))
                .chatDetailManagerModule(ChatDetailManagerModule(this, this, this))
                .chatDetailViewModule(ChatDetailViewModule(option))
                .build()
                .inject(this)
    }

    private fun initListener() {
        chat_detail_activity_send.setOnClickListener(this)
        chat_detail_activity_edit.setOnClickListener(this)
        chat_detail_activity_emoji_btn.setOnClickListener(this)
        chat_detail_activity_bottom_btn.setOnClickListener(this)
        chat_detail_activity_edit.setOnFocusChangeListener { _, _ -> mChatDetailViewControl.showKeyBoradOnly() }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.chat_detail_activity_send -> {
                mPresenter.sendMsg(chat_detail_activity_edit?.text.toString())
            }
            R.id.chat_detail_activity_edit -> {
                mChatDetailViewControl.showKeyBoradOnly()
            }
            R.id.chat_detail_activity_emoji_btn -> {
                mChatDetailViewControl.showEmojiOnly()
            }
            R.id.chat_detail_activity_bottom_btn -> {
                mChatDetailViewControl.showBottomMenuOnly()
            }
        }
    }

    override fun onItemClick(context: Context, position: Int) {

    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (v is RecyclerView) {
            mChatDetailViewControl.hideAllMenuAndKebBoard()
        }
        return false
    }

    override fun onBackPressed() {
        if (mChatDetailViewControl.interceptBack()) {
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.release()
    }

    override fun onRefresh() {

    }

    override fun onLoadMore() {
        mPresenter.loadMoreData()
    }

    override fun notifyView() {
        mAdapter.notifyDataSetChanged()
    }


    override fun loadMoreComplete() {
        mNormalAdapterManager.loadMoreComplete()
    }

    override fun loadMoreEnd() {

        mNormalAdapterManager.setNoMore(true)
    }

    override fun sendSuccess() {
        chat_detail_activity_edit?.setText("")
        mAdapter.notifyDataSetChanged()
    }

    override val context: Context
        get() = this
}