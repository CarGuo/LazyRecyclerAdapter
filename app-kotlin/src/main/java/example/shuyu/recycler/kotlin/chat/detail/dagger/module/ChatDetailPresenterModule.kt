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
open class ChatDetailPresenterModule(private val chatView: ChatDetailContract.IChatDetailView) {

    @Provides
    fun provideChatDetailView(): ChatDetailContract.IChatDetailView = chatView

    @Provides
    fun provideContext(): Context = chatView.context

}
