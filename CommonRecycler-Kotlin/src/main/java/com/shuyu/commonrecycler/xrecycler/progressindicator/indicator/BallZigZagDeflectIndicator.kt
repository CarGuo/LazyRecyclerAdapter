package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator


import android.animation.Animator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/19.
 */
open class BallZigZagDeflectIndicator : BallZigZagIndicator() {


    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()
        val startX = (width / 6).toFloat()
        val startY = (width / 6).toFloat()
        for (i in 0..1) {
            var translateXAnim = ValueAnimator.ofFloat(startX, width - startX, startX, width - startX, startX)
            if (i == 1) {
                translateXAnim = ValueAnimator.ofFloat(width - startX, startX, width - startX, startX, width - startX)
            }
            var translateYAnim = ValueAnimator.ofFloat(startY, startY, height - startY, height - startY, startY)
            if (i == 1) {
                translateYAnim = ValueAnimator.ofFloat(height - startY, height - startY, startY, startY, height - startY)
            }

            translateXAnim.duration = 2000
            translateXAnim.interpolator = LinearInterpolator()
            translateXAnim.repeatCount = -1
            translateXAnim.addUpdateListener { animation ->
                translateX[i] = animation.animatedValue as Float
                postInvalidate()
            }
            translateXAnim.start()

            translateYAnim.duration = 2000
            translateYAnim.interpolator = LinearInterpolator()
            translateYAnim.repeatCount = -1
            translateYAnim.addUpdateListener { animation ->
                translateY[i] = animation.animatedValue as Float
                postInvalidate()
            }
            translateYAnim.start()

            animators.add(translateXAnim)
            animators.add(translateYAnim)
        }
        return animators
    }

}
