package com.shuyu.apprecycler.chat.detail.dagger;

import android.support.v7.widget.RecyclerView;

import com.shuyu.apprecycler.chat.detail.view.ChatDetailBottomView;
import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.KeyBoardLockLayout;
import com.shuyu.textutillib.RichEditText;


/**
 * view option
 * Created by guoshuyu on 2017/9/7.
 */

public class ChatDetailViewOption {

    RichEditText mChatDetailActivityEdit;
    KeyBoardLockLayout mChatDetailActivityKeyboardLayout;
    EmojiLayout mChatDetailActivitySendEmojiLayout;
    ChatDetailBottomView mChatDetailActivityBottomMenu;
    RecyclerView mRecyclerView;


    public RichEditText getChatDetailActivityEdit() {
        return mChatDetailActivityEdit;
    }

    public ChatDetailViewOption setChatDetailActivityEdit(RichEditText chatDetailActivityEdit) {
        this.mChatDetailActivityEdit = chatDetailActivityEdit;
        return this;
    }

    public KeyBoardLockLayout getChatDetailActivityKeyboardLayout() {
        return mChatDetailActivityKeyboardLayout;
    }

    public ChatDetailViewOption setChatDetailActivityKeyboardLayout(KeyBoardLockLayout chatDetailActivityKeyboardLayout) {
        this.mChatDetailActivityKeyboardLayout = chatDetailActivityKeyboardLayout;
        return this;
    }

    public EmojiLayout getChatDetailActivitySendEmojiLayout() {
        return mChatDetailActivitySendEmojiLayout;
    }

    public ChatDetailViewOption setChatDetailActivitySendEmojiLayout(EmojiLayout chatDetailActivitySendEmojiLayout) {
        this.mChatDetailActivitySendEmojiLayout = chatDetailActivitySendEmojiLayout;
        return this;
    }

    public ChatDetailBottomView getChatDetailActivityBottomMenu() {
        return mChatDetailActivityBottomMenu;
    }

    public ChatDetailViewOption setChatDetailActivityBottomMenu(ChatDetailBottomView chatDetailActivityBottomMenu) {
        this.mChatDetailActivityBottomMenu = chatDetailActivityBottomMenu;
        return this;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }
}
