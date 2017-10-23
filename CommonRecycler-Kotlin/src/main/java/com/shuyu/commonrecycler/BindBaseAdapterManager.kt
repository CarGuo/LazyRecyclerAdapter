package com.shuyu.commonrecycler

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import com.shuyu.commonrecycler.listener.OnBindDataChooseListener
import com.shuyu.commonrecycler.listener.OnItemClickListener
import com.shuyu.commonrecycler.listener.OnItemLongClickListener

import java.lang.reflect.Constructor
import java.util.ArrayList
import java.util.HashMap

/**
 * 通用适配管理基类
 * Created by guoshuyu on 2017/8/28.
 */

open abstract class BindBaseAdapterManager<T : BindBaseAdapterManager<T>> {

    private val TAG = BindBaseAdapterManager::class.java.name

    //根据model类名绑定layoutId
    val modelToId = HashMap<String, ArrayList<Int>>()

    //根据id类名绑定holder
    val idToHolder = HashMap<Int, Class<out BindRecyclerBaseHolder>>()

    //没有数据的布局id
    var noDataLayoutId = -1
        private set

    //没有数据的object
    var noDataObject: Any? = null
        private set

    //没有数据的holder
    var noDataHolder: Class<out BindRecyclerBaseHolder>? = null
        private set

    //一个model对应多种Holder的数据的筛选，不设置默认使用最后一个
    var normalBindDataChooseListener: OnBindDataChooseListener? = null
        private set

    //单击
    lateinit var itemClickListener: OnItemClickListener

    //长按
    lateinit var itemLongClickListener: OnItemLongClickListener

    //是否支持动画
    var needAnimation = false

    val isSupportLoadMore: Boolean
        get() = false

    /**
     * 是否需要显示空数据
     */
    val isShowNoData: Boolean
        get() = noDataLayoutId != -1 && noDataHolder != null && noDataObject != null

    /**
     * 绑定加载更多模块显示
     *
     * @param modelClass  数据实体类名，一个model可以对用多个布局和Holder
     * @param layoutId    布局id，一个manager中，一个id只能对应一个holder。
     * @param holderClass holder类名，一个manager中，一个holder只能对应一个id。
     */
    fun bind(modelClass: Class<*>, layoutId: Int, holderClass: Class<out BindRecyclerBaseHolder>): T {

        if (!modelToId.containsKey(modelClass.name)) {
            val list = ArrayList<Int>()
            list.add(layoutId)
            modelToId.put(modelClass.name, list)
        } else {
            val list = modelToId[modelClass.name]
            if (!queryIdIn(list!!, layoutId)) {
                list.add(layoutId)
                modelToId.put(modelClass.name, list)
            }
        }
        if (!idToHolder.containsKey(layoutId)) {
            idToHolder.put(layoutId, holderClass)
        } else {
            Log.e(TAG, "*******************layoutId had bind Holder******************* \n" + holderClass.name)
        }
        return this as T
    }

    /**
     * 绑定空数据显示
     *
     * @param noDataModel 数据实体
     * @param layoutId    布局id
     * @param holderClass holder类名
     */
    fun bindEmpty(noDataModel: Any, layoutId: Int, holderClass: Class<out BindRecyclerBaseHolder>): T {
        noDataHolder = holderClass
        noDataObject = noDataModel
        noDataLayoutId = layoutId
        return this as T
    }

    /**
     * 一个model对应多种Holder的数据的筛选回调
     * 不设置默认使用最后一个
     */
    fun bingChooseListener(listener: OnBindDataChooseListener): T {
        normalBindDataChooseListener = listener
        return this as T
    }

    /**
     * 设置点击
     */
    fun setOnItemClickListener(listener: OnItemClickListener): T {
        this.itemClickListener = listener
        return this as T
    }

    /**
     * 设置长按
     */
    fun setOnItemLongClickListener(listener: OnItemLongClickListener): T {
        this.itemLongClickListener = listener
        return this as T
    }

    /**
     * 是否支持动画
     */
    fun isNeedAnimation(): Boolean {
        return needAnimation
    }

    /**
     * 设置是否需要动画
     */
    fun setNeedAnimation(needAnimation: Boolean): T {
        this.needAnimation = needAnimation
        return this as T
    }

    /**
     * 创建正常数据的holder
     */
    fun getViewTypeHolder(context: Context, parent: ViewGroup, viewType: Int): BindRecyclerBaseHolder? {
        val idToHolder = idToHolder[viewType]
        return contructorHolder<BindRecyclerBaseHolder>(context, parent, idToHolder, viewType)
    }

    /**
     * 创建空数据的holder
     */
    fun getNoDataViewTypeHolder(context: Context, parent: ViewGroup): BindRecyclerBaseHolder? {
        return contructorHolder<BindRecyclerBaseHolder>(context, parent, noDataHolder, noDataLayoutId)
    }

    /**
     * 根据参数构造
     */
    private fun <T> contructorHolder(context: Context, parent: ViewGroup, classType: Class<out BindRecyclerBaseHolder>?, layoutId: Int): T? {
        var `object`: Constructor<*>? = null
        var constructorFirst = true

        val v = LayoutInflater.from(context).inflate(layoutId, parent, false)

        try {
            `object` = classType!!.getDeclaredConstructor(*arrayOf(Context::class.java, View::class.java))
        } catch (e: NoSuchMethodException) {
            constructorFirst = false
            //e.printStackTrace();
        }

        if (!constructorFirst) {
            try {
                `object` = classType!!.getDeclaredConstructor(*arrayOf<Class<*>>(View::class.java))
            } catch (e: NoSuchMethodException) {
                //e.printStackTrace();
            }

        }
        if (`object` == null) {
            throw RuntimeException("Holder Constructor Error For : " + classType!!.name)
        }
        try {
            `object`.isAccessible = true
            return if (constructorFirst) {
                `object`.newInstance(context, v) as T
            } else {
                `object`.newInstance(v) as T
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }


    private fun queryIdIn(list: List<Int>, id: Int): Boolean {
        for (i in list) {
            if (i == id) {
                return true
            }
        }
        return false
    }

}
