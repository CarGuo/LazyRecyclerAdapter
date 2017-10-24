package example.shuyu.recycler.kotlin

import android.app.Application

import example.shuyu.recycler.kotlin.chat.utils.ChatConst

/**
 * Created by guoshuyu on 2017/9/8.
 */

open class LazyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ChatConst.ChatInit(this)
    }

}
