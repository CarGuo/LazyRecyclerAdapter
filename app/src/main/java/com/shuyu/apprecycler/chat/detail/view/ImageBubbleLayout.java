package com.shuyu.apprecycler.chat.detail.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;

/**
 * 转为图片转换的布局
 */
public class ImageBubbleLayout extends BubbleLayout {

    private RectF mRect;
    private Path mPath = new Path();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mStrokePath;
    private Paint mStrokePaint;

    public ImageBubbleLayout(Context context) {
        this(context, null, 0);
    }

    public ImageBubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(getBubbleColor());
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        switch (getArrowDirection()) {
            case LEFT:
                paddingLeft -= getArrowWidth();
                break;
            case RIGHT:
                paddingRight -= getArrowWidth();
                break;
            case TOP:
                paddingTop -= getArrowHeight();
                break;
            case BOTTOM:
                paddingBottom -= getArrowHeight();
                break;
        }
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        initDrawable(0, getWidth(), 0, getHeight());
    }


    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        drawPath(canvas);
        return super.drawChild(canvas, child, drawingTime);
    }

    private void drawPath(Canvas canvas) {
        if (getStrokeWidth() > 0) {
            canvas.drawPath(mStrokePath, mStrokePaint);
        }
        canvas.clipPath(mPath, Region.Op.INTERSECT);
    }


    private void initDrawable(int left, int right, int top, int bottom) {
        if (right < left || bottom < top) return;
        mRect = new RectF(left, top, right, bottom);
        if (getStrokeWidth() > 0) {
            mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mStrokePaint.setColor(getStrokeColor());
            mStrokePath = new Path();

            initPath(getArrowDirection(), mPath, getStrokeWidth());
            initPath(getArrowDirection(), mStrokePath, 0);
        } else {
            initPath(getArrowDirection(), mPath, 0);
        }
    }


    private void initPath(ArrowDirection arrowDirection, Path path, float strokeWidth) {
        switch (arrowDirection) {
            case LEFT:
                if (getCornersRadius() <= 0) {
                    initLeftSquarePath(mRect, path, strokeWidth);
                    break;
                }

                if (strokeWidth > 0 && strokeWidth > getCornersRadius()) {
                    initLeftSquarePath(mRect, path, strokeWidth);
                    break;
                }

                initLeftRoundedPath(mRect, path, strokeWidth);
                break;
            case TOP:
                if (getCornersRadius() <= 0) {
                    initTopSquarePath(mRect, path, strokeWidth);
                    break;
                }

                if (strokeWidth > 0 && strokeWidth > getCornersRadius()) {
                    initTopSquarePath(mRect, path, strokeWidth);
                    break;
                }

                initTopRoundedPath(mRect, path, strokeWidth);

                break;
            case RIGHT:
                if (getCornersRadius() <= 0) {
                    initRightSquarePath(mRect, path, strokeWidth);
                    break;
                }

                if (strokeWidth > 0 && strokeWidth > getCornersRadius()) {
                    initRightSquarePath(mRect, path, strokeWidth);
                    break;
                }

                initRightRoundedPath(mRect, path, strokeWidth);

                break;
            case BOTTOM:
                if (getCornersRadius() <= 0) {
                    initBottomSquarePath(mRect, path, strokeWidth);
                    break;
                }

                if (strokeWidth > 0 && strokeWidth > getCornersRadius()) {
                    initBottomSquarePath(mRect, path, strokeWidth);
                    break;
                }

                initBottomRoundedPath(mRect, path, strokeWidth);
                break;
        }
    }

    private void initLeftRoundedPath(RectF rect, Path path, float strokeWidth) {

        path.moveTo(getArrowWidth() + rect.left + getCornersRadius() + strokeWidth, rect.top + strokeWidth);
        path.lineTo(rect.width() - getCornersRadius() - strokeWidth, rect.top + strokeWidth);
        path.arcTo(new RectF(rect.right - getCornersRadius(), rect.top + strokeWidth, rect.right - strokeWidth,
                getCornersRadius() + rect.top), 270, 90);

        path.lineTo(rect.right - strokeWidth, rect.bottom - getCornersRadius() - strokeWidth);
        path.arcTo(new RectF(rect.right - getCornersRadius(), rect.bottom - getCornersRadius(),
                rect.right - strokeWidth, rect.bottom - strokeWidth), 0, 90);


        path.lineTo(rect.left + getArrowWidth() + getCornersRadius() + strokeWidth, rect.bottom - strokeWidth);


        path.arcTo(new RectF(rect.left + getArrowWidth() + strokeWidth, rect.bottom - getCornersRadius(),
                getCornersRadius() + rect.left + getArrowWidth(), rect.bottom - strokeWidth), 90, 90);

        path.lineTo(rect.left + getArrowWidth() + strokeWidth, getArrowHeight() + getArrowPosition() - (strokeWidth / 2));

        path.lineTo(rect.left + strokeWidth + strokeWidth, getArrowPosition() + getArrowHeight() / 2);


        path.lineTo(rect.left + getArrowWidth() + strokeWidth, getArrowPosition() + (strokeWidth / 2));

        path.lineTo(rect.left + getArrowWidth() + strokeWidth, rect.top + getCornersRadius() + strokeWidth);

        path.arcTo(new RectF(rect.left + getArrowWidth() + strokeWidth, rect.top + strokeWidth, getCornersRadius()
                + rect.left + getArrowWidth(), getCornersRadius() + rect.top), 180, 90);

        path.close();
    }

    private void initLeftSquarePath(RectF rect, Path path, float strokeWidth) {

        path.moveTo(getArrowWidth() + rect.left + strokeWidth, rect.top + strokeWidth);
        path.lineTo(rect.width() - strokeWidth, rect.top + strokeWidth);

        path.lineTo(rect.right - strokeWidth, rect.bottom - strokeWidth);

        path.lineTo(rect.left + getArrowWidth() + strokeWidth, rect.bottom - strokeWidth);


        path.lineTo(rect.left + getArrowWidth() + strokeWidth, getArrowHeight() + getArrowPosition() - (strokeWidth / 2));
        path.lineTo(rect.left + strokeWidth + strokeWidth, getArrowPosition() + getArrowHeight() / 2);
        path.lineTo(rect.left + getArrowWidth() + strokeWidth, getArrowPosition() + (strokeWidth / 2));

        path.lineTo(rect.left + getArrowWidth() + strokeWidth, rect.top + strokeWidth);


        path.close();
    }


    private void initTopRoundedPath(RectF rect, Path path, float strokeWidth) {
        path.moveTo(rect.left + Math.min(getArrowPosition(), getCornersRadius()) + strokeWidth, rect.top + getArrowHeight() + strokeWidth);
        path.lineTo(rect.left + getArrowPosition() + (strokeWidth / 2), rect.top + getArrowHeight() + strokeWidth);
        path.lineTo(rect.left + getArrowWidth() / 2 + getArrowPosition(), rect.top + strokeWidth + strokeWidth);
        path.lineTo(rect.left + getArrowWidth() + getArrowPosition() - (strokeWidth / 2), rect.top + getArrowHeight() + strokeWidth);
        path.lineTo(rect.right - getCornersRadius() - strokeWidth, rect.top + getArrowHeight() + strokeWidth);

        path.arcTo(new RectF(rect.right - getCornersRadius(),
                rect.top + getArrowHeight() + strokeWidth, rect.right - strokeWidth, getCornersRadius() + rect.top + getArrowHeight()), 270, 90);
        path.lineTo(rect.right - strokeWidth, rect.bottom - getCornersRadius() - strokeWidth);

        path.arcTo(new RectF(rect.right - getCornersRadius(), rect.bottom - getCornersRadius(),
                rect.right - strokeWidth, rect.bottom - strokeWidth), 0, 90);
        path.lineTo(rect.left + getCornersRadius() + strokeWidth, rect.bottom - strokeWidth);

        path.arcTo(new RectF(rect.left + strokeWidth, rect.bottom - getCornersRadius(),
                getCornersRadius() + rect.left, rect.bottom - strokeWidth), 90, 90);

        path.lineTo(rect.left + strokeWidth, rect.top + getArrowHeight() + getCornersRadius() + strokeWidth);

        path.arcTo(new RectF(rect.left + strokeWidth, rect.top + getArrowHeight() + strokeWidth, getCornersRadius()
                + rect.left, getCornersRadius() + rect.top + getArrowHeight()), 180, 90);

        path.close();
    }

    private void initTopSquarePath(RectF rect, Path path, float strokeWidth) {
        path.moveTo(rect.left + getArrowPosition() + strokeWidth, rect.top + getArrowHeight() + strokeWidth);

        path.lineTo(rect.left + getArrowPosition() + (strokeWidth / 2), rect.top + getArrowHeight() + strokeWidth);
        path.lineTo(rect.left + getArrowWidth() / 2 + getArrowPosition(), rect.top + strokeWidth + strokeWidth);
        path.lineTo(rect.left + getArrowWidth() + getArrowPosition() - (strokeWidth / 2), rect.top + getArrowHeight() + strokeWidth);
        path.lineTo(rect.right - strokeWidth, rect.top + getArrowHeight() + strokeWidth);

        path.lineTo(rect.right - strokeWidth, rect.bottom - strokeWidth);

        path.lineTo(rect.left + strokeWidth, rect.bottom - strokeWidth);


        path.lineTo(rect.left + strokeWidth, rect.top + getArrowHeight() + strokeWidth);

        path.lineTo(rect.left + getArrowPosition() + strokeWidth, rect.top + getArrowHeight() + strokeWidth);


        path.close();
    }


    private void initRightRoundedPath(RectF rect, Path path, float strokeWidth) {

        path.moveTo(rect.left + getCornersRadius() + strokeWidth, rect.top + strokeWidth);
        path.lineTo(rect.width() - getCornersRadius() - getArrowWidth() - strokeWidth, rect.top + strokeWidth);
        path.arcTo(new RectF(rect.right - getCornersRadius() - getArrowWidth(),
                rect.top + strokeWidth, rect.right - getArrowWidth() - strokeWidth, getCornersRadius() + rect.top), 270, 90);

        path.lineTo(rect.right - getArrowWidth() - strokeWidth, getArrowPosition() + (strokeWidth / 2));
        path.lineTo(rect.right - strokeWidth - strokeWidth, getArrowPosition() + getArrowHeight() / 2);
        path.lineTo(rect.right - getArrowWidth() - strokeWidth, getArrowPosition() + getArrowHeight() - (strokeWidth / 2));
        path.lineTo(rect.right - getArrowWidth() - strokeWidth, rect.bottom - getCornersRadius() - strokeWidth);

        path.arcTo(new RectF(rect.right - getCornersRadius() - getArrowWidth(), rect.bottom - getCornersRadius(),
                rect.right - getArrowWidth() - strokeWidth, rect.bottom - strokeWidth), 0, 90);
        path.lineTo(rect.left + getArrowWidth() + strokeWidth, rect.bottom - strokeWidth);

        path.arcTo(new RectF(rect.left + strokeWidth, rect.bottom - getCornersRadius(),
                getCornersRadius() + rect.left, rect.bottom - strokeWidth), 90, 90);

        path.arcTo(new RectF(rect.left + strokeWidth, rect.top + strokeWidth, getCornersRadius()
                + rect.left, getCornersRadius() + rect.top), 180, 90);
        path.close();
    }

    private void initRightSquarePath(RectF rect, Path path, float strokeWidth) {

        path.moveTo(rect.left + strokeWidth, rect.top + strokeWidth);
        path.lineTo(rect.width() - getArrowWidth() - strokeWidth, rect.top + strokeWidth);

        path.lineTo(rect.right - getArrowWidth() - strokeWidth, getArrowPosition() + (strokeWidth / 2));
        path.lineTo(rect.right - strokeWidth - strokeWidth, getArrowPosition() + getArrowHeight() / 2);
        path.lineTo(rect.right - getArrowWidth() - strokeWidth, getArrowPosition() + getArrowHeight() - (strokeWidth / 2));

        path.lineTo(rect.right - getArrowWidth() - strokeWidth, rect.bottom - strokeWidth);

        path.lineTo(rect.left + strokeWidth, rect.bottom - strokeWidth);
        path.lineTo(rect.left + strokeWidth, rect.top + strokeWidth);

        path.close();
    }


    private void initBottomRoundedPath(RectF rect, Path path, float strokeWidth) {

        path.moveTo(rect.left + getCornersRadius() + strokeWidth, rect.top + strokeWidth);
        path.lineTo(rect.width() - getCornersRadius() - strokeWidth, rect.top + strokeWidth);
        path.arcTo(new RectF(rect.right - getCornersRadius(),
                rect.top + strokeWidth, rect.right - strokeWidth, getCornersRadius() + rect.top), 270, 90);

        path.lineTo(rect.right - strokeWidth, rect.bottom - getArrowHeight() - getCornersRadius() - strokeWidth);
        path.arcTo(new RectF(rect.right - getCornersRadius(), rect.bottom - getCornersRadius() - getArrowHeight(),
                rect.right - strokeWidth, rect.bottom - getArrowHeight() - strokeWidth), 0, 90);

        path.lineTo(rect.left + getArrowWidth() + getArrowPosition() - (strokeWidth / 2), rect.bottom - getArrowHeight() - strokeWidth);
        path.lineTo(rect.left + getArrowPosition() + getArrowWidth() / 2, rect.bottom - strokeWidth - strokeWidth);
        path.lineTo(rect.left + getArrowPosition() + (strokeWidth / 2), rect.bottom - getArrowHeight() - strokeWidth);
        path.lineTo(rect.left + Math.min(getCornersRadius(), getArrowPosition()) + strokeWidth, rect.bottom - getArrowHeight() - strokeWidth);

        path.arcTo(new RectF(rect.left + strokeWidth, rect.bottom - getCornersRadius() - getArrowHeight(),
                getCornersRadius() + rect.left, rect.bottom - getArrowHeight() - strokeWidth), 90, 90);
        path.lineTo(rect.left + strokeWidth, rect.top + getCornersRadius() + strokeWidth);
        path.arcTo(new RectF(rect.left + strokeWidth, rect.top + strokeWidth, getCornersRadius()
                + rect.left, getCornersRadius() + rect.top), 180, 90);
        path.close();
    }

    private void initBottomSquarePath(RectF rect, Path path, float strokeWidth) {

        path.moveTo(rect.left + strokeWidth, rect.top + strokeWidth);
        path.lineTo(rect.right - strokeWidth, rect.top + strokeWidth);
        path.lineTo(rect.right - strokeWidth, rect.bottom - getArrowHeight() - strokeWidth);


        path.lineTo(rect.left + getArrowWidth() + getArrowPosition() - (strokeWidth / 2), rect.bottom - getArrowHeight() - strokeWidth);
        path.lineTo(rect.left + getArrowPosition() + getArrowWidth() / 2, rect.bottom - strokeWidth - strokeWidth);
        path.lineTo(rect.left + getArrowPosition() + (strokeWidth / 2), rect.bottom - getArrowHeight() - strokeWidth);
        path.lineTo(rect.left + getArrowPosition() + strokeWidth, rect.bottom - getArrowHeight() - strokeWidth);


        path.lineTo(rect.left + strokeWidth, rect.bottom - getArrowHeight() - strokeWidth);
        path.lineTo(rect.left + strokeWidth, rect.top + strokeWidth);
        path.close();
    }

}
