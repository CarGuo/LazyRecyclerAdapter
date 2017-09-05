package com.shuyu.apprecycler.chat.detail.dagger;

import android.content.Context;

import com.shuyu.apprecycler.chat.detail.ChatDetailPresenter;
import com.shuyu.bind.BindSuperAdapter;



import javax.inject.Inject;

/**
 * 继承之后
 * Created by guoshuyu on 2017/9/5.
 */

public class ChatDetailAdapter extends BindSuperAdapter {

    @Inject
    public ChatDetailAdapter(Context context, ChatSuperAdapterManager normalAdapterManager, ChatDetailPresenter chatDetailPresenter) {
        super(context, normalAdapterManager, chatDetailPresenter.getDataList());
    }
}
