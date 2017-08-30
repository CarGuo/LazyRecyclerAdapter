package com.shuyu.bind;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shuyu.bind.holder.NormalBindErrorHolder;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.listener.OnItemLongClickListener;

import java.util.List;

/**
 * 通用绑定的adapter
 */
public class NormalBindRecyclerAdapter extends RecyclerView.Adapter {

    private final static String TAG = "NormalBindRecyclerAdapter";

    private Context context = null;

    //数据
    private List dataList = null;

    //管理器
    private NormalBindAdapterManager normalAdapterManager;

    //最后的位置
    private int lastPosition = -1;

    public NormalBindRecyclerAdapter(Context context, NormalBindAdapterManager normalAdapterManager, List dataList) {
        this.normalAdapterManager = normalAdapterManager;
        this.dataList = dataList;
        this.context = context;
    }

    /**
     * 更新
     */
    public void notifychange() {
        notifyDataSetChanged();
    }

    /**
     * 删除
     */
    public void remove(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 增加
     */
    @SuppressWarnings("unchecked")
    public void add(Object object, int position) {
        dataList.add(position, object);
        notifyItemInserted(position);
    }

    /**
     * 往后添加数据
     */
    @SuppressWarnings("unchecked")
    public synchronized void addListData(List data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 设置新数据
     */
    public void setListData(List data) {
        dataList = data;
        lastPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * 获取列表数据
     */
    public List getDataList() {
        return dataList;
    }

    /**
     * 获取最后一个位置
     */
    public int getLastPosition() {
        return lastPosition;
    }

    /**
     * 最后一个的位置
     */
    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isFooter(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //错误数据
        if (viewType == NormalBindErrorHolder.ID) {
            View v = LayoutInflater.from(context).inflate(NormalBindErrorHolder.ID, parent, false);
            return new NormalBindErrorHolder(context, v);
        }

        //是否显示没有数据页面
        if (normalAdapterManager.isShowNoData() && dataList != null && dataList.size() == 0) {
            return normalAdapterManager.getNoDataViewTypeHolder(context, parent);
        }

        //如果有加载更多
        if (normalAdapterManager.isSupportLoadMore() && viewType == normalAdapterManager.getLoadMoreId()) {
            return normalAdapterManager.getLoadMoreViewTypeHolder(context, parent);
        }

        final RecyclerView.ViewHolder holder = normalAdapterManager.getViewTypeHolder(context, parent, viewType);

        //itemView 的点击事件
        if (normalAdapterManager.itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    normalAdapterManager.itemClickListener.onItemClick(holder.itemView.getContext(), holder.getPosition());
                }
            });
        }

        if (normalAdapterManager.itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return normalAdapterManager.itemLongClickListener.onItemClick(holder.itemView.getContext(), holder.getPosition());
                }
            });
        }


        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (position < 0)
            return;

        Object model;

        if (normalAdapterManager.isShowNoData() && dataList != null && dataList.size() == 0) {
            NormalBindRecyclerBaseHolder recyclerHolder = (NormalBindRecyclerBaseHolder) holder;
            recyclerHolder.setAdapter(this);
            recyclerHolder.onBind(normalAdapterManager.getNoDataObject(), position);
            return;
        }

        if (normalAdapterManager.isSupportLoadMore() && position + 1 == getItemCount()) {
            model = normalAdapterManager.getLoadMoreObject();
            NormalBindLoadMoreHolder normalLoadMoreHolder = (NormalBindLoadMoreHolder) holder;
            normalLoadMoreHolder.switchLoadMore(model, normalAdapterManager.loadMoreState);
        } else {
            model = dataList.get(position);
        }


        if (holder instanceof NormalBindErrorHolder) {
            ((NormalBindErrorHolder) (holder)).onBind(model, position);
            return;
        }


        NormalBindRecyclerBaseHolder recyclerHolder = (NormalBindRecyclerBaseHolder) holder;

        recyclerHolder.setAdapter(this);

        recyclerHolder.onBind(model, position);

        if (normalAdapterManager.needAnimation && recyclerHolder.getAnimator(recyclerHolder.itemView) != null
                && position > lastPosition) {

            recyclerHolder.getAnimator(recyclerHolder.itemView).start();
            lastPosition = position;

        } else if (position > lastPosition) {

            lastPosition = position;

        }
    }

    @Override
    public int getItemCount() {
        //如果是显示没有数据，那么也要一个item作为显示
        if (normalAdapterManager.isShowNoData() && dataList.size() == 0) {
            return 1;
        }
        return (normalAdapterManager.isSupportLoadMore()) ? dataList.size() + 1 : dataList.size();

    }


    @Override
    public int getItemViewType(int position) {
        //如果位置不对，就返回
        if (position < 0 || (!normalAdapterManager.isSupportLoadMore() && position > getItemCount() - 1
                || (normalAdapterManager.isSupportLoadMore() && position > getItemCount())))
            return 0;

        //如果没有数据，就显示空页面
        if (normalAdapterManager.isShowNoData() && dataList.size() == 0) {
            return normalAdapterManager.getNoDataLayoutId();
        }

        //如果是最后，就加载更多
        if (normalAdapterManager.isSupportLoadMore() && position + 1 == getItemCount()) {
            return normalAdapterManager.getLoadMoreId();
        }

        //返回需要显示的ID
        Object object = dataList.get(position);
        List<Integer> modelToId = normalAdapterManager.getModelToId().get(object.getClass().getName());


        if (modelToId == null || modelToId.size() == 0) {
            return NormalBindErrorHolder.ID;
        }

        int layoutId = modelToId.get(modelToId.size() - 1);
        if (normalAdapterManager.getNormalBindDataChooseListener() != null && modelToId.size() > 1) {
            layoutId = normalAdapterManager.getNormalBindDataChooseListener().
                    getCurrentDataLayoutId(object, object.getClass(), position, modelToId);
        }
        if (-1 == layoutId || layoutId == 0 || layoutId == Integer.MAX_VALUE) {
            return NormalBindErrorHolder.ID;
        }
        return layoutId;
    }

    private boolean isFooter(int position) {
        if (normalAdapterManager.isSupportLoadMore()) {
            return position == getItemCount() - 1;
        } else {
            return false;
        }
    }

}
