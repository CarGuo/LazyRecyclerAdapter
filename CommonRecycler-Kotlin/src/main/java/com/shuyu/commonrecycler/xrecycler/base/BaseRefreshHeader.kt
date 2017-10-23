package com.shuyu.commonrecycler.xrecycler.base

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * Created by jianghejie on 15/11/22.
 */
open abstract class BaseRefreshHeader : LinearLayout, BaseRefreshInterface {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    companion object {

        const val STATE_NORMAL = 0

        const val STATE_RELEASE_TO_REFRESH = 1

        const val STATE_REFRESHING = 2

        const val STATE_DONE = 3
    }
}