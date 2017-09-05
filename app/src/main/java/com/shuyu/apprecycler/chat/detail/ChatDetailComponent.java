package com.shuyu.apprecycler.chat.detail;


import dagger.Component;

@Component(modules = ChatDetailModule.class)
public interface ChatDetailComponent {
    void inject(ChatDetailActivity activity);
}
