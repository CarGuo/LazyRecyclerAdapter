package example.shuyu.recycler.kotlin.chat.detail.view

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView


import com.shuyu.commonrecycler.BindRecyclerBaseHolder
import com.shuyu.commonrecycler.BindSuperAdapter
import com.shuyu.commonrecycler.BindSuperAdapterManager
import com.shuyu.commonrecycler.listener.OnItemClickListener

import java.util.ArrayList

import example.shuyu.recycler.kotlin.R
import kotlinx.android.synthetic.main.chat_detail_bottom_view.view.*

/**
 * Created by guoshuyu on 2017/9/7.
 */

open class ChatDetailBottomView : LinearLayout {

    private var mBindSuperAdapter: BindSuperAdapter? = null

    private var mDataList: ArrayList<Any> = ArrayList()

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

        val bindSuperAdapterManager = BindSuperAdapterManager()
        bindSuperAdapterManager
                .setPullRefreshEnabled(false)
                .setLoadingMoreEnabled(false)
                .bind(ChatDetailBottomMenuModel::class.java, R.layout.chat_detail_bottom_menu_item, ChatDetailBottomMenuHolder::class.java)
                .setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(context: Context, position: Int) {
                        mClickListener?.onItemClick(context, position)
                    }
                })
        mBindSuperAdapter = BindSuperAdapter(context, bindSuperAdapterManager, mDataList)
        chat_detail_bottom_recycler.layoutManager = GridLayoutManager(context, 4)
        chat_detail_bottom_recycler.adapter = mBindSuperAdapter
    }

    class ChatDetailBottomMenuHolder(v: View) : BindRecyclerBaseHolder(v) {


        lateinit var mChatDetailBottomMenuItemImg: ImageView

        lateinit var mChatDetailBottomMenuItemTxt: TextView

        override fun createView(v: View) {
            mChatDetailBottomMenuItemImg = v.findViewById(R.id.chat_detail_bottom_menu_item_img)
            mChatDetailBottomMenuItemTxt = v.findViewById(R.id.chat_detail_bottom_menu_item_txt)
        }

        override fun onBind(model: Any, position: Int) {
            val chatDetailBottomMenuModel = model as ChatDetailBottomMenuModel
            mChatDetailBottomMenuItemImg.setImageResource(chatDetailBottomMenuModel.menuRes)
            mChatDetailBottomMenuItemTxt.text = chatDetailBottomMenuModel.menuName

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
