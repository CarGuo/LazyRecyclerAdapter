package example.shuyu.recycler.kotlin.chat.detail.dagger.component


import dagger.Component
import example.shuyu.recycler.kotlin.chat.detail.ChatDetailActivity
import example.shuyu.recycler.kotlin.chat.detail.dagger.ChatDetailSingleton
import example.shuyu.recycler.kotlin.chat.detail.dagger.module.ChatDetailManagerModule
import example.shuyu.recycler.kotlin.chat.detail.dagger.module.ChatDetailPresenterModule
import example.shuyu.recycler.kotlin.chat.detail.dagger.module.ChatDetailViewModule

@ChatDetailSingleton
@Component(modules = arrayOf(ChatDetailPresenterModule::class, ChatDetailManagerModule::class, ChatDetailViewModule::class))
open interface ChatDetailComponent {
    fun inject(activity: ChatDetailActivity)
}
