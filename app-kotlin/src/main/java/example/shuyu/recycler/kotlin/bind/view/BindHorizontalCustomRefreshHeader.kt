package example.shuyu.recycler.kotlin.bind.view

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.shuyu.commonrecycler.BindBaseRefreshHeader
import com.shuyu.commonrecycler.xrecycler.base.BaseRefreshHeader
import example.shuyu.recycler.kotlin.R

/**
 * 继承BindCustomRefreshHeader实现的下拉刷新
 *
 *
 * 如果需要自定义程度更高的，即可继承BaseRefreshHeader
 *
 *
 * Created by guoshuyu on 2017/1/8.
 */

open class BindHorizontalCustomRefreshHeader : BindBaseRefreshHeader {


    private var mCustomRefreshImg: ImageView? = null

    private var mCustomRefreshTxt: TextView? = null

    private var mAnimationDrawable: AnimationDrawable? = null

    /**
     * 继承，返回高度
     */
    override var currentMeasuredHeight: Int = 0


    /**
     * 继承后配置为横向
     */
    /**
     * 继承后配置为横向
     */
    override var visibleHeight: Int
        get() {
            val lp = mContainer?.layoutParams as LinearLayout.LayoutParams
            return lp.width
        }
        set(height) {
            var height = height
            if (height < 0) height = 0
            val lp = mContainer?.layoutParams as LinearLayout.LayoutParams
            lp.width = height
            mContainer?.layoutParams = lp
        }


    /**
     * 继承
     *
     * @return 返回布局id
     */
    override val layoutId: Int
        get() = R.layout.layout_horizontal_custom_refresh_header

    /**
     * 继承，根据状态调整显示效果
     *
     * @param state
     */
    override// 显示进度
            // 显示进度
    var state: Int
        get() = super.state
        set(state) {
            if (state == super.state) return

            if (state == BaseRefreshHeader.STATE_REFRESHING) {
                mAnimationDrawable!!.start()
            } else if (state == BaseRefreshHeader.STATE_DONE) {
                mAnimationDrawable!!.stop()
            } else {
                mAnimationDrawable!!.stop()
            }

            when (state) {
                BaseRefreshHeader.STATE_NORMAL -> {
                    if (super.state == BaseRefreshHeader.STATE_RELEASE_TO_REFRESH) {
                    }
                    if (super.state == BaseRefreshHeader.STATE_REFRESHING) {
                    }
                    mCustomRefreshTxt!!.text = "看到了我吧！"
                }
                BaseRefreshHeader.STATE_RELEASE_TO_REFRESH -> if (super.state != BaseRefreshHeader.STATE_RELEASE_TO_REFRESH) {
                    mCustomRefreshTxt!!.text = "放开我刷新！"
                }
                BaseRefreshHeader.STATE_REFRESHING -> mCustomRefreshTxt!!.text = "刷新中！"
                BaseRefreshHeader.STATE_DONE -> mCustomRefreshTxt!!.text = "刷新好了哟！"
            }
            super.state = state
        }

    constructor(context: Context) : super(context) {}


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    /**
     * 继承，将view添加到控件中
     *
     * @param container
     */
    override fun addView(container: ViewGroup) {
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
        lp.setMargins(0, 0, 0, 0)
        this.layoutParams = lp
        this.setPadding(0, 0, 0, 0)

        addView(container, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT))
        setGravity(Gravity.LEFT)

        mCustomRefreshImg = container.findViewById(R.id.custom_refresh_img) as ImageView
        mCustomRefreshTxt = container.findViewById(R.id.custom_refresh_txt) as TextView


        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        currentMeasuredHeight = measuredWidth

        mAnimationDrawable = mCustomRefreshImg!!.drawable as AnimationDrawable
    }
}
