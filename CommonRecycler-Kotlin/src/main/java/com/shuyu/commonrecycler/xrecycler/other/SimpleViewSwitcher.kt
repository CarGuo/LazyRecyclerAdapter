package com.shuyu.commonrecycler.xrecycler.other

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * Created by jianghejie on 15/11/22.
 */
open class SimpleViewSwitcher : ViewGroup {

    constructor(context: Context) : super(context) {}

    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(context, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val childCount = this.childCount
        var maxHeight = 0
        var maxWidth = 0
        for (i in 0 until childCount) {
            val child = this.getChildAt(i)
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec)
            val cw = child.measuredWidth
            // int ch = child.getMeasuredHeight();
            maxWidth = child.measuredWidth
            maxHeight = child.measuredHeight
        }
        setMeasuredDimension(maxWidth, maxHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                child.layout(0, 0, r - l, b - t)

            }
        }
    }

    fun setView(view: View) {
        if (this.childCount != 0) {
            this.removeViewAt(0)
        }
        this.addView(view, 0)
    }

}