package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.animation.Animator
import android.animation.ValueAnimator

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/19.
 */
open class LineScalePulseOutRapidIndicator : LineScaleIndicator() {

    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()
        val delays = longArrayOf(400, 200, 0, 200, 400)
        for (i in 0..4) {
            val scaleAnim = ValueAnimator.ofFloat(1f, 0.4f, 1f)
            scaleAnim.duration = 1000
            scaleAnim.repeatCount = -1
            scaleAnim.startDelay = delays[i]
            scaleAnim.addUpdateListener { animation ->
                scaleYFloats[i] = animation.animatedValue as Float
                postInvalidate()
            }
            scaleAnim.start()
            animators.add(scaleAnim)
        }
        return animators
    }

}
