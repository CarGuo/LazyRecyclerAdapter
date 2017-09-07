package com.shuyu.apprecycler.chat.detail.dagger.module;

import android.view.View;

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
    private final View.OnTouchListener mOnTouchListener;

    public ChatDetailManagerModule(OnItemClickListener onItemClickListener, OnLoadingListener loadingListener, View.OnTouchListener touchListener) {
        mOnItemClickListener = onItemClickListener;
        mLoadingListener = loadingListener;
        mOnTouchListener = touchListener;
    }

    @Provides
    OnItemClickListener provideOnItemClickListener() {
        return mOnItemClickListener;
    }

    @Provides
    OnLoadingListener provideOnLoadingListener() {
        return mLoadingListener;
    }

    @Provides
    View.OnTouchListener provideOnTouchListener() {
        return mOnTouchListener;
    }
}
