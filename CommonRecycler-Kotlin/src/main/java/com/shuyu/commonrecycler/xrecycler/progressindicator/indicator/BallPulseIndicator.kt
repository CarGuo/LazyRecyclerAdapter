package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/16.
 */
open class BallPulseIndicator : BaseIndicatorController() {

    //scale x ,y
    private val scaleFloats = floatArrayOf(SCALE, SCALE, SCALE)


    override fun draw(canvas: Canvas, paint: Paint) {
        val circleSpacing = 4f
        val radius = (Math.min(width, height) - circleSpacing * 2) / 6
        val x = width / 2 - (radius * 2 + circleSpacing)
        val y = (height / 2).toFloat()
        for (i in 0..2) {
            canvas.save()
            val translateX = x + radius * 2 * i + circleSpacing * i
            canvas.translate(translateX, y)
            canvas.scale(scaleFloats[i], scaleFloats[i])
            canvas.drawCircle(0f, 0f, radius, paint)
            canvas.restore()
        }
    }

    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()
        val delays = intArrayOf(120, 240, 360)
        for (i in 0..2) {

            val scaleAnim = ValueAnimator.ofFloat(1f, 0.3f, 1f)

            scaleAnim.duration = 750
            scaleAnim.repeatCount = -1
            scaleAnim.startDelay = delays[i].toLong()

            scaleAnim.addUpdateListener { animation ->
                scaleFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            scaleAnim.start()
            animators.add(scaleAnim)
        }
        return animators
    }

    companion object {

        val SCALE = 1.0f
    }

}
