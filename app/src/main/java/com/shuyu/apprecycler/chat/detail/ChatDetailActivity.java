package com.shuyu.apprecycler.chat.detail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailAdapter;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailViewControl;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailViewOption;
import com.shuyu.apprecycler.chat.detail.dagger.ChatSuperAdapterManager;
import com.shuyu.apprecycler.chat.detail.dagger.component.DaggerChatDetailComponent;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailManagerModule;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailPresenterModule;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailViewModule;
import com.shuyu.apprecycler.chat.detail.view.ChatDetailBottomView;
import com.shuyu.apprecycler.chat.detail.view.ChatDetailEmojiLayout;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.listener.OnLoadingListener;
import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.KeyBoardLockLayout;
import com.shuyu.textutillib.RichEditText;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

/**
 * 聊天详情
 * Created by guoshuyu on 2017/9/4.
 */

public class ChatDetailActivity extends AppCompatActivity implements ChatDetailContract.IChatDetailView, OnItemClickListener, OnLoadingListener, View.OnTouchListener {

    @BindView(R.id.chat_detail_activity_toolbar)
    Toolbar mChatDetailActivityToolbar;
    @BindView(R.id.chat_detail_activity_recycler)
    RecyclerView mChatDetailActivityRecycler;
    @BindView(R.id.chat_detail_activity_edit)
    RichEditText mChatDetailActivityEdit;
    @BindView(R.id.chat_detail_activity_keyboard_layout)
    KeyBoardLockLayout mChatDetailActivityKeyboardLayout;
    @BindView(R.id.chat_detail_activity_send_emojiLayout)
    EmojiLayout mChatDetailActivitySendEmojiLayout;
    @BindView(R.id.chat_detail_activity_bottom_menu)
    ChatDetailBottomView mChatDetailActivityBottomMenu;

    @Inject
    ChatDetailPresenter mPresenter;
    @Inject
    ChatSuperAdapterManager mNormalAdapterManager;
    @Inject
    ChatDetailAdapter mAdapter;
    @Inject
    ChatDetailViewControl mChatDetailViewControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ChatDetailEmojiLayout.initEmoji(getContext());
        setContentView(R.layout.activity_chat_detail);
        ButterKnife.bind(this);
        initTitle();
        initActivity();
    }

    private void initTitle() {
        mChatDetailActivityToolbar.setTitle("聊天详情");
        mChatDetailActivityToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mChatDetailActivityToolbar);
    }

    @SuppressWarnings("unchecked")
    private void initActivity() {

        ChatDetailViewOption option = new ChatDetailViewOption();
        option.setChatDetailActivityBottomMenu(mChatDetailActivityBottomMenu)
                .setChatDetailActivityKeyboardLayout(mChatDetailActivityKeyboardLayout)
                .setChatDetailActivitySendEmojiLayout(mChatDetailActivitySendEmojiLayout)
                .setChatDetailActivityEdit(mChatDetailActivityEdit)
                .setRecyclerView(mChatDetailActivityRecycler);

        DaggerChatDetailComponent.builder()
                .chatDetailPresenterModule(new ChatDetailPresenterModule(this))
                .chatDetailManagerModule(new ChatDetailManagerModule(this, this, this))
                .chatDetailViewModule(new ChatDetailViewModule(option))
                .build()
                .inject(this);
    }

    @OnClick(R.id.chat_detail_activity_send)
    public void onSendClicked() {
        mPresenter.sendMsg(mChatDetailActivityEdit.getText().toString());
    }

    @OnClick(R.id.chat_detail_activity_edit)
    public void onEditClicked() {
        mChatDetailViewControl.showKeyBoradOnly();
    }

    @OnClick(R.id.chat_detail_activity_emoji_btn)
    public void onEmojiBtnClicked() {
        mChatDetailViewControl.showEmojiOnly();
    }

    @OnClick(R.id.chat_detail_activity_bottom_btn)
    public void onBottomViewBtnClicked() {
        mChatDetailViewControl.showBottomMenuOnly();
    }

    @OnFocusChange(R.id.chat_detail_activity_edit)
    public void onEditFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            mChatDetailViewControl.showKeyBoradOnly();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    //TODO 用diff判断
    @Override
    public void notifyView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendSuccess() {
        mChatDetailActivityEdit.setText("");
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(Context context, int position) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v instanceof RecyclerView) {
            mChatDetailViewControl.hideAllMenuAndKebBoard();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (mChatDetailViewControl.interceptBack()) {
            return;
        }
        super.onBackPressed();
    }
}
