package com.shuyu.commonrecycler.xrecycler.progressindicator

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.support.annotation.IntDef
import android.util.AttributeSet
import android.view.View

import com.shuyu.commonrecycler.R
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallBeatIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallClipRotateIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallClipRotateMultipleIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallClipRotatePulseIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallGridBeatIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallGridPulseIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallPulseIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallPulseRiseIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallPulseSyncIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallRotateIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallScaleIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallScaleMultipleIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallScaleRippleIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallScaleRippleMultipleIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallSpinFadeLoaderIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallTrianglePathIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallZigZagDeflectIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BallZigZagIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.BaseIndicatorController
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.CubeTransitionIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.LineScaleIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.LineScalePartyIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.LineScalePulseOutIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.LineScalePulseOutRapidIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.LineSpinFadeLoaderIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.PacmanIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.SemiCircleSpinIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.SquareSpinIndicator
import com.shuyu.commonrecycler.xrecycler.progressindicator.indicator.TriangleSkewSpinIndicator


/**
 * Created by Jack on 2015/10/15
 *
 *
 * BallPulse,
 * BallGridPulse,
 * BallClipRotate,
 * BallClipRotatePulse,
 * SquareSpin,
 * BallClipRotateMultiple,
 * BallPulseRise,
 * BallRotate,
 * CubeTransition,
 * BallZigZag,
 * BallZigZagDeflect,
 * BallTrianglePath,
 * BallScale,
 * LineScale,
 * LineScaleParty,
 * BallScaleMultiple,
 * BallPulseSync,
 * BallBeat,
 * LineScalePulseOut,
 * LineScalePulseOutRapid,
 * BallScaleRipple,
 * BallScaleRippleMultiple,
 * BallSpinFadeLoader,
 * LineSpinFadeLoader,
 * TriangleSkewSpin,
 * Pacman,
 * BallGridBeat,
 * SemiCircleSpin
 */
open class AVLoadingIndicatorView : View {

    //attrs
    internal var mIndicatorId: Int = 0
    internal var mIndicatorColor: Int = 0

    lateinit var mPaint: Paint

    lateinit var mIndicatorController: BaseIndicatorController

    private var mHasAnimation: Boolean = false


    @IntDef(flag = true, value = * longArrayOf(BallPulse.toLong(), BallGridPulse.toLong(), BallClipRotate.toLong(), BallClipRotatePulse.toLong(), SquareSpin.toLong(), BallClipRotateMultiple.toLong(), BallPulseRise.toLong(), BallRotate.toLong(), CubeTransition.toLong(), BallZigZag.toLong(), BallZigZagDeflect.toLong(), BallTrianglePath.toLong(), BallScale.toLong(), LineScale.toLong(), LineScaleParty.toLong(), BallScaleMultiple.toLong(), BallPulseSync.toLong(), BallBeat.toLong(), LineScalePulseOut.toLong(), LineScalePulseOutRapid.toLong(), BallScaleRipple.toLong(), BallScaleRippleMultiple.toLong(), BallSpinFadeLoader.toLong(), LineSpinFadeLoader.toLong(), TriangleSkewSpin.toLong(), Pacman.toLong(), BallGridBeat.toLong(), SemiCircleSpin.toLong()))
    annotation class Indicator;

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AVLoadingIndicatorView)
        mIndicatorId = a.getInt(R.styleable.AVLoadingIndicatorView_indicator, BallPulse)
        mIndicatorColor = a.getColor(R.styleable.AVLoadingIndicatorView_indicator_color, Color.WHITE)
        a.recycle()
        mPaint = Paint()
        mPaint.color = mIndicatorColor
        mPaint.style = Paint.Style.FILL
        mPaint.isAntiAlias = true
        applyIndicator()
    }

    fun setIndicatorId(indicatorId: Int) {
        mIndicatorId = indicatorId
        applyIndicator()
    }

    fun setIndicatorColor(color: Int) {
        mIndicatorColor = color
        mPaint.color = mIndicatorColor
        this.invalidate()
    }

    private fun applyIndicator() {
        when (mIndicatorId) {
            BallPulse -> mIndicatorController = BallPulseIndicator()
            BallGridPulse -> mIndicatorController = BallGridPulseIndicator()
            BallClipRotate -> mIndicatorController = BallClipRotateIndicator()
            BallClipRotatePulse -> mIndicatorController = BallClipRotatePulseIndicator()
            SquareSpin -> mIndicatorController = SquareSpinIndicator()
            BallClipRotateMultiple -> mIndicatorController = BallClipRotateMultipleIndicator()
            BallPulseRise -> mIndicatorController = BallPulseRiseIndicator()
            BallRotate -> mIndicatorController = BallRotateIndicator()
            CubeTransition -> mIndicatorController = CubeTransitionIndicator()
            BallZigZag -> mIndicatorController = BallZigZagIndicator()
            BallZigZagDeflect -> mIndicatorController = BallZigZagDeflectIndicator()
            BallTrianglePath -> mIndicatorController = BallTrianglePathIndicator()
            BallScale -> mIndicatorController = BallScaleIndicator()
            LineScale -> mIndicatorController = LineScaleIndicator()
            LineScaleParty -> mIndicatorController = LineScalePartyIndicator()
            BallScaleMultiple -> mIndicatorController = BallScaleMultipleIndicator()
            BallPulseSync -> mIndicatorController = BallPulseSyncIndicator()
            BallBeat -> mIndicatorController = BallBeatIndicator()
            LineScalePulseOut -> mIndicatorController = LineScalePulseOutIndicator()
            LineScalePulseOutRapid -> mIndicatorController = LineScalePulseOutRapidIndicator()
            BallScaleRipple -> mIndicatorController = BallScaleRippleIndicator()
            BallScaleRippleMultiple -> mIndicatorController = BallScaleRippleMultipleIndicator()
            BallSpinFadeLoader -> mIndicatorController = BallSpinFadeLoaderIndicator()
            LineSpinFadeLoader -> mIndicatorController = LineSpinFadeLoaderIndicator()
            TriangleSkewSpin -> mIndicatorController = TriangleSkewSpinIndicator()
            Pacman -> mIndicatorController = PacmanIndicator()
            BallGridBeat -> mIndicatorController = BallGridBeatIndicator()
            SemiCircleSpin -> mIndicatorController = SemiCircleSpinIndicator()
        }
        mIndicatorController.target = this
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureDimension(dp2px(DEFAULT_SIZE), widthMeasureSpec)
        val height = measureDimension(dp2px(DEFAULT_SIZE), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    private fun measureDimension(defaultSize: Int, measureSpec: Int): Int {
        var result = defaultSize
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize)
        } else {
            result = defaultSize
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawIndicator(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!mHasAnimation) {
            mHasAnimation = true
            applyAnimation()
        }
    }

    override fun setVisibility(v: Int) {
        if (visibility != v) {
            super.setVisibility(v)
            if (v == View.GONE || v == View.INVISIBLE) {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.END)
            } else {
                mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.CANCEL)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mIndicatorController.setAnimationStatus(BaseIndicatorController.AnimStatus.START)
    }

    internal fun drawIndicator(canvas: Canvas) {
        mIndicatorController.draw(canvas, mPaint)
    }

    internal fun applyAnimation() {
        mIndicatorController.initAnimation()
    }

    private fun dp2px(dpValue: Int): Int {
        return context.resources.displayMetrics.density.toInt() * dpValue
    }

    companion object {
        //indicators
        const val BallPulse = 0
        const val BallGridPulse = 1
        const val BallClipRotate = 2
        const val BallClipRotatePulse = 3
        const val SquareSpin = 4
        const val BallClipRotateMultiple = 5
        const val BallPulseRise = 6
        const val BallRotate = 7
        const val CubeTransition = 8
        const val BallZigZag = 9
        const val BallZigZagDeflect = 10
        const val BallTrianglePath = 11
        const val BallScale = 12
        const val LineScale = 13
        const val LineScaleParty = 14
        const val BallScaleMultiple = 15
        const val BallPulseSync = 16
        const val BallBeat = 17
        const val LineScalePulseOut = 18
        const val LineScalePulseOutRapid = 19
        const val BallScaleRipple = 20
        const val BallScaleRippleMultiple = 21
        const val BallSpinFadeLoader = 22
        const val LineSpinFadeLoader = 23
        const val TriangleSkewSpin = 24
        const val Pacman = 25
        const val BallGridBeat = 26
        const val SemiCircleSpin = 27

        //Sizes (with defaults in DP)
        const val DEFAULT_SIZE = 30
    }


}
