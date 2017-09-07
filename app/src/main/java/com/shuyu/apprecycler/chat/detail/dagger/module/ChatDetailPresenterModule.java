package com.shuyu.apprecycler.chat.detail.dagger.module;


import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.shuyu.apprecycler.chat.detail.ChatDetailContract;

import dagger.Module;
import dagger.Provides;

/**
 * 提供注入Presenter的参数
 * Created by guoshuyu on 2017/9/5.
 */
@Module
public class ChatDetailPresenterModule {
    private final ChatDetailContract.IChatDetailView mView;
    private final RecyclerView mRecycler;

    public ChatDetailPresenterModule(ChatDetailContract.IChatDetailView view, RecyclerView recyclerView) {
        this.mView = view;
        this.mRecycler = recyclerView;
    }

    @Provides
    ChatDetailContract.IChatDetailView provideChatDetailView() {
        return mView;
    }

    @Provides
    Context provideContext() {
        return mView.getContext();
    }

    @Provides
    RecyclerView provideRecyclerView() {
        return mRecycler;
    }
}
