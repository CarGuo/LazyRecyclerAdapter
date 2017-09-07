package com.shuyu.apprecycler.chat.detail.dagger;

import android.content.Context;
import android.view.View;

import com.shuyu.apprecycler.chat.detail.ChatDetailPresenter;
import com.shuyu.apprecycler.chat.detail.view.ChatDetailBottomView;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.KeyBoardLockLayout;
import com.shuyu.textutillib.RichEditText;

import javax.inject.Inject;


/**
 * Created by guoshuyu on 2017/9/7.
 */
@ChatDetailSingleton
public class ChatDetailViewControl {

    private KeyBoardLockLayout mChatDetailActivityKeyboardLayout;

    private EmojiLayout mChatDetailActivitySendEmojiLayout;

    private ChatDetailBottomView mChatDetailActivityBottomMenu;

    @Inject
    public ChatDetailViewControl(ChatDetailViewOption chatDetailViewOption, ChatDetailPresenter chatDetailPresenter) {
        mChatDetailActivityBottomMenu = chatDetailViewOption.mChatDetailActivityBottomMenu;
        mChatDetailActivitySendEmojiLayout = chatDetailViewOption.mChatDetailActivitySendEmojiLayout;
        mChatDetailActivityKeyboardLayout = chatDetailViewOption.mChatDetailActivityKeyboardLayout;
        mChatDetailActivitySendEmojiLayout.setEditTextSmile(chatDetailViewOption.getChatDetailActivityEdit());
        mChatDetailActivityKeyboardLayout.setBottomView(mChatDetailActivitySendEmojiLayout);
        mChatDetailActivityBottomMenu.setDataList(chatDetailPresenter.getMenuList());
    }

    @Inject
    public void initListener(final ChatDetailPresenter chatDetailPresenter) {

        mChatDetailActivityKeyboardLayout.setKeyBoardStateListener(new KeyBoardLockLayout.KeyBoardStateListener() {
            @Override
            public void onState(boolean show) {
                if (show) {
                    mChatDetailActivitySendEmojiLayout.showKeyboard();
                } else {
                    mChatDetailActivitySendEmojiLayout.hideKeyboard();
                }
            }
        });

        mChatDetailActivityBottomMenu.setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Context context, int position) {
                chatDetailPresenter.sendMenuItem(position);
            }
        });
    }

    public void showKeyBoradOnly() {
        mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight();
    }

    public void showEmojiOnly() {
        mChatDetailActivityKeyboardLayout.setBottomView(mChatDetailActivitySendEmojiLayout);
        mChatDetailActivityBottomMenu.setVisibility(View.GONE);
        if (mChatDetailActivitySendEmojiLayout.getVisibility() == View.VISIBLE) {
            mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight();
        } else {
            mChatDetailActivityKeyboardLayout.showBottomViewLockHeight();
        }
    }

    public void showBottomMenuOnly() {
        mChatDetailActivityKeyboardLayout.setBottomView(mChatDetailActivityBottomMenu);
        mChatDetailActivitySendEmojiLayout.setVisibility(View.GONE);
        if (mChatDetailActivityBottomMenu.getVisibility() == View.VISIBLE) {
            mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight();
        } else {
            mChatDetailActivityKeyboardLayout.showBottomViewLockHeight();
        }
    }

    public void hideAllMenuAndKebBoard() {
        mChatDetailActivityBottomMenu.setVisibility(View.GONE);
        mChatDetailActivitySendEmojiLayout.setVisibility(View.GONE);
        mChatDetailActivitySendEmojiLayout.hideKeyboard();
    }

    public boolean interceptBack() {
        if (mChatDetailActivitySendEmojiLayout.getVisibility() == View.VISIBLE) {
            mChatDetailActivitySendEmojiLayout.setVisibility(View.GONE);
            return true;
        }

        if (mChatDetailActivityBottomMenu.getVisibility() == View.VISIBLE) {
            mChatDetailActivityBottomMenu.setVisibility(View.GONE);
            return true;
        }

        return false;
    }

}
