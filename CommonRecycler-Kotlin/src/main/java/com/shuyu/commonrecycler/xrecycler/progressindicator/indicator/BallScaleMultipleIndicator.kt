package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.view.animation.LinearInterpolator

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/19.
 */
open class BallScaleMultipleIndicator : BaseIndicatorController() {

    internal var scaleFloats = floatArrayOf(1f, 1f, 1f)
    internal var alphaInts = intArrayOf(255, 255, 255)

    override fun draw(canvas: Canvas, paint: Paint) {
        val circleSpacing = 4f
        for (i in 0..2) {
            paint.alpha = alphaInts[i]
            canvas.scale(scaleFloats[i], scaleFloats[i], (width / 2).toFloat(), (height / 2).toFloat())
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), width / 2 - circleSpacing, paint)
        }
    }

    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()
        val delays = longArrayOf(0, 200, 400)
        for (i in 0..2) {
            val scaleAnim = ValueAnimator.ofFloat(0f, 1f)
            scaleAnim.interpolator = LinearInterpolator()
            scaleAnim.duration = 1000
            scaleAnim.repeatCount = -1
            scaleAnim.addUpdateListener { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            scaleAnim.startDelay = delays[i]
            scaleAnim.start()

            val alphaAnim = ValueAnimator.ofInt(255, 0)
            alphaAnim.interpolator = LinearInterpolator()
            alphaAnim.duration = 1000
            alphaAnim.repeatCount = -1
            alphaAnim.addUpdateListener { animation ->
                alphaInts[i] = animation.animatedValue as Int
                postInvalidate()
            }
            scaleAnim.startDelay = delays[i]
            alphaAnim.start()

            animators.add(scaleAnim)
            animators.add(alphaAnim)
        }
        return animators
    }

}
