package com.shuyu.bind.decoration;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

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

    private boolean needGridRightLeftEdge = true;

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
                gridRect(parent, view, outRect);
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
                drawGridVertical(c, parent);
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
        int currentPosition = parent.getChildAdapterPosition(view);
        //去掉header，上下拉item
        if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                outRect.right = space;
            } else {
                outRect.bottom = space;
            }
            if (linearLayoutManager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                outRect.right = space;
            } else {
                outRect.bottom = space;
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
                outRect.bottom = space;
                if (needGridRightLeftEdge) {
                    outRect.right = space;
                }
            } else if (spanIndex == 0) {
                outRect.right = space;
                outRect.bottom = space;
                if (needGridRightLeftEdge) {
                    outRect.left = space;
                }
            }
        }
    }

    void drawLineVertical(Canvas canvas, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getMeasuredHeight() - parent.getPaddingBottom();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + layoutParams.rightMargin;
            final int right = left + space;
            if (paint != null) {
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    void drawLineHorizontal(Canvas canvas, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        final int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child = parent.getChildAt(i);
            int currentPosition = parent.getChildAdapterPosition(child);
            if (currentPosition >= offsetPosition && currentPosition < endDataPosition) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
                int top = child.getBottom() + layoutParams.bottomMargin;
                int bottom = top + space;
                if (paint != null) {
                    canvas.drawRect(left, top, right, bottom, paint);
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

                    if (paint != null) {
                        canvas.drawRect(left, bottomTop, right, bottom, paint);
                    }

                    if (paint != null) {
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

                    if (paint != null) {
                        canvas.drawRect(left, bottomTop, right, bottom, paint);
                    }

                    if (paint != null) {
                        canvas.drawRect(right, top, rightRight, bottom, paint);
                    }

                    if (paint != null) {
                        canvas.drawRect(left - space, top, leftRight - space, bottom, paint);
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
}