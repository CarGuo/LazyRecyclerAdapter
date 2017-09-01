package com.shuyu.bind.decoration;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.base.BaseLoadMoreFooter;
import com.shuyu.bind.BindRecyclerAdapter;
import com.shuyu.bind.BindSuperAdapter;

/**
 * 实现分割线
 */
class BindItemDecoration extends RecyclerView.ItemDecoration {

    private BindRecyclerAdapter bindSuperAdapter;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int space = 50;

    private int spanCount;

    private int orientation;

    private int dataSize;

    private int offsetPosition;

    private int endDataPosition;

    //GRID左右是否需要边距
    private boolean needGridRightLeftEdge = true;

    //第一行是否需要边距
    private boolean needFirstTopEdge = false;

    private int color = Color.BLACK;

    BindItemDecoration(BindRecyclerAdapter bindRecyclerAdapter) {
        this.bindSuperAdapter = bindRecyclerAdapter;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() != null) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();

            //获取下拉刷新和头的偏移位置
            initOffsetPosition();
            if (dataSize == 0) {
                return;
            }
            if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
                orientation = getOrientation(layoutManager);
                spanCount = getSpanCount(layoutManager);
                if (orientation == OrientationHelper.HORIZONTAL) {
                    gridRectHorizontal(parent, view, outRect);
                } else {
                    gridRect(parent, view, outRect);
                }
            } else if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                orientation = linearLayoutManager.getOrientation();
                linearRect(layoutManager, parent, view, outRect);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (dataSize == 0) {
            return;
        }
        if (parent.getLayoutManager() != null) {
            RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
            //根据布局管理器绘制边框
            if (layoutManager instanceof LinearLayoutManager && !(layoutManager instanceof GridLayoutManager)) {
                //线性的
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                    drawLineVertical(c, parent);
                } else {
                    drawLineHorizontal(c, parent);
                }
            } else {
                if (getOrientation(layoutManager) == OrientationHelper.HORIZONTAL) {
                    drawGridHorizontal(c, parent);
                } else {
                    drawGridVertical(c, parent);
                }
            }
        }
    }

    /**
     * 初始化偏移位置，不包含刷新、头部、上拉等item
     */
    void initOffsetPosition() {
        //获取下拉刷新和头的偏移位置
        if (bindSuperAdapter instanceof BindSuperAdapter) {
            offsetPosition = ((BindSuperAdapter) bindSuperAdapter).absFirstPosition();
        }
        dataSize = (bindSuperAdapter.getDataList() != null ? bindSuperAdapter.getDataList().size() : 0);
        endDataPosition = offsetPosition + dataSize;
        if (endDataPosition < 0) {
            endDataPosition = 0;
        }
    }

    /**
     * 配置linear模式的item rect
     */
    void linearRect(RecyclerView.LayoutManager layoutManager, RecyclerView parent, View view, Rect outRect) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int currentPosition = parent.getChildAdapterPosition(view);
        //去掉header，上下拉item
        if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                if (linearLayoutManager.getReverseLayout()) {
                    outRect.left = space;
                } else {
                    outRect.right = space;
                }
            } else {
                if (linearLayoutManager.getReverseLayout()) {
                    outRect.top = space;
                } else {
                    outRect.bottom = space;
                }
            }
        }
        //第一行顶部间隔
        if (needFirstTopEdge && currentPosition == offsetPosition && currentPosition != endDataPosition) {
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (linearLayoutManager.getReverseLayout()) {
                    outRect.right = space;
                } else {
                    outRect.left = space;
                }
            } else {
                if (linearLayoutManager.getReverseLayout()) {
                    outRect.bottom = space;
                } else {
                    outRect.top = space;
                }
            }
        }
    }

    /**
     * 配置grid模式的item rect
     */
    void gridRect(RecyclerView parent, View view, Rect outRect) {
        int currentPosition = parent.getChildAdapterPosition(view);
        int spanIndex = getSpanIndex(parent, view, (RecyclerView.LayoutParams) view.getLayoutParams());
        //去掉header，上下拉item
        if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
            if (spanIndex == (spanCount - 1)) {
                if (getCurReverseLayout(parent.getLayoutManager())) {
                    outRect.top = space;
                } else {
                    outRect.bottom = space;
                }
                if (needGridRightLeftEdge) {
                    outRect.right = space;
                }
            } else if (spanIndex == 0) {
                outRect.right = space;
                if (getCurReverseLayout(parent.getLayoutManager())) {
                    outRect.top = space;
                } else {
                    outRect.bottom = space;
                }
                if (needGridRightLeftEdge) {
                    outRect.left = space;
                }
            } else {
                if (getCurReverseLayout(parent.getLayoutManager())) {
                    outRect.top = space;
                } else {
                    outRect.bottom = space;
                }
                outRect.right = space;
            }
        }

        //第一行顶部间隔
        if (needFirstTopEdge && currentPosition >= offsetPosition
                && currentPosition < (offsetPosition + spanCount)
                && currentPosition != endDataPosition) {
            if (getCurReverseLayout(parent.getLayoutManager())) {
                outRect.bottom = space;
            } else {
                outRect.top = space;
            }
        }

    }

    /**
     * 配置橫向grid模式的item rect
     */
    void gridRectHorizontal(RecyclerView parent, View view, Rect outRect) {
        int currentPosition = parent.getChildAdapterPosition(view);
        int spanIndex = getSpanIndex(parent, view, (RecyclerView.LayoutParams) view.getLayoutParams());
        //去掉header，上下拉item
        if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
            if (spanIndex == (spanCount - 1)) {
                if (getCurReverseLayout(parent.getLayoutManager())) {
                    outRect.left = space;
                } else {
                    outRect.right = space;
                }
                if (needGridRightLeftEdge) {
                    outRect.bottom = space;
                }
            } else if (spanIndex == 0) {
                outRect.bottom = space;
                if (getCurReverseLayout(parent.getLayoutManager())) {
                    outRect.left = space;
                } else {
                    outRect.right = space;
                }
                if (needGridRightLeftEdge) {
                    outRect.top = space;
                }
            } else {
                if (getCurReverseLayout(parent.getLayoutManager())) {
                    outRect.left = space;
                } else {
                    outRect.right = space;
                }
                outRect.bottom = space;
            }
        }

        //第一行顶部间隔
        if (needFirstTopEdge && currentPosition >= offsetPosition
                && currentPosition < (offsetPosition + spanCount)
                && currentPosition != endDataPosition) {
            if (getCurReverseLayout(parent.getLayoutManager())) {
                outRect.right = space;
            } else {
                outRect.left = space;
            }
        }

    }

    void drawLineVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            int currentPosition = parent.getChildAdapterPosition(child);
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                if (linearLayoutManager.getReverseLayout()) {
                    if (paint != null) {
                        final int left = child.getLeft() - space;
                        final int right = child.getLeft();
                        canvas.drawRect(left, top, right, bottom, paint);
                    }
                } else {
                    if (paint != null) {
                        final int left = child.getRight();
                        final int right = left + space;
                        canvas.drawRect(left, top, right, bottom, paint);
                    }
                }
                //第一行顶部间隔
                if (needFirstTopEdge && currentPosition == offsetPosition && currentPosition != endDataPosition) {
                    if (linearLayoutManager.getReverseLayout()) {
                        if (paint != null) {
                            canvas.drawRect(child.getRight(), top, child.getRight() + space, bottom, paint);
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(child.getLeft() - space, top, child.getLeft(), bottom, paint);
                        }
                    }
                }
            }
        }
    }

    void drawLineHorizontal(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) parent.getLayoutManager();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            int currentPosition = parent.getChildAdapterPosition(child);
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                if (linearLayoutManager.getReverseLayout()) {
                    int top = child.getTop() - space;
                    int bottom = child.getTop();
                    if (paint != null) {
                        canvas.drawRect(left, top, right, bottom, paint);
                    }
                } else {
                    int top = child.getBottom();
                    int bottom = top + space;
                    if (paint != null) {
                        canvas.drawRect(left, top, right, bottom, paint);
                    }
                }
            }
            //第一行顶部间隔
            if (needFirstTopEdge && currentPosition == offsetPosition && currentPosition != endDataPosition) {
                if (linearLayoutManager.getReverseLayout()) {
                    if (paint != null) {
                        canvas.drawRect(left, child.getBottom(), right, child.getBottom() + space, paint);
                    }
                } else {
                    if (paint != null) {
                        canvas.drawRect(left, child.getTop() - space, right, child.getTop(), paint);
                    }
                }
            }
        }
    }

    void drawGridVertical(Canvas canvas, RecyclerView parent) {
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            int currentPosition = parent.getChildAdapterPosition(child);
            int spanIndex = getSpanIndex(parent, child, (RecyclerView.LayoutParams) child.getLayoutParams());
            //去掉header，上下拉item
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                if (spanIndex == (spanCount - 1)) {
                    int top = child.getTop();
                    int bottomTop = child.getBottom();
                    int bottom = bottomTop + space;
                    int left = child.getLeft();
                    int right = child.getRight();
                    int rightRight = right + space;

                    if (getCurReverseLayout(parent.getLayoutManager())) {
                        if (paint != null) {
                            canvas.drawRect(left - space, top - space, right + space, top, paint);
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(left, bottomTop, right, bottom, paint);
                        }
                    }

                    if (paint != null && needGridRightLeftEdge) {
                        canvas.drawRect(right, top, rightRight, bottom, paint);
                    }

                } else if (spanIndex == 0) {
                    int top = child.getTop();
                    int bottomTop = child.getBottom();
                    int bottom = bottomTop + space;
                    int left = child.getLeft();
                    int leftRight = left + space;
                    int right = child.getRight();
                    int rightRight = right + space;

                    if (getCurReverseLayout(parent.getLayoutManager())) {
                        if (paint != null) {
                            canvas.drawRect(left - space, top - space, right + space, top, paint);
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(left, bottomTop, right, bottom, paint);
                        }
                    }

                    if (paint != null) {
                        canvas.drawRect(right, top, rightRight, bottom, paint);
                    }

                    if (paint != null && needGridRightLeftEdge) {
                        canvas.drawRect(left - space, top, leftRight - space, bottom, paint);
                    }
                } else {
                    int top = child.getTop();
                    int bottomTop = child.getBottom();
                    int bottom = bottomTop + space;
                    int left = child.getLeft();
                    int right = child.getRight();
                    int rightRight = right + space;

                    if (getCurReverseLayout(parent.getLayoutManager())) {
                        if (paint != null) {
                            canvas.drawRect(left - space, top - space, right + space, top, paint);
                        }
                    } else {

                        if (paint != null) {
                            canvas.drawRect(left, bottomTop, right, bottom, paint);
                        }
                    }

                    if (paint != null) {
                        canvas.drawRect(right, top, rightRight, bottom, paint);
                    }
                }
                stagFixEnd(canvas, parent, child, i, currentPosition);
            }

            //第一行顶部间隔
            if (needFirstTopEdge && currentPosition >= offsetPosition
                    && currentPosition < (offsetPosition + spanCount)
                    && currentPosition != endDataPosition) {
                int top = child.getTop() - space;
                int topBottom = child.getTop();
                int left = child.getLeft() - space;
                int right = child.getRight() + space;
                if (getCurReverseLayout(parent.getLayoutManager())) {
                    if (paint != null) {
                        canvas.drawRect(left, child.getBottom(), right, child.getBottom() + space, paint);
                    }
                } else {
                    if (paint != null) {
                        canvas.drawRect(left, top, right, topBottom, paint);
                    }
                }
            }
        }
    }

    void drawGridHorizontal(Canvas canvas, RecyclerView parent) {
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            int currentPosition = parent.getChildAdapterPosition(child);
            int spanIndex = getSpanIndex(parent, child, (RecyclerView.LayoutParams) child.getLayoutParams());

            //去掉header，上下拉item
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                if (spanIndex == 0) {
                    int top = child.getTop();
                    int bottomTop = child.getBottom();
                    int bottom = bottomTop + space;
                    int left = child.getLeft();
                    int right = child.getRight();
                    int rightRight = right + space;

                    if (paint != null) {
                        canvas.drawRect(left, bottomTop, right, bottom, paint);
                    }

                    if (getCurReverseLayout(parent.getLayoutManager())) {
                        if (paint != null) {
                            canvas.drawRect(left - space, top - space, left, bottom, paint);
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(right, top, rightRight, bottom, paint);
                        }
                    }

                    if (paint != null && needGridRightLeftEdge) {
                        canvas.drawRect(left, top - space, right + space, top, paint);
                    }

                } else if (spanIndex == (spanCount - 1)) {

                    int top = child.getTop();
                    int bottomTop = child.getBottom();
                    int bottom = bottomTop + space;
                    int left = child.getLeft();
                    int right = child.getRight();
                    int rightRight = right + space;

                    if (getCurReverseLayout(parent.getLayoutManager())) {
                        if (paint != null) {
                            canvas.drawRect(left - space, top - space, left, bottom, paint);
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(right, top, rightRight, bottom, paint);
                        }
                    }

                    if (paint != null && needGridRightLeftEdge) {
                        canvas.drawRect(left, bottomTop, right, bottom, paint);
                    }

                } else {
                    int top = child.getTop();
                    int bottomTop = child.getBottom();
                    int bottom = bottomTop + space;
                    int left = child.getLeft();
                    int right = child.getRight();
                    int rightRight = right + space;

                    if (paint != null) {
                        canvas.drawRect(left, bottomTop, right, bottom, paint);
                    }

                    if (getCurReverseLayout(parent.getLayoutManager())) {
                        if (paint != null) {
                            canvas.drawRect(left - space, top - space, left, bottom, paint);
                        }
                    } else {
                        if (paint != null) {
                            canvas.drawRect(right, top, rightRight, bottom, paint);
                        }
                    }
                }
                stagFixEnd(canvas, parent, child, i, currentPosition);
            }

            //第一行顶部间隔
            if (needFirstTopEdge && currentPosition >= offsetPosition
                    && currentPosition < (offsetPosition + spanCount)
                    && currentPosition != endDataPosition) {
                if (getCurReverseLayout(parent.getLayoutManager())) {
                    if (paint != null) {
                        canvas.drawRect(child.getRight(), child.getTop() - space, child.getRight() + space, child.getBottom() + space, paint);
                    }
                } else {
                    if (paint != null) {
                        canvas.drawRect(child.getLeft() - space, child.getTop() - space, child.getLeft(), child.getBottom() + space, paint);
                    }
                }
            }

        }
    }

    /**
     * 针对瀑布流的优化处理，因为瀑布流有长短边
     */
    void stagFixEnd(Canvas canvas, RecyclerView parent, View child, int i, int currentPosition) {
        if (!(parent.getLayoutManager() instanceof StaggeredGridLayoutManager)) {
            return;
        }
        if (orientation == OrientationHelper.HORIZONTAL) {
            if (i > 0) {
                int row = (int) Math.ceil((float) (dataSize - 1) / spanCount);
                int curRow = (int) Math.ceil((currentPosition - offsetPosition) / (float) spanCount);
                if (row == curRow) {
                    stagFixHorizontal(canvas, parent, child, i - 1);
                }
            }
        } else {
            if (i > 0) {
                int row = (int) Math.ceil((float) (dataSize - 1) / spanCount);
                int curRow = (int) Math.ceil((currentPosition - offsetPosition) / (float) spanCount);
                if (row == curRow) {
                    stagFixVertical(canvas, parent, child, i);
                }
            }
        }
    }

    /**
     * 补齐那些缺少的瀑布流，存在缺角需要补齐
     */
    void stagFixVertical(Canvas canvas, RecyclerView parent, View view, int position) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int spanIndex = getSpanIndex(parent, view, (RecyclerView.LayoutParams) view.getLayoutParams());
            if (spanIndex != 0) {
                if (getCurReverseLayout(layoutManager)) {
                    //倒过来的，最后的旁边那个，应该加1，因为减1就是loadmre的view了
                    View preView = parent.getChildAt(position + 1);
                    if (preView.getTop() > view.getTop()) {
                        canvas.drawRect(view.getLeft() - space, view.getTop() - space
                                , view.getLeft(), preView.getTop(), paint);
                    }
                } else {
                    View preView = parent.getChildAt(position - 1);
                    if (preView.getBottom() < view.getBottom()) {
                        canvas.drawRect(view.getLeft() - space, preView.getBottom()
                                , view.getLeft(), view.getBottom() + space, paint);
                    }
                }
            }
        }
    }

    /**
     * 补齐那些缺少的瀑布流，存在缺角需要补齐
     */
    void stagFixHorizontal(Canvas canvas, RecyclerView parent, View view, int position) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            int spanIndex = getSpanIndex(parent, view, (RecyclerView.LayoutParams) view.getLayoutParams());
            if (spanIndex != 0) {
                View preView = parent.getChildAt(position);
                if (getCurReverseLayout(layoutManager)) {
                    if (preView.getLeft() > view.getLeft()) {
                        canvas.drawRect(view.getLeft()
                                , view.getTop() - space, preView.getLeft(), view.getTop(), paint);
                    }
                } else {
                    if (preView.getRight() < view.getRight()) {
                        canvas.drawRect(preView.getRight(), view.getTop() - space
                                , view.getRight() + space, view.getTop(),  paint);
                    }
                }
            }
        }
    }

    protected int getSpanIndex(RecyclerView parent, View view, RecyclerView.LayoutParams layoutParams) {
        if (layoutParams instanceof GridLayoutManager.LayoutParams) {
            int currentPosition = parent.getChildAdapterPosition(view);
            return ((currentPosition - offsetPosition) % spanCount);
        } else if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            return ((StaggeredGridLayoutManager.LayoutParams) layoutParams).getSpanIndex();
        }
        return 0;
    }

    protected int getOrientation(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        }
        return 0;
    }

    protected int getSpanCount(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return 0;
    }

    private boolean getCurReverseLayout(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getReverseLayout();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getReverseLayout();
        } else if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getReverseLayout();
        }
        return false;
    }


    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public void setColor(int color) {
        this.color = color;
        if (paint != null) {
            paint.setColor(color);
        }
    }

    public void setNeedGridRightLeftEdge(boolean needGridRightLeftEdge) {
        this.needGridRightLeftEdge = needGridRightLeftEdge;
    }

    public void setNeedFirstTopEdge(boolean needFirstTopEdge) {
        this.needFirstTopEdge = needFirstTopEdge;
    }
}