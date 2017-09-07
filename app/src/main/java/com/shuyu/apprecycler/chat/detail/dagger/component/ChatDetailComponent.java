package com.shuyu.apprecycler.chat.detail.dagger.component;


import com.shuyu.apprecycler.chat.detail.ChatDetailActivity;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailSingleton;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailManagerModule;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailPresenterModule;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailViewModule;


import dagger.Component;

@ChatDetailSingleton
@Component(modules = {ChatDetailPresenterModule.class, ChatDetailManagerModule.class, ChatDetailViewModule.class})
public interface ChatDetailComponent {
    void inject(ChatDetailActivity activity);
}
