package com.shuyu.apprecycler.chat.detail.dagger;

import android.content.Context;
import android.view.View;

import com.shuyu.apprecycler.chat.detail.ChatDetailPresenter;
import com.shuyu.apprecycler.chat.detail.view.ChatDetailBottomView;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.KeyBoardLockLayout;

import javax.inject.Inject;

/**
 * Created by guoshuyu on 2017/9/7.
 */
//ChatDetailSingleton
public class ChatViewControl {
    /*@Inject
    public ChatViewControl() {
    }

    @Inject
    void initControlView(final ChatDetailViewOption viewOption, final ChatDetailPresenter chatDetailPresenter) {
        viewOption.getChatDetailActivitySendEmojiLayout().setEditTextSmile(viewOption.getChatDetailActivityEdit());
        viewOption.getChatDetailActivityKeyboardLayout().setBottomView(viewOption.getChatDetailActivitySendEmojiLayout());
        viewOption.getChatDetailActivityBottomMenu().setDataList(chatDetailPresenter.getMenuList());

        viewOption.getChatDetailActivityKeyboardLayout().setKeyBoardStateListener(new KeyBoardLockLayout.KeyBoardStateListener() {
            @Override
            public void onState(boolean show) {
                if (show) {
                    viewOption.getChatDetailActivitySendEmojiLayout().showKeyboard();
                } else {
                    viewOption.getChatDetailActivitySendEmojiLayout().hideKeyboard();
                }
            }
        });

        viewOption.getChatDetailActivityBottomMenu().setClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Context context, int position) {
                chatDetailPresenter.sendMenuItem(position);
            }
        });



        mChatDetailActivityKeyboardLayout.setBottomView(mChatDetailActivitySendEmojiLayout);
        mChatDetailActivityBottomMenu.setVisibility(View.GONE);
        if (mChatDetailActivitySendEmojiLayout.getVisibility() == View.VISIBLE) {
            mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight();
        } else {
            mChatDetailActivityKeyboardLayout.showBottomViewLockHeight();
        }
    }*/
}
