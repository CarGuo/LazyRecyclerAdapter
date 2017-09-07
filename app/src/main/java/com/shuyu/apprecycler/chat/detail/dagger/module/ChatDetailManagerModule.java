package com.shuyu.apprecycler.chat.detail.dagger.module;

import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.listener.OnLoadingListener;

import dagger.Module;
import dagger.Provides;

/**
 * Created by guoshuyu on 2017/9/7.
 */
@Module
public class ChatDetailManagerModule {
    private final OnItemClickListener mOnItemClickListener;
    private final OnLoadingListener mLoadingListener;

    public ChatDetailManagerModule(OnItemClickListener onItemClickListener, OnLoadingListener loadingListener) {
        mOnItemClickListener = onItemClickListener;
        mLoadingListener = loadingListener;
    }

    @Provides
    OnItemClickListener provideOnItemClickListener() {
        return mOnItemClickListener;
    }

    @Provides
    OnLoadingListener provideOnOnLoadingListener() {
        return mLoadingListener;
    }
}
