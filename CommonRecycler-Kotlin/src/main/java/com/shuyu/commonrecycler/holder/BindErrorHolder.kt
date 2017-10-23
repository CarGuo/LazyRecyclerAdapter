package com.shuyu.commonrecycler.holder

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.TextView

import com.shuyu.commonrecycler.BindRecyclerBaseHolder
import com.shuyu.commonrecycler.R

/**
 * 数据为绑定等错误提示
 * Created by guoshuyu on 2017/8/29.
 */

open class BindErrorHolder : BindRecyclerBaseHolder {

    private var errorText: TextView? = null

    constructor(v: View) : super(v) {}

    constructor(context: Context, v: View) : super(context, v) {}

    override fun createView(v: View) {
        errorText = v.findViewById(R.id.error_text) as TextView
    }

    override fun onBind(model: Any, position: Int) {
        val text = "model：<font color=\"#000000\"><big>" + model.javaClass.name + "</big></font>    never bind"
        errorText!!.text = Html.fromHtml(text)
    }

    companion object {

        val ID = R.layout.error_item
    }
}
