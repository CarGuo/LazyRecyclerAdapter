package com.shuyu.apprecycler.chat.detail.dagger.module;

import android.support.v7.widget.RecyclerView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by guoshuyu on 2017/9/7.
 */
@Module
public class ChatDetailViewModule {
    private final RecyclerView mRecycler;

    public ChatDetailViewModule(RecyclerView recyclerView) {
        this.mRecycler = recyclerView;
    }

    @Provides
    RecyclerView provideRecyclerView() {
        return mRecycler;
    }
}
