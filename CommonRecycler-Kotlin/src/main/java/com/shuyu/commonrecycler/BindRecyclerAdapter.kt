package com.shuyu.commonrecycler

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.shuyu.commonrecycler.holder.BindErrorHolder
import com.shuyu.commonrecycler.xrecycler.XRecyclerView

/**
 * 通用绑定的adapter
 */
open class BindRecyclerAdapter(context: Context, //管理器
                               private val normalAdapterManager: BindBaseAdapterManager<*>, dataList: ArrayList<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var context: Context? = null

    //数据
    var dataList: ArrayList<Any>? = null

    //当前RecyclerView
    private var curRecyclerView: RecyclerView? = null

    //最后的位置
    var lastPosition = -1

    init {
        this.dataList = dataList
        this.context = context
    }

    /**
     * 更新
     */
    fun notifychange() {
        notifyDataSetChanged()
    }

    /**
     * 删除
     */
    fun remove(position: Int) {
        dataList?.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * 增加
     */
    fun add(`object`: Any, position: Int) {
        dataList?.add(position, `object`)
        notifyItemInserted(position)
    }

    /**
     * 往后添加数据
     */
    @Synchronized
    fun addListData(data: List<Any>) {
        dataList?.addAll(data)
        notifyDataSetChanged()
    }

    /**
     * 设置新数据
     */
    fun setListData(data: ArrayList<Any>) {
        dataList = data
        lastPosition = -1
        notifyDataSetChanged()
    }

    /**
     * 获取列表数据
     */
    fun getDataList(): List<Any>? = dataList

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        curRecyclerView = recyclerView
        val manager = recyclerView?.layoutManager
        if (manager is GridLayoutManager) {
            manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (isFooter(position))
                        manager.spanCount
                    else
                        1
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        val lp = holder?.itemView?.layoutParams
        if (lp != null
                && lp is StaggeredGridLayoutManager.LayoutParams
                && isFooter(holder.layoutPosition)) {
            lp.isFullSpan = true
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        //错误数据
        if (viewType == BindErrorHolder.ID) {
            val v = LayoutInflater.from(context).inflate(BindErrorHolder.ID, parent, false)
            return BindErrorHolder(context!!, v)
        }

        //是否显示没有数据页面
        if (normalAdapterManager.isShowNoData && dataList != null && dataList!!.size == 0) {
            return normalAdapterManager.getNoDataViewTypeHolder(context!!, parent)!!
        }

        val holder = normalAdapterManager.getViewTypeHolder(context!!, parent, viewType)

        //itemView 的点击事件
        if (normalAdapterManager.itemClickListener != null) {
            holder?.itemView?.setOnClickListener { normalAdapterManager.itemClickListener?.onItemClick(holder.itemView.context, curPosition(holder.position)) }
        }

        if (normalAdapterManager.itemLongClickListener != null) {
            holder?.itemView?.setOnLongClickListener { normalAdapterManager.itemLongClickListener?.onItemClick(holder.itemView.context, curPosition(holder.position))!! }
        }


        return holder!!

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position < 0)
            return

        if (normalAdapterManager.isShowNoData && dataList != null && dataList!!.size == 0) {
            val recyclerHolder = holder as BindRecyclerBaseHolder
            recyclerHolder.setAdapter(this)
            recyclerHolder.onBind(normalAdapterManager.noDataObject!!, position)
            return
        }

        val model = if (normalAdapterManager.isSupportLoadMore && position + 1 == itemCount) {
            Any()
        } else {
            dataList!![position]
        }


        if (holder is BindErrorHolder) {
            holder.onBind(model, position)
            return
        }


        val recyclerHolder = holder as BindRecyclerBaseHolder

        recyclerHolder.setAdapter(this)

        recyclerHolder.onBind(model, position)

        if (normalAdapterManager.needAnimation && recyclerHolder.getAnimator(recyclerHolder.itemView) != null
                && position > lastPosition) {

            recyclerHolder.getAnimator(recyclerHolder.itemView)!!.start()
            lastPosition = position

        } else if (position > lastPosition) {

            lastPosition = position

        }
    }

    override fun getItemCount(): Int {
        //如果是显示没有数据，那么也要一个item作为显示
        if (normalAdapterManager.isShowNoData && dataList!!.size == 0) {
            return 1
        }
        return if (normalAdapterManager.isSupportLoadMore) dataList!!.size + 1 else dataList!!.size

    }


    override fun getItemViewType(position: Int): Int {
        //如果位置不对，就返回
        if (position < 0 || !normalAdapterManager.isSupportLoadMore && position > itemCount - 1 || normalAdapterManager.isSupportLoadMore && position > itemCount)
            return 0

        //如果没有数据，就显示空页面
        if (normalAdapterManager.isShowNoData && dataList!!.size == 0) {
            return normalAdapterManager.noDataLayoutId
        }

        //返回需要显示的ID
        val `object` = dataList!![position]
        val modelToId = normalAdapterManager.modelToId[`object`.javaClass.name] as List<Int>


        if (modelToId.isEmpty()) {
            return BindErrorHolder.ID
        }

        var layoutId = modelToId[modelToId.size - 1]
        if (normalAdapterManager.normalBindDataChooseListener != null && modelToId.size > 1) {
            layoutId = normalAdapterManager.normalBindDataChooseListener!!.getCurrentDataLayoutId(`object`, `object`.javaClass, position, modelToId)
        }
        return if (-1 == layoutId || layoutId == 0 || layoutId == Integer.MAX_VALUE) {
            BindErrorHolder.ID
        } else layoutId
    }

    private fun isFooter(position: Int): Boolean {
        return if (normalAdapterManager.isSupportLoadMore) {
            position == itemCount - 1
        } else {
            false
        }
    }

    internal open fun curPosition(position: Int): Int {
        return if (curRecyclerView is XRecyclerView) {
            val xRecyclerView = curRecyclerView as XRecyclerView?
            val count = xRecyclerView!!.headersCount
            val refresh = xRecyclerView.isPullRefreshEnabled
            position - count - if (refresh) 1 else 0
        } else {
            position
        }
    }

    companion object {

        private val TAG = "BindRecyclerAdapter"
    }

}
