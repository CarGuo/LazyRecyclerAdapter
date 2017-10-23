package com.shuyu.commonrecycler.xrecycler.progressindicator.indicator

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

/**
 * Created by Jack on 2015/10/24.
 * Email:81813780@qq.com
 */
open class LineSpinFadeLoaderIndicator : BallSpinFadeLoaderIndicator() {


    override fun draw(canvas: Canvas, paint: Paint) {
        val radius = (width / 10).toFloat()
        for (i in 0..7) {
            canvas.save()
            val point = circleAt(width, height, width / 2.5f - radius, i * (Math.PI / 4))
            canvas.translate(point.x, point.y)
            canvas.scale(scaleFloats[i], scaleFloats[i])
            canvas.rotate((i * 45).toFloat())
            paint.alpha = alphas[i]
            val rectF = RectF(-radius, -radius / 1.5f, 1.5f * radius, radius / 1.5f)
            canvas.drawRoundRect(rectF, 5f, 5f, paint)
            canvas.restore()
        }
    }

}
