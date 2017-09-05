package com.shuyu.apprecycler.chat.detail.dagger.module;


import com.shuyu.apprecycler.chat.detail.ChatDetailContract;

import dagger.Module;
import dagger.Provides;

/**
 *
 * Created by guoshuyu on 2017/9/5.
 */
@Module
public class ChatDetailPresenterModule {
    private final ChatDetailContract.IChatDetailView mView;

    public ChatDetailPresenterModule(ChatDetailContract.IChatDetailView view) {
        this.mView = view;
    }


    @Provides
    ChatDetailContract.IChatDetailView provideChatDetailView() {
        return mView;
    }

}
