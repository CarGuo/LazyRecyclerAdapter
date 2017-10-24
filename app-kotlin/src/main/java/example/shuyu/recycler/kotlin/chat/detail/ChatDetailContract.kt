package example.shuyu.recycler.kotlin.chat.detail

import android.content.Context

/**
 * 接口
 * Created by guoshuyu on 2017/9/4.
 */

class ChatDetailContract {
    interface IChatDetailView {

        val context: Context
        fun notifyView()

        fun sendSuccess()
    }

    interface IChatDetailPresenter<T> {

        val dataList: ArrayList<Any>

        val menuList: List<*>

        fun loadMoreData(page: Int)

        fun sendMsg(text: String)

        fun sendMenuItem(position: Int)

        fun release()

    }
}
