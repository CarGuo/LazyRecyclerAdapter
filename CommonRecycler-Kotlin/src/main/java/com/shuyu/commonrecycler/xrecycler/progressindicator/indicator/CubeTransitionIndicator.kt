package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.animation.LinearInterpolator

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/18.
 */
open class CubeTransitionIndicator : BaseIndicatorController() {

    internal var translateX = FloatArray(2)
    internal var translateY = FloatArray(2)
    internal var degrees: Float = 0.toFloat()
    internal var scaleFloat = 1.0f

    override fun draw(canvas: Canvas, paint: Paint) {
        val rWidth = (width / 5).toFloat()
        val rHeight = (height / 5).toFloat()
        for (i in 0..1) {
            canvas.save()
            canvas.translate(translateX[i], translateY[i])
            canvas.rotate(degrees)
            canvas.scale(scaleFloat, scaleFloat)
            val rectF = RectF(-rWidth / 2, -rHeight / 2, rWidth / 2, rHeight / 2)
            canvas.drawRect(rectF, paint)
            canvas.restore()
        }
    }

    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()
        val startX = (width / 5).toFloat()
        val startY = (height / 5).toFloat()
        for (i in 0..1) {
            translateX[i] = startX
            var translationXAnim = ValueAnimator.ofFloat(startX, width - startX, width - startX, startX, startX)
            if (i == 1) {
                translationXAnim = ValueAnimator.ofFloat(width - startX, startX, startX, width - startX, width - startX)
            }
            translationXAnim.interpolator = LinearInterpolator()
            translationXAnim.duration = 1600
            translationXAnim.repeatCount = -1
            translationXAnim.addUpdateListener { animation ->
                translateX[i] = animation.animatedValue as Float
                postInvalidate()
            }
            translationXAnim.start()
            translateY[i] = startY
            var translationYAnim = ValueAnimator.ofFloat(startY, startY, height - startY, height - startY, startY)
            if (i == 1) {
                translationYAnim = ValueAnimator.ofFloat(height - startY, height - startY, startY, startY, height - startY)
            }
            translationYAnim.duration = 1600
            translationYAnim.interpolator = LinearInterpolator()
            translationYAnim.repeatCount = -1
            translationYAnim.addUpdateListener { animation ->
                translateY[i] = animation.animatedValue as Float
                postInvalidate()
            }
            translationYAnim.start()

            animators.add(translationXAnim)
            animators.add(translationYAnim)
        }

        val scaleAnim = ValueAnimator.ofFloat(1f, 0.5f, 1f, 0.5f, 1f)
        scaleAnim.duration = 1600
        scaleAnim.interpolator = LinearInterpolator()
        scaleAnim.repeatCount = -1
        scaleAnim.addUpdateListener { animation ->
            scaleFloat = animation.animatedValue as Float
            postInvalidate()
        }
        scaleAnim.start()

        val rotateAnim = ValueAnimator.ofFloat(0f, 180f, 360f, 1.5f * 360, 2 * 360f)
        rotateAnim.duration = 1600
        rotateAnim.interpolator = LinearInterpolator()
        rotateAnim.repeatCount = -1
        rotateAnim.addUpdateListener { animation ->
            degrees = animation.animatedValue as Float
            postInvalidate()
        }
        rotateAnim.start()

        animators.add(scaleAnim)
        animators.add(rotateAnim)
        return animators
    }
}
