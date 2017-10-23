package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/20.
 */
open class BallGridBeatIndicator : BaseIndicatorController() {

    internal var alphas = intArrayOf(ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA, ALPHA)

    override fun draw(canvas: Canvas, paint: Paint) {
        val circleSpacing = 4f
        val radius = (width - circleSpacing * 4) / 6
        val x = width / 2 - (radius * 2 + circleSpacing)
        val y = width / 2 - (radius * 2 + circleSpacing)

        for (i in 0..2) {
            for (j in 0..2) {
                canvas.save()
                val translateX = x + radius * 2 * j + circleSpacing * j
                val translateY = y + radius * 2 * i + circleSpacing * i
                canvas.translate(translateX, translateY)
                paint.alpha = alphas[3 * i + j]
                canvas.drawCircle(0f, 0f, radius, paint)
                canvas.restore()
            }
        }
    }

    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()

        val durations = intArrayOf(960, 930, 1190, 1130, 1340, 940, 1200, 820, 1190)
        val delays = intArrayOf(360, 400, 680, 410, 710, -150, -120, 10, 320)

        for (i in 0..8) {
            val alphaAnim = ValueAnimator.ofInt(255, 168, 255)
            alphaAnim.duration = durations[i].toLong()
            alphaAnim.repeatCount = -1
            alphaAnim.startDelay = delays[i].toLong()
            alphaAnim.addUpdateListener { animation ->
                alphas[i] = animation.animatedValue as Int
                postInvalidate()
            }
            alphaAnim.start()
            animators.add(alphaAnim)
        }
        return animators
    }

    companion object {

        val ALPHA = 255
    }


}
