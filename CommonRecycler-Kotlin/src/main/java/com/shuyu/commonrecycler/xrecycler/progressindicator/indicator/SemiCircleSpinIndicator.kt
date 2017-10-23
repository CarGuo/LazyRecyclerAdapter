package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/20.
 */
open class SemiCircleSpinIndicator : BaseIndicatorController() {


    override fun draw(canvas: Canvas, paint: Paint) {
        val rectF = RectF(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawArc(rectF, -60f, 120f, false, paint)
    }

    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()
        val rotateAnim = ObjectAnimator.ofFloat(target, "rotation", 0f, 180f, 360f)
        rotateAnim.duration = 600
        rotateAnim.repeatCount = -1
        rotateAnim.start()
        animators.add(rotateAnim)
        return animators
    }


}
