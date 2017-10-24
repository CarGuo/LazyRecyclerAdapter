package example.shuyu.recycler.kotlin.chat.detail.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


import com.shuyu.commonrecycler.BindRecyclerBaseHolder
import com.shuyu.commonrecycler.BindSuperAdapter
import com.shuyu.commonrecycler.BindSuperAdapterManager
import com.shuyu.commonrecycler.listener.OnItemClickListener

import java.util.ArrayList

import example.shuyu.recycler.kotlin.R

/**
 * Created by guoshuyu on 2017/9/7.
 */

open class ChatDetailBottomView : LinearLayout {


    internal var mChatDetailBottomRecycler: RecyclerView? = null

    internal var mBindSuperAdapter: BindSuperAdapter?=null

    internal var mDataList: ArrayList<Any> = ArrayList()

    internal var mClickListener: OnItemClickListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.chat_detail_bottom_view, this)
        //ButterKnife.bind(this);

        val bindSuperAdapterManager = BindSuperAdapterManager()
        bindSuperAdapterManager
                .setPullRefreshEnabled(false)
                .setLoadingMoreEnabled(false)
                .bind(ChatDetailBottomMenuModel::class.java, R.layout.chat_detail_bottom_menu_item, ChatDetailBottomMenuHolder::class.java)
                .setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(context: Context, position: Int) {
                        if (mClickListener != null) {
                            mClickListener!!.onItemClick(context, position)
                        }
                    }
                })
        mBindSuperAdapter = BindSuperAdapter(context, bindSuperAdapterManager, mDataList)
        mChatDetailBottomRecycler!!.layoutManager = GridLayoutManager(context, 4)
        mChatDetailBottomRecycler!!.adapter = mBindSuperAdapter
    }

    class ChatDetailBottomMenuHolder(v: View) : BindRecyclerBaseHolder(v) {


        internal var mChatDetailBottomMenuItemImg: ImageView? = null

        internal var mChatDetailBottomMenuItemTxt: TextView? = null

        override fun createView(v: View) {
            //ButterKnife.bind(this, v);
        }

        override fun onBind(model: Any, position: Int) {
            val chatDetailBottomMenuModel = model as ChatDetailBottomMenuModel
            mChatDetailBottomMenuItemImg!!.setImageResource(chatDetailBottomMenuModel.menuRes)
            mChatDetailBottomMenuItemTxt!!.text = chatDetailBottomMenuModel.menuName

        }
    }

    class ChatDetailBottomMenuModel {
        var menuName: String? = null
        var menuRes: Int = 0

        constructor(name: String, res: Int) {
            menuName = name
            menuRes = res
        }
    }


    fun setDataList(dataList: List<ChatDetailBottomMenuModel>?) {
        if (dataList != null) {
            this.mDataList.addAll(dataList)
            mBindSuperAdapter?.notifyDataSetChanged()
        }
    }

    fun setClickListener(clickListener: OnItemClickListener) {
        this.mClickListener = clickListener
    }
}
