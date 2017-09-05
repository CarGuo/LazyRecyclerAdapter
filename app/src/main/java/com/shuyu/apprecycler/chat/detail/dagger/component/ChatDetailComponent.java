package com.shuyu.apprecycler.chat.detail.dagger.component;


import com.shuyu.apprecycler.chat.detail.ChatDetailActivity;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailSingleton;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailPresenterModule;


import dagger.Component;

@ChatDetailSingleton
@Component(modules = {ChatDetailPresenterModule.class})
public interface ChatDetailComponent {
    void inject(ChatDetailActivity activity);
}
