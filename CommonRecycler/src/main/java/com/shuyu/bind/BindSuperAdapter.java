package com.shuyu.bind;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.jcodecraeer.xrecyclerview.ArrowRefreshHeader;
import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;
import com.jcodecraeer.xrecyclerview.other.AppBarStateChangeListener;
;
import java.util.List;

import static android.view.View.GONE;
import static com.shuyu.bind.BindSuperAdapterManager.HEADER_INIT_INDEX;

/**
 * Created by guoshuyu on 2017/8/30.
 */

public class BindSuperAdapter extends BindRecyclerAdapter implements View.OnTouchListener {

    //下面的ItemViewType是保留值(ReservedItemViewType),如果用户的adapter与它们重复将会强制抛出异常。
    //不过为了简化,我们检测到重复时对用户的提示是ItemViewType必须小于10000
    //设置一个很大的数字,尽可能避免和用户的adapter冲突
    private static final int TYPE_REFRESH_HEADER = 10000;

    private static final int TYPE_FOOTER = 10001;

    //下拉刷新的移动系数
    private static final float DRAG_RATE = 3;

    //兼容 appbar
    private AppBarStateChangeListener.State appbarState = AppBarStateChangeListener.State.EXPANDED;

    //用在内外数据变化更新的同步更新
    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    private WrapAdapter mWrapAdapter;

    private RecyclerView mRecyclerView;

    private Context context;

    private BindSuperAdapterManager normalAdapterManager;

    //触摸下拉移动的距离
    private float mLastY = -1;

    private float mLastX = -1;

    public BindSuperAdapter(Context context, BindSuperAdapterManager normalAdapterManager, List dataList) {
        super(context, normalAdapterManager, dataList);
        this.normalAdapterManager = normalAdapterManager;
        this.context = context;
        init();
    }

    private void init() {
        if (normalAdapterManager.pullRefreshEnabled && normalAdapterManager.mRefreshHeader == null) {
            normalAdapterManager.mRefreshHeader = new ArrowRefreshHeader(context);
            normalAdapterManager.mRefreshHeader.setProgressStyle(normalAdapterManager.mRefreshProgressStyle);
        }
        if (normalAdapterManager.mFootView == null) {
            LoadingMoreFooter footView = new LoadingMoreFooter(context);
            footView.setProgressStyle(normalAdapterManager.mLoadingMoreProgressStyle);
            normalAdapterManager.mFootView = footView;
        }
        normalAdapterManager.mFootView.setVisibility(GONE);
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        mRecyclerView = recyclerView;
        if (adapter instanceof BindSuperAdapter) {
            mWrapAdapter = new WrapAdapter((BindSuperAdapter)adapter);
            recyclerView.setAdapter(mWrapAdapter);
            recyclerView.setOnTouchListener(this);
            recyclerView.addOnScrollListener(mScrollListener);
            adapter.registerAdapterDataObserver(mDataObserver);
            mDataObserver.onChanged();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (normalAdapterManager.mTouchListener != null) {
            return normalAdapterManager.mTouchListener.onTouch(v, ev);
        }
        if (getOrientation(mRecyclerView.getLayoutManager()) == OrientationHelper.HORIZONTAL) {
            return touchX(ev);
        }
        return touchY(ev);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder); //解决和CollapsingToolbarLayout冲突的问题
        AppBarLayout appBarLayout = null;
        ViewParent p = mRecyclerView.getParent();
        while (p != null) {
            if (p instanceof CoordinatorLayout) {
                break;
            }
            p = p.getParent();
        }
        if (p instanceof CoordinatorLayout) {
            CoordinatorLayout coordinatorLayout = (CoordinatorLayout) p;
            final int childCount = coordinatorLayout.getChildCount();
            for (int i = childCount - 1; i >= 0; i--) {
                final View child = coordinatorLayout.getChildAt(i);
                if (child instanceof AppBarLayout) {
                    appBarLayout = (AppBarLayout) child;
                    break;
                }
            }
            if (appBarLayout != null) {
                appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
                    @Override
                    public void onStateChanged(AppBarLayout appBarLayout, State state) {
                        appbarState = state;
                    }
                });
            }
        }
    }


    @Override
    int curPosition(int position) {
        int count = normalAdapterManager.getHeadersCount();
        boolean refresh = normalAdapterManager.isPullRefreshEnabled();
        return position - count - ((refresh) ? 1 : 0);
    }

    public int absFirstPosition() {
        int count = normalAdapterManager.getHeadersCount();
        boolean refresh = normalAdapterManager.isPullRefreshEnabled();
        return count + ((refresh) ? 1 : 0);
    }

    public int absFirstPositionWithoutHeader() {
        boolean refresh = normalAdapterManager.isPullRefreshEnabled();
        return ((refresh) ? 1 : 0);
    }

    public boolean emptyForLoadMore() {
        if ((getDataList() == null || getDataList().size() == 0) && !normalAdapterManager.loadingMoreEmptyEnabled) {
            return true;
        }
        return false;
    }

    private boolean touchX(MotionEvent ev) {
        if (mLastX == -1) {
            mLastX = ev.getRawX();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaX = ev.getRawX() - mLastX;
                mLastX = ev.getRawX();
                if (normalAdapterManager.pullRefreshEnabled && isOnTop() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    float moveD = deltaX / DRAG_RATE;
                    if (getCurReverseLayout(mRecyclerView.getLayoutManager())) {
                        moveD = -moveD;
                    }
                    normalAdapterManager.mRefreshHeader.onMove(moveD);
                    if (normalAdapterManager.mRefreshHeader.getVisibleHeight() > 0 && normalAdapterManager.mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastX = -1; // reset
                if (normalAdapterManager.pullRefreshEnabled && isOnTop() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (normalAdapterManager.mRefreshHeader.releaseAction()) {
                        if (normalAdapterManager.mLoadingListener != null) {
                            normalAdapterManager.mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return mRecyclerView.onTouchEvent(ev);
    }


    private boolean touchY(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (normalAdapterManager.pullRefreshEnabled && isOnTop() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    float moveD = deltaY / DRAG_RATE;
                    if (getCurReverseLayout(mRecyclerView.getLayoutManager())) {
                        moveD = -moveD;
                    }
                    normalAdapterManager.mRefreshHeader.onMove(moveD);
                    if (normalAdapterManager.mRefreshHeader.getVisibleHeight() > 0 && normalAdapterManager.mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING) {
                        return false;
                    }
                }
                break;
            default:
                mLastY = -1; // reset
                if (normalAdapterManager.pullRefreshEnabled && isOnTop() && appbarState == AppBarStateChangeListener.State.EXPANDED) {
                    if (normalAdapterManager.mRefreshHeader.releaseAction()) {
                        if (normalAdapterManager.mLoadingListener != null) {
                            normalAdapterManager.mLoadingListener.onRefresh();
                        }
                    }
                }
                break;
        }
        return mRecyclerView.onTouchEvent(ev);
    }

    /**
     * 滑动监听
     */
    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int state) {
            super.onScrollStateChanged(recyclerView, state);
            if (normalAdapterManager.onScrollListener != null) {
                normalAdapterManager.onScrollListener.onScrollStateChanged(recyclerView, state);
            }
            if (state == RecyclerView.SCROLL_STATE_IDLE && normalAdapterManager.mLoadingListener != null && !normalAdapterManager.isLoadingData && normalAdapterManager.loadingMoreEnabled) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastVisibleItemPosition;
                if (layoutManager instanceof GridLayoutManager) {
                    lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                    lastVisibleItemPosition = findMax(into);
                } else {
                    lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                }
                if (layoutManager.getChildCount() > 0
                        && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                        && layoutManager.getItemCount() > layoutManager.getChildCount()
                        && !normalAdapterManager.isNoMore
                        && (normalAdapterManager.mRefreshHeader == null
                        || normalAdapterManager.mRefreshHeader.getState() < ArrowRefreshHeader.STATE_REFRESHING)) {
                    if (emptyForLoadMore()) {
                        return;
                    }
                    normalAdapterManager.isLoadingData = true;
                    normalAdapterManager.mFootView.setState(LoadingMoreFooter.STATE_LOADING);
                    normalAdapterManager.mLoadingListener.onLoadMore();
                }
            }
        }
    };

    /**
     * 根据header的ViewType判断是哪个header
     */
    private View getHeaderViewByType(int itemType) {
        if (!isHeaderType(itemType)) {
            return null;
        }
        return normalAdapterManager.mHeaderViews.get(itemType - HEADER_INIT_INDEX);
    }

    /**
     * 判断一个type是否为HeaderType
     */
    private boolean isHeaderType(int itemViewType) {
        return normalAdapterManager.mHeaderViews.size() > 0 && normalAdapterManager.sHeaderTypes.contains(itemViewType);
    }

    /**
     * 判断是否是XRecyclerView保留的itemViewType
     */
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_REFRESH_HEADER || itemViewType == TYPE_FOOTER || normalAdapterManager.sHeaderTypes.contains(itemViewType)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 瀑布流里最后的一个item的位置
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 是否顶部
     */
    private boolean isOnTop() {
        if (normalAdapterManager.mRefreshHeader == null) {
            return true;
        }
        if (normalAdapterManager.mRefreshHeader.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 外部adapter更新的时候，同步更新内部adapter
     */
    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }
    }


    /**
     * 内部adapter
     */
    public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private BindRecyclerAdapter adapter;

        WrapAdapter(BindRecyclerAdapter adapter) {
            this.adapter = adapter;
        }

        boolean isHeader(int position) {
            int startHeader = (normalAdapterManager.pullRefreshEnabled ? 1 : 0);
            int endHeader = (normalAdapterManager.pullRefreshEnabled ? normalAdapterManager.mHeaderViews.size() + 1 : normalAdapterManager.mHeaderViews.size());
            return position >= startHeader && position < endHeader;
        }

        boolean isFooter(int position) {
            if (normalAdapterManager.loadingMoreEnabled) {
                return position == getItemCount() - 1;
            } else {
                return false;
            }
        }

        boolean isRefreshHeader(int position) {
            return position == 0 && normalAdapterManager.pullRefreshEnabled;
        }

        int getHeadersCount() {
            return normalAdapterManager.mHeaderViews.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_REFRESH_HEADER) {
                return new SimpleViewHolder(normalAdapterManager.mRefreshHeader);
            } else if (isHeaderType(viewType)) {
                return new SimpleViewHolder(getHeaderViewByType(viewType));
            } else if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(normalAdapterManager.mFootView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (isHeader(position) || isRefreshHeader(position)) {
                return;
            }
            int adjPosition = position - (normalAdapterManager.pullRefreshEnabled ? (getHeadersCount() + 1) : getHeadersCount());
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (normalAdapterManager.loadingMoreEnabled) {
                if (adapter != null) {
                    if (normalAdapterManager.pullRefreshEnabled)
                        return getHeadersCount() + adapter.getItemCount() + 2;
                    else
                        return getHeadersCount() + adapter.getItemCount() + 1;
                } else {
                    return getHeadersCount() + 2;
                }
            } else {
                if (adapter != null) {
                    if (normalAdapterManager.pullRefreshEnabled) {
                        return getHeadersCount() + adapter.getItemCount() + 1;
                    } else {
                        return getHeadersCount() + adapter.getItemCount();
                    }
                } else {
                    return getHeadersCount() + 1;
                }
            }
        }

        @Override
        public int getItemViewType(int position) {
            int adjPosition = position - (normalAdapterManager.pullRefreshEnabled ? (getHeadersCount() + 1) : getHeadersCount());
            if (isReservedItemViewType(adapter.getItemViewType(adjPosition))) {
                throw new IllegalStateException("require itemViewType in adapter should be less than 10000 ");
            }
            if (normalAdapterManager.pullRefreshEnabled && isRefreshHeader(position)) {
                return TYPE_REFRESH_HEADER;
            }
            if (isHeader(position)) {
                if (normalAdapterManager.pullRefreshEnabled) {
                    position = position - 1;
                }
                return normalAdapterManager.sHeaderTypes.get(position);
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }

            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeadersCount() + 1) {
                int adjPosition = position - (getHeadersCount() + 1);
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            adapter.onAttachedToRecyclerView(recyclerView);
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position) || isRefreshHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isRefreshHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }

        public BindRecyclerAdapter getBindRecyclerAdapter() {
            return adapter;
        }
    }

    private int getOrientation(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
        } else if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).getOrientation();
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
}
