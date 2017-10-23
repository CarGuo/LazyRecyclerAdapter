package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

import java.util.ArrayList

/**
 * Created by Jack on 2015/10/17.
 */
open class BallClipRotateMultipleIndicator : BaseIndicatorController() {

    internal var scaleFloat = 1f
    internal var degrees: Float = 0.toFloat()


    override fun draw(canvas: Canvas, paint: Paint) {
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE

        val circleSpacing = 12f
        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()

        canvas.save()

        canvas.translate(x, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.rotate(degrees)

        //draw two big arc
        val bStartAngles = floatArrayOf(135f, -45f)
        for (i in 0..1) {
            val rectF = RectF(-x + circleSpacing, -y + circleSpacing, x - circleSpacing, y - circleSpacing)
            canvas.drawArc(rectF, bStartAngles[i], 90f, false, paint)
        }

        canvas.restore()
        canvas.translate(x, y)
        canvas.scale(scaleFloat, scaleFloat)
        canvas.rotate(-degrees)
        //draw two small arc
        val sStartAngles = floatArrayOf(225f, 45f)
        for (i in 0..1) {
            val rectF = RectF(-x / 1.8f + circleSpacing, -y / 1.8f + circleSpacing, x / 1.8f - circleSpacing, y / 1.8f - circleSpacing)
            canvas.drawArc(rectF, sStartAngles[i], 90f, false, paint)
        }
    }

    override fun createAnimation(): List<Animator> {
        val animators = ArrayList<Animator>()
        val scaleAnim = ValueAnimator.ofFloat(1f, 0.6f, 1f)
        scaleAnim.duration = 1000
        scaleAnim.repeatCount = -1
        scaleAnim.addUpdateListener { animation ->
            scaleFloat = animation.animatedValue as Float
            postInvalidate()
        }
        scaleAnim.start()

        val rotateAnim = ValueAnimator.ofFloat(0f, 180f, 360f)
        rotateAnim.duration = 1000
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
