package com.shuyu.commonrecycler.listener

/**
 * 多个layoutId的情况下，判断该model使用哪个id
 * Created by guoshuyu on 2017/8/29.
 */

open interface OnBindDataChooseListener {
    fun getCurrentDataLayoutId(`object`: Any, classType: Class<*>, position: Int, ids: List<Int>): Int
}
