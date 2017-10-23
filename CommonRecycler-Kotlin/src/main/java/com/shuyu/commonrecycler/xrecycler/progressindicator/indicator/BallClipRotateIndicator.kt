package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/16.
 */
open class BallClipRotateIndicator : BaseIndicatorController() {

    internal var scaleFloat = 1f
    internal var degrees: Float = 0.toFloat()

    override fun draw(canvas: Canvas, paint: Paint) {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f

        val circleSpacing = 12f
        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()
        canvas.translate(x, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.rotate(degrees)
        val rectF = RectF(-x + circleSpacing, -y + circleSpacing, 0 + x - circleSpacing, 0 + y - circleSpacing)
        canvas.drawArc(rectF, -45f, 270f, false, paint)
    }

    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()
        val scaleAnim = ValueAnimator.ofFloat(1f, 0.6f, 0.5f, 1f)
        scaleAnim.duration = 750
        scaleAnim.repeatCount = -1
        scaleAnim.addUpdateListener { animation ->
            scaleFloat = animation.animatedValue as Float
            postInvalidate()
        }
        scaleAnim.start()

        val rotateAnim = ValueAnimator.ofFloat(0f, 180f, 360f)
        rotateAnim.duration = 750
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
