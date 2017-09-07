package com.shuyu.apprecycler.chat.detail.dagger.module;

import android.support.v7.widget.RecyclerView;

import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailViewOption;
import com.shuyu.apprecycler.chat.detail.view.ChatDetailBottomView;
import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.KeyBoardLockLayout;
import com.shuyu.textutillib.RichEditText;

import dagger.Module;
import dagger.Provides;

/**
 * Created by guoshuyu on 2017/9/7.
 */
@Module
public class ChatDetailViewModule {

    private final ChatDetailViewOption mChatDetailViewOption;

    public ChatDetailViewModule(ChatDetailViewOption chatDetailViewOption) {
        this.mChatDetailViewOption = chatDetailViewOption;
    }

    @Provides
    ChatDetailViewOption provideChatDetailViewOption() {
        return mChatDetailViewOption;
    }

    @Provides
    RecyclerView provideRecyclerView() {
        return mChatDetailViewOption.getRecyclerView();
    }

    @Provides
    public KeyBoardLockLayout provideboardLayout() {
        return mChatDetailViewOption.getChatDetailActivityKeyboardLayout();
    }

    @Provides
    public EmojiLayout provideEmojiLayout() {
        return mChatDetailViewOption.getChatDetailActivitySendEmojiLayout();
    }

    @Provides
    public ChatDetailBottomView provideBottomMenu() {
        return mChatDetailViewOption.getChatDetailActivityBottomMenu();
    }

    @Provides
    public RichEditText provideRichEditText() {
        return mChatDetailViewOption.getChatDetailActivityEdit();
    }
}
