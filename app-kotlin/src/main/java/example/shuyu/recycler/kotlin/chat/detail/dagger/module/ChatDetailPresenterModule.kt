package example.shuyu.recycler.kotlin.chat.detail.dagger.module


import android.content.Context

import dagger.Module
import dagger.Provides
import example.shuyu.recycler.kotlin.chat.detail.ChatDetailContract

/**
 * 提供注入Presenter的参数
 * Created by guoshuyu on 2017/9/5.
 */
@Module
open class ChatDetailPresenterModule(val mView: ChatDetailContract.IChatDetailView) {

    @Provides
    fun provideChatDetailView(): ChatDetailContract.IChatDetailView {
        return mView
    }

    @Provides
    fun provideContext(): Context {
        return mView.context
    }

}
