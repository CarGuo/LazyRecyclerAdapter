package com.shuyu.bind;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.Collections;
import java.util.List;

/**
 * 拖动回调
 * Created by guoshuyu on 2017/9/11.
 */

public class BindDragCallBack extends ItemTouchHelper.Callback {

    private BindSuperAdapter mAdapter;

    //是否可以拖动
    private boolean mDragEnabled = true;

    //是否可以swipe
    private boolean mSwipeEnabled = false;

    //限制首位不能拖动
    private boolean mLimitStartPosition = false;

    //限制结束不能拖动
    private boolean mLimitEndPosition = false;

    private int mSwipeFlags = ItemTouchHelper.START;

    private DragMoveListener mMoveListener;

    private SwipeListener mSwipeListener;

    private boolean isSwiped = false;

    public BindDragCallBack(BindSuperAdapter adapter, boolean limitStartPosition, boolean limitEndPosition) {
        super();
        init(adapter, limitStartPosition, limitEndPosition);
    }

    public BindDragCallBack(BindSuperAdapter adapter) {
        super();
        init(adapter, false, false);
    }

    private void init(BindSuperAdapter adapter, boolean limitStartPosition, boolean limitEndPosition) {
        mLimitEndPosition = limitEndPosition;
        mLimitStartPosition = limitStartPosition;
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags;
        final int swipeFlags;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager
                || recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            swipeFlags = mSwipeFlags;
        }
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        int fromPosition = viewHolder.getAdapterPosition();
        int toPosition = target.getAdapterPosition();
        if (mLimitStartPosition && (fromPosition == 0 || toPosition == 0)) {
            return false;
        }
        if ((viewHolder instanceof BindSuperAdapter.WrapAdapter.SimpleViewHolder)) {
            return false;
        }
        return true;
    }

    @Override
    public void onMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        int fromPosition = viewHolder.getAdapterPosition() - mAdapter.absFirstPosition();
        int toPosition = target.getAdapterPosition() - mAdapter.absFirstPosition();
        List dataList = mAdapter.getDataList();
        if (fromPosition >= 0 && toPosition >= 0 && dataList != null
                && toPosition < dataList.size() && fromPosition < dataList.size()) {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    if (fromPosition < dataList.size() && toPosition < dataList.size()) {
                        Collections.swap(dataList, i, i + 1);
                    }
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    if (fromPosition < dataList.size() && toPosition < dataList.size()) {
                        Collections.swap(dataList, i, i - 1);
                    }
                }
            }
            //数据和item的位置不是一致的
            if (mAdapter != null) {
                mAdapter.notifyItemMoved(fromPos, toPos);
            }

            if (mMoveListener != null) {
                mMoveListener.onMoved(fromPosition, toPosition);
            }
        }
        isSwiped = false;
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if ((viewHolder instanceof BindSuperAdapter.WrapAdapter.SimpleViewHolder)) {
            return;
        }

        int pos = viewHolder.getAdapterPosition() - mAdapter.absFirstPosition();


        if (pos >= 0 && pos <= (mAdapter.getDataList().size() - 1)) {
            isSwiped = true;
            int position = viewHolder.getAdapterPosition();
            mAdapter.getDataList().remove(pos);
            mAdapter.notifyItemRemoved(position);
            if (position != mAdapter.getDataList().size()) {
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount() - position);
            }
            if (mSwipeListener != null && mSwipeEnabled) {
                mSwipeListener.onSwiped(pos);
            }
        }
    }

    /**
     * 当长按选中item的时候（拖拽开始的时候）调用
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            return;
        }
        if (mMoveListener != null) {
            mMoveListener.onMoveStart();
        }
    }

    /**
     * /当手指松开的时候（拖拽完成的时候）调用
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if ((viewHolder instanceof BindSuperAdapter.WrapAdapter.SimpleViewHolder)) {
            return;
        }

        if (isSwiped) {
            isSwiped = false;
            return;
        }

        if (mMoveListener != null) {
            mMoveListener.onMoveEnd();
        }
        try {
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int position = viewHolder.getAdapterPosition() - mAdapter.absFirstPosition();
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (position >= 0 && position <= (mAdapter.getDataList().size() - 1) && !(viewHolder instanceof BindSuperAdapter.WrapAdapter.SimpleViewHolder)) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        } else {
            dY = DragLimited(viewHolder, dY);
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return mDragEnabled;
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return mSwipeEnabled;
    }


    private float DragLimited(RecyclerView.ViewHolder viewHolder, float dY) {

        int position = viewHolder.getAdapterPosition() - mAdapter.absFirstPosition();

        if (position < 0) {
            return dY < 0 ? 0 : dY;
        }

        if (position > (mAdapter.getDataList().size() - 1)) {
            return dY < 0 ? 0 : dY;
        }

        if (mLimitStartPosition && position == 0) {
            return dY < 0 ? 0 : dY;
        }

        if (mLimitEndPosition && position == mAdapter.getDataList().size() - 1) {
            return dY > 0 ? 0 : dY;
        }

        return dY;
    }

    /**
     * 是否使能拖动
     */
    public void setDragMoveEnabled(boolean dragEnabled) {
        mDragEnabled = dragEnabled;
    }

    /**
     * 拖动监听
     */
    public void setDragMoveListener(DragMoveListener moveListener) {
        mMoveListener = moveListener;
    }

    /**
     * 滑动监听
     */
    public void setSwipeListener(SwipeListener swipeListener) {
        this.mSwipeListener = swipeListener;
    }

    /**
     * 滑动方向
     */
    public void setSwipeFlags(int swipeFlags) {
        this.mSwipeFlags = swipeFlags;
    }

    /**
     * 使能滑动
     */
    public void setSwipeEnabled(boolean swipeEnabled) {
        this.mSwipeEnabled = swipeEnabled;
    }

    public interface DragMoveListener {
        /**
         * Item 切换位置
         *
         * @param fromPosition 开始位置
         * @param toPosition   结束位置
         */
        void onMoved(int fromPosition, int toPosition);

        /**
         * 开始移动
         */
        void onMoveStart();

        /**
         * 停止移动
         */
        void onMoveEnd();
    }

    public interface SwipeListener {

        void onSwiped(int position);
    }

}
