package com.shuyu.commonrecycler.xrecycler.other


/**
 * Created by jianghejie on 16/6/20.
 */

open interface ItemTouchHelperAdapter {

    fun onItemMove(fromPosition: Int, toPosition: Int)


    fun onItemDismiss(position: Int)
}
