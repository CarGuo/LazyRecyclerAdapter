package com.shuyu.apprecycler.chat.detail.dagger;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shuyu.apprecycler.chat.detail.ChatDetailPresenter;
import com.shuyu.bind.BindSuperAdapter;



import javax.inject.Inject;

/**
 * 继承之后实现注入
 * Created by guoshuyu on 2017/9/5.
 */
@ChatDetailSingleton
public class ChatDetailAdapter extends BindSuperAdapter {

    @Inject
    public ChatDetailAdapter(Context context, ChatSuperAdapterManager normalAdapterManager, ChatDetailPresenter chatDetailPresenter) {
        super(context, normalAdapterManager, chatDetailPresenter.getDataList());
    }


    @Inject
    public void initRecycler(Context context, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(this);
    }
}
