package example.shuyu.recycler.kotlin.chat.detail.dagger

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


import com.shuyu.commonrecycler.BindSuperAdapter

import javax.inject.Inject

import example.shuyu.recycler.kotlin.chat.detail.ChatDetailPresenter

/**
 * 继承之后实现注入
 * Created by guoshuyu on 2017/9/5.
 */
@ChatDetailSingleton
open class ChatDetailAdapter @Inject constructor(context: Context, normalAdapterManager: ChatSuperAdapterManager, chatDetailPresenter: ChatDetailPresenter) : BindSuperAdapter(context, normalAdapterManager, chatDetailPresenter.dataList) {


    @Inject
    fun initRecycler(context: Context, recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true)
        recyclerView.adapter = this
    }
}
