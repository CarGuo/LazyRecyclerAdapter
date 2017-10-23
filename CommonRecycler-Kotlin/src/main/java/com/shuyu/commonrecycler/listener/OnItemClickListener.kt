package com.shuyu.commonrecycler.listener

import android.content.Context

open interface OnItemClickListener {
    fun onItemClick(context: Context, position: Int)
}