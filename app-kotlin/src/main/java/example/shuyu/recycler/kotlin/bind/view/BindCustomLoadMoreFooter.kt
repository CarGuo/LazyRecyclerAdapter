package example.shuyu.recycler.kotlin.bind.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.shuyu.commonrecycler.xrecycler.base.BaseLoadMoreFooter

import example.shuyu.recycler.kotlin.R


/**
 * 继承BaseLoadMoreFooter的LoadMore控件
 * Created by guoshuyu on 2017/1/8.
 */

open class BindCustomLoadMoreFooter : BaseLoadMoreFooter {

    private var mImageView: ImageView? = null

    private var mText: TextView? = null


    private var mAnimationDrawable: AnimationDrawable? = null


    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }


    /**
     * 继承，必须要时需要实现样式
     * @param style
     */
    override fun setProgressStyle(style: Int) {}

    /**
     * 继承，根据状态调整显示效果
     * @param state
     */
    override fun setState(state: Int) {
        when (state) {
            BaseLoadMoreFooter.STATE_LOADING -> {
                mImageView!!.visibility = View.VISIBLE
                mText!!.text = context.getText(R.string.listview_loading)
                this.visibility = View.VISIBLE
                mAnimationDrawable!!.start()
            }
            BaseLoadMoreFooter.STATE_COMPLETE -> {
                mText!!.text = context.getText(R.string.listview_loading)
                this.visibility = View.GONE
                mAnimationDrawable!!.stop()
            }
            BaseLoadMoreFooter.STATE_NOMORE -> {
                mText!!.text = context.getText(R.string.nomore_loading)
                mImageView!!.visibility = View.GONE
                this.visibility = View.VISIBLE
                mAnimationDrawable!!.stop()
            }
        }
    }

    /**
     * 初始化view
     */
    internal fun initView() {
        setGravity(Gravity.CENTER)
        layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setPadding(0, resources.getDimension(R.dimen.textandiconmargin).toInt(), 0,
                resources.getDimension(R.dimen.textandiconmargin).toInt())


        mImageView = ImageView(context)
        mImageView!!.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        mImageView!!.setImageResource(R.drawable.progressbar)


        addView(mImageView)

        mText = TextView(context)
        mText!!.text = "正在加载..."

        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(resources.getDimension(R.dimen.textandiconmargin).toInt(), 0, 0, 0)

        mText!!.layoutParams = layoutParams
        addView(mText)

        mAnimationDrawable = mImageView!!.drawable as AnimationDrawable
    }
}