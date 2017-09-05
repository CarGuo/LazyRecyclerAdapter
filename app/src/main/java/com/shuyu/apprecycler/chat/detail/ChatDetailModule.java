package com.shuyu.apprecycler.chat.detail;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by guoshuyu on 2017/9/5.
 */
@Module
public class ChatDetailModule {
    private final ChatDetailContract.IChatDetailView mView;

    public ChatDetailModule(ChatDetailContract.IChatDetailView view) {
        this.mView = view;
    }


    @Provides
    ChatDetailContract.IChatDetailView provideChatDetailView() {
        return mView;
    }

}
