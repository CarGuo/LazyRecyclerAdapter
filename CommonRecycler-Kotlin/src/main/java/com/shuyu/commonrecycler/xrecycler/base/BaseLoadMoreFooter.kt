package com.shuyu.commonrecycler.xrecycler.base

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by guoshuyu on 2017/1/7.
 */

open abstract class BaseLoadMoreFooter : LinearLayout, BaseLoadMoreInterface {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    companion object {

        const val STATE_LOADING = 0

        const val STATE_COMPLETE = 1

        const val STATE_NOMORE = 2
    }
}
