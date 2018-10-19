package example.shuyu.recycler.kotlin.bind.holder

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.ImageView

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.bind.model.BindImageModel
import example.shuyu.recycler.kotlin.bind.model.BindMutliModel
import com.shuyu.commonrecycler.BindRecyclerBaseHolder

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 * Created by guoshuyu on 2017/8/29.
 */

open class BindImageHolder (context: Context, v: View) : BindRecyclerBaseHolder(context, v) {

    private var itemImage: ImageView? = null


    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    override fun createView(v: View) {
        itemImage = v.findViewById(R.id.item_image)
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    override fun onBind(model: Any, position: Int) {
        if (model is BindMutliModel) {
            itemImage!!.setImageResource(model.res2)
        } else if (model is BindImageModel) {
            itemImage!!.setImageResource(model.resId)
        }
    }

    /**
     * 选择继承，默认返回null，实现后可返回item动画
     */
    override fun getAnimator(view: View): AnimatorSet? {
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(view, "translationY", dip2px(context, 80f).toFloat(), 0f)
        animator.duration = 500
        animator.interpolator = OvershootInterpolator(.5f)

        val animatorSx = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f)
        animator.duration = 500
        animator.interpolator = OvershootInterpolator(.5f)

        val animatorSy = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f)
        animator.duration = 500
        animator.interpolator = OvershootInterpolator(.5f)

        animatorSet.playTogether(animator, animatorSx, animatorSy)
        return animatorSet
    }

    companion object {

        val ID = R.layout.image_item

        /**
         * dip转为PX
         */
        fun dip2px(context: Context?, dipValue: Float): Int {
            val fontScale = context!!.resources.displayMetrics.density
            return (dipValue * fontScale + 0.5f).toInt()
        }
    }
}
