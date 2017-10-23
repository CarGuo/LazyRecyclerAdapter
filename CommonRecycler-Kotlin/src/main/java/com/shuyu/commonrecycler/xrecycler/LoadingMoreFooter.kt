package com.shuyu.commonrecycler.xrecycler

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView

import com.shuyu.commonrecycler.R
import com.shuyu.commonrecycler.xrecycler.base.BaseLoadMoreFooter
import com.shuyu.commonrecycler.xrecycler.other.ProgressStyle
import com.shuyu.commonrecycler.xrecycler.other.SimpleViewSwitcher
import com.shuyu.commonrecycler.xrecycler.progressindicator.AVLoadingIndicatorView

open class LoadingMoreFooter : BaseLoadMoreFooter {

    private var progressCon: SimpleViewSwitcher? = null

    private var mText: TextView? = null


    constructor(context: Context) : super(context) {
        initView()
    }

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    fun initView() {
        setGravity(Gravity.CENTER)
        layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setPadding(0, resources.getDimension(R.dimen.textandiconmargin).toInt(), 0,
                resources.getDimension(R.dimen.textandiconmargin).toInt())
        progressCon = SimpleViewSwitcher(context)
        progressCon?.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val progressView = AVLoadingIndicatorView(this.context)
        progressView.setIndicatorColor(-0x4a4a4b)
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader)
        progressCon?.setView(progressView)

        addView(progressCon)
        mText = TextView(context)
        mText?.text = "正在加载..."

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(resources.getDimension(R.dimen.textandiconmargin).toInt(), 0, 0, 0)

        mText?.layoutParams = layoutParams
        addView(mText)
    }

    override fun setProgressStyle(style: Int) {
        if (style == ProgressStyle.SysProgress) {
            progressCon?.setView(ProgressBar(context, null, android.R.attr.progressBarStyleSmall))
        } else {
            val progressView = AVLoadingIndicatorView(this.context)
            progressView.setIndicatorColor(-0x4a4a4b)
            progressView.setIndicatorId(style)
            progressCon?.setView(progressView)
        }
    }

    override fun setState(state: Int) {
        when (state) {
            BaseLoadMoreFooter.STATE_LOADING -> {
                progressCon?.visibility = View.VISIBLE
                mText?.text = context.getText(R.string.listview_loading)
                this.visibility = View.VISIBLE
            }
            BaseLoadMoreFooter.STATE_COMPLETE -> {
                mText?.text = context.getText(R.string.listview_loading)
                this.visibility = View.GONE
            }
            BaseLoadMoreFooter.STATE_NOMORE -> {
                mText?.text = context.getText(R.string.nomore_loading)
                progressCon?.visibility = View.GONE
                this.visibility = View.VISIBLE
            }
        }
    }
}
