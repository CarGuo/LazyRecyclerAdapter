package example.shuyu.recycler.kotlin.chat.detail.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import android.util.AttributeSet
import android.view.View

import com.daasuu.bl.ArrowDirection
import com.daasuu.bl.BubbleLayout

/**
 * 转为图片转换的布局
 */
open class ImageBubbleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BubbleLayout(context, attrs, defStyleAttr) {

    private var mRect: RectF? = null
    private val mPath = Path()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mStrokePath: Path? = null
    private var mStrokePaint: Paint? = null

    init {
        mPaint.color = bubbleColor
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        var paddingLeft = paddingLeft
        var paddingRight = paddingRight
        var paddingTop = paddingTop
        var paddingBottom = paddingBottom
        when (arrowDirection) {
            ArrowDirection.LEFT -> paddingLeft -= arrowWidth.toInt()
            ArrowDirection.RIGHT -> paddingRight -= arrowWidth.toInt()
            ArrowDirection.TOP -> paddingTop -= arrowHeight.toInt()
            ArrowDirection.BOTTOM -> paddingBottom -= arrowHeight.toInt()
        }
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        initDrawable(0, width, 0, height)
    }


    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        drawPath(canvas)
        return super.drawChild(canvas, child, drawingTime)
    }

    private fun drawPath(canvas: Canvas) {
        if (strokeWidth > 0) {
            canvas.drawPath(mStrokePath!!, mStrokePaint!!)
        }
        canvas.clipPath(mPath, Region.Op.INTERSECT)
    }


    private fun initDrawable(left: Int, right: Int, top: Int, bottom: Int) {
        if (right < left || bottom < top) return
        mRect = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
        if (strokeWidth > 0) {
            mStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mStrokePaint!!.color = strokeColor
            mStrokePath = Path()

            initPath(arrowDirection, mPath, strokeWidth)
            initPath(arrowDirection, mStrokePath!!, 0f)
        } else {
            initPath(arrowDirection, mPath, 0f)
        }
    }


    private fun initPath(arrowDirection: ArrowDirection, path: Path, strokeWidth: Float) {
        when (arrowDirection) {
            ArrowDirection.LEFT -> {
                if (cornersRadius <= 0) {
                    initLeftSquarePath(mRect, path, strokeWidth)
                }

                if (strokeWidth > 0 && strokeWidth > cornersRadius) {
                    initLeftSquarePath(mRect, path, strokeWidth)
                }

                initLeftRoundedPath(mRect, path, strokeWidth)
            }
            ArrowDirection.TOP -> {
                if (cornersRadius <= 0) {
                    initTopSquarePath(mRect, path, strokeWidth)
                }

                if (strokeWidth > 0 && strokeWidth > cornersRadius) {
                    initTopSquarePath(mRect, path, strokeWidth)
                }

                initTopRoundedPath(mRect, path, strokeWidth)
            }
            ArrowDirection.RIGHT -> {
                if (cornersRadius <= 0) {
                    initRightSquarePath(mRect, path, strokeWidth)
                }

                if (strokeWidth > 0 && strokeWidth > cornersRadius) {
                    initRightSquarePath(mRect, path, strokeWidth)
                }

                initRightRoundedPath(mRect, path, strokeWidth)
            }
            ArrowDirection.BOTTOM -> {
                if (cornersRadius <= 0) {
                    initBottomSquarePath(mRect, path, strokeWidth)
                }

                if (strokeWidth > 0 && strokeWidth > cornersRadius) {
                    initBottomSquarePath(mRect, path, strokeWidth)
                }

                initBottomRoundedPath(mRect, path, strokeWidth)
            }
        }
    }

    private fun initLeftRoundedPath(rect: RectF?, path: Path, strokeWidth: Float) {

        path.moveTo(arrowWidth + rect!!.left + cornersRadius + strokeWidth, rect.top + strokeWidth)
        path.lineTo(rect.width() - cornersRadius - strokeWidth, rect.top + strokeWidth)
        path.arcTo(RectF(rect.right - cornersRadius, rect.top + strokeWidth, rect.right - strokeWidth,
                cornersRadius + rect.top), 270f, 90f)

        path.lineTo(rect.right - strokeWidth, rect.bottom - cornersRadius - strokeWidth)
        path.arcTo(RectF(rect.right - cornersRadius, rect.bottom - cornersRadius,
                rect.right - strokeWidth, rect.bottom - strokeWidth), 0f, 90f)


        path.lineTo(rect.left + arrowWidth + cornersRadius + strokeWidth, rect.bottom - strokeWidth)


        path.arcTo(RectF(rect.left + arrowWidth + strokeWidth, rect.bottom - cornersRadius,
                cornersRadius + rect.left + arrowWidth, rect.bottom - strokeWidth), 90f, 90f)

        path.lineTo(rect.left + arrowWidth + strokeWidth, arrowHeight + arrowPosition - strokeWidth / 2)

        path.lineTo(rect.left + strokeWidth + strokeWidth, arrowPosition + arrowHeight / 2)


        path.lineTo(rect.left + arrowWidth + strokeWidth, arrowPosition + strokeWidth / 2)

        path.lineTo(rect.left + arrowWidth + strokeWidth, rect.top + cornersRadius + strokeWidth)

        path.arcTo(RectF(rect.left + arrowWidth + strokeWidth, rect.top + strokeWidth, cornersRadius
                + rect.left + arrowWidth, cornersRadius + rect.top), 180f, 90f)

        path.close()
    }

    private fun initLeftSquarePath(rect: RectF?, path: Path, strokeWidth: Float) {

        path.moveTo(arrowWidth + rect!!.left + strokeWidth, rect.top + strokeWidth)
        path.lineTo(rect.width() - strokeWidth, rect.top + strokeWidth)

        path.lineTo(rect.right - strokeWidth, rect.bottom - strokeWidth)

        path.lineTo(rect.left + arrowWidth + strokeWidth, rect.bottom - strokeWidth)


        path.lineTo(rect.left + arrowWidth + strokeWidth, arrowHeight + arrowPosition - strokeWidth / 2)
        path.lineTo(rect.left + strokeWidth + strokeWidth, arrowPosition + arrowHeight / 2)
        path.lineTo(rect.left + arrowWidth + strokeWidth, arrowPosition + strokeWidth / 2)

        path.lineTo(rect.left + arrowWidth + strokeWidth, rect.top + strokeWidth)


        path.close()
    }


    private fun initTopRoundedPath(rect: RectF?, path: Path, strokeWidth: Float) {
        path.moveTo(rect!!.left + Math.min(arrowPosition, cornersRadius) + strokeWidth, rect.top + arrowHeight + strokeWidth)
        path.lineTo(rect.left + arrowPosition + strokeWidth / 2, rect.top + arrowHeight + strokeWidth)
        path.lineTo(rect.left + arrowWidth / 2 + arrowPosition, rect.top + strokeWidth + strokeWidth)
        path.lineTo(rect.left + arrowWidth + arrowPosition - strokeWidth / 2, rect.top + arrowHeight + strokeWidth)
        path.lineTo(rect.right - cornersRadius - strokeWidth, rect.top + arrowHeight + strokeWidth)

        path.arcTo(RectF(rect.right - cornersRadius,
                rect.top + arrowHeight + strokeWidth, rect.right - strokeWidth, cornersRadius + rect.top + arrowHeight), 270f, 90f)
        path.lineTo(rect.right - strokeWidth, rect.bottom - cornersRadius - strokeWidth)

        path.arcTo(RectF(rect.right - cornersRadius, rect.bottom - cornersRadius,
                rect.right - strokeWidth, rect.bottom - strokeWidth), 0f, 90f)
        path.lineTo(rect.left + cornersRadius + strokeWidth, rect.bottom - strokeWidth)

        path.arcTo(RectF(rect.left + strokeWidth, rect.bottom - cornersRadius,
                cornersRadius + rect.left, rect.bottom - strokeWidth), 90f, 90f)

        path.lineTo(rect.left + strokeWidth, rect.top + arrowHeight + cornersRadius + strokeWidth)

        path.arcTo(RectF(rect.left + strokeWidth, rect.top + arrowHeight + strokeWidth, cornersRadius + rect.left, cornersRadius + rect.top + arrowHeight), 180f, 90f)

        path.close()
    }

    private fun initTopSquarePath(rect: RectF?, path: Path, strokeWidth: Float) {
        path.moveTo(rect!!.left + arrowPosition + strokeWidth, rect.top + arrowHeight + strokeWidth)

        path.lineTo(rect.left + arrowPosition + strokeWidth / 2, rect.top + arrowHeight + strokeWidth)
        path.lineTo(rect.left + arrowWidth / 2 + arrowPosition, rect.top + strokeWidth + strokeWidth)
        path.lineTo(rect.left + arrowWidth + arrowPosition - strokeWidth / 2, rect.top + arrowHeight + strokeWidth)
        path.lineTo(rect.right - strokeWidth, rect.top + arrowHeight + strokeWidth)

        path.lineTo(rect.right - strokeWidth, rect.bottom - strokeWidth)

        path.lineTo(rect.left + strokeWidth, rect.bottom - strokeWidth)


        path.lineTo(rect.left + strokeWidth, rect.top + arrowHeight + strokeWidth)

        path.lineTo(rect.left + arrowPosition + strokeWidth, rect.top + arrowHeight + strokeWidth)


        path.close()
    }


    private fun initRightRoundedPath(rect: RectF?, path: Path, strokeWidth: Float) {

        path.moveTo(rect!!.left + cornersRadius + strokeWidth, rect.top + strokeWidth)
        path.lineTo(rect.width() - cornersRadius - arrowWidth - strokeWidth, rect.top + strokeWidth)
        path.arcTo(RectF(rect.right - cornersRadius - arrowWidth,
                rect.top + strokeWidth, rect.right - arrowWidth - strokeWidth, cornersRadius + rect.top), 270f, 90f)

        path.lineTo(rect.right - arrowWidth - strokeWidth, arrowPosition + strokeWidth / 2)
        path.lineTo(rect.right - strokeWidth - strokeWidth, arrowPosition + arrowHeight / 2)
        path.lineTo(rect.right - arrowWidth - strokeWidth, arrowPosition + arrowHeight - strokeWidth / 2)
        path.lineTo(rect.right - arrowWidth - strokeWidth, rect.bottom - cornersRadius - strokeWidth)

        path.arcTo(RectF(rect.right - cornersRadius - arrowWidth, rect.bottom - cornersRadius,
                rect.right - arrowWidth - strokeWidth, rect.bottom - strokeWidth), 0f, 90f)
        path.lineTo(rect.left + arrowWidth + strokeWidth, rect.bottom - strokeWidth)

        path.arcTo(RectF(rect.left + strokeWidth, rect.bottom - cornersRadius,
                cornersRadius + rect.left, rect.bottom - strokeWidth), 90f, 90f)

        path.arcTo(RectF(rect.left + strokeWidth, rect.top + strokeWidth, cornersRadius + rect.left, cornersRadius + rect.top), 180f, 90f)
        path.close()
    }

    private fun initRightSquarePath(rect: RectF?, path: Path, strokeWidth: Float) {

        path.moveTo(rect!!.left + strokeWidth, rect.top + strokeWidth)
        path.lineTo(rect.width() - arrowWidth - strokeWidth, rect.top + strokeWidth)

        path.lineTo(rect.right - arrowWidth - strokeWidth, arrowPosition + strokeWidth / 2)
        path.lineTo(rect.right - strokeWidth - strokeWidth, arrowPosition + arrowHeight / 2)
        path.lineTo(rect.right - arrowWidth - strokeWidth, arrowPosition + arrowHeight - strokeWidth / 2)

        path.lineTo(rect.right - arrowWidth - strokeWidth, rect.bottom - strokeWidth)

        path.lineTo(rect.left + strokeWidth, rect.bottom - strokeWidth)
        path.lineTo(rect.left + strokeWidth, rect.top + strokeWidth)

        path.close()
    }


    private fun initBottomRoundedPath(rect: RectF?, path: Path, strokeWidth: Float) {

        path.moveTo(rect!!.left + cornersRadius + strokeWidth, rect.top + strokeWidth)
        path.lineTo(rect.width() - cornersRadius - strokeWidth, rect.top + strokeWidth)
        path.arcTo(RectF(rect.right - cornersRadius,
                rect.top + strokeWidth, rect.right - strokeWidth, cornersRadius + rect.top), 270f, 90f)

        path.lineTo(rect.right - strokeWidth, rect.bottom - arrowHeight - cornersRadius - strokeWidth)
        path.arcTo(RectF(rect.right - cornersRadius, rect.bottom - cornersRadius - arrowHeight,
                rect.right - strokeWidth, rect.bottom - arrowHeight - strokeWidth), 0f, 90f)

        path.lineTo(rect.left + arrowWidth + arrowPosition - strokeWidth / 2, rect.bottom - arrowHeight - strokeWidth)
        path.lineTo(rect.left + arrowPosition + arrowWidth / 2, rect.bottom - strokeWidth - strokeWidth)
        path.lineTo(rect.left + arrowPosition + strokeWidth / 2, rect.bottom - arrowHeight - strokeWidth)
        path.lineTo(rect.left + Math.min(cornersRadius, arrowPosition) + strokeWidth, rect.bottom - arrowHeight - strokeWidth)

        path.arcTo(RectF(rect.left + strokeWidth, rect.bottom - cornersRadius - arrowHeight,
                cornersRadius + rect.left, rect.bottom - arrowHeight - strokeWidth), 90f, 90f)
        path.lineTo(rect.left + strokeWidth, rect.top + cornersRadius + strokeWidth)
        path.arcTo(RectF(rect.left + strokeWidth, rect.top + strokeWidth, cornersRadius + rect.left, cornersRadius + rect.top), 180f, 90f)
        path.close()
    }

    private fun initBottomSquarePath(rect: RectF?, path: Path, strokeWidth: Float) {

        path.moveTo(rect!!.left + strokeWidth, rect.top + strokeWidth)
        path.lineTo(rect.right - strokeWidth, rect.top + strokeWidth)
        path.lineTo(rect.right - strokeWidth, rect.bottom - arrowHeight - strokeWidth)


        path.lineTo(rect.left + arrowWidth + arrowPosition - strokeWidth / 2, rect.bottom - arrowHeight - strokeWidth)
        path.lineTo(rect.left + arrowPosition + arrowWidth / 2, rect.bottom - strokeWidth - strokeWidth)
        path.lineTo(rect.left + arrowPosition + strokeWidth / 2, rect.bottom - arrowHeight - strokeWidth)
        path.lineTo(rect.left + arrowPosition + strokeWidth, rect.bottom - arrowHeight - strokeWidth)


        path.lineTo(rect.left + strokeWidth, rect.bottom - arrowHeight - strokeWidth)
        path.lineTo(rect.left + strokeWidth, rect.top + strokeWidth)
        path.close()
    }

}
