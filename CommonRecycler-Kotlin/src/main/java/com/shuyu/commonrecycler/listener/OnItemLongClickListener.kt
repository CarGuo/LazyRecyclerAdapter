package com.shuyu.commonrecycler.listener

import android.content.Context

open interface OnItemLongClickListener {
    fun onItemClick(context: Context, position: Int): Boolean
}