package com.shuyu.common;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.shuyu.common.holder.LoadMoreHolder;
import com.shuyu.common.holder.NoDataHolder;
import com.shuyu.common.listener.OnItemClickListener;
import com.shuyu.common.listener.OnItemLongClickListener;
import com.shuyu.common.model.LoadMoreModel;
import com.shuyu.common.model.RecyclerBaseModel;

import java.util.List;

/**
 * Created by Guo on 2015/11/23.
 * 通用recyclerView的adapter
 *
 *
 * 1、把你布局的LayoutID和对应的holder类名放到CommonRecyclerManager里面
 * 2、为你的model继承RecyclerBaseModel,设置上面的LayoutID->resLayoutId;
 * 3、把model的list数据和CommonRecyclerManager传给adapter
 */
public class CommonRecyclerAdapter extends RecyclerView.Adapter {

    private final static String TAG = "CommonRecyclerAdapter";

    private Context context = null;

    //数据
    private List<RecyclerBaseModel> dataList = null;

    //管理器
    private CommonRecyclerManager commonRecyclerManager;

    //单击
    private OnItemClickListener itemClickListener;

    //长按
    private OnItemLongClickListener itemLongClickListener;

    //空数据model
    private RecyclerBaseModel noDataModel;

    //加载更多的配置model
    private LoadMoreModel loadMoreModel;

    //支持加载更多
    private boolean supportLoadMore;

    //支持显示没有数据
    private boolean showNoData = false;

    //是否支持动画
    private boolean needAnimation = false;

    //加载状态
    private int loadMoreState = LoadMoreHolder.LOAD_MORE_STATE;

    //没有数据的布局id
    private int noDataLayoutId;

    //最后的位置
    private int lastPosition = -1;

    public CommonRecyclerAdapter(Context context, CommonRecyclerManager commonRecyclerManager, List<RecyclerBaseModel> dataList) {
        this.commonRecyclerManager = commonRecyclerManager;
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
    public void add(RecyclerBaseModel recyclerBaseModel, int position) {
        dataList.add(position, recyclerBaseModel);
        notifyItemInserted(position);
    }

    /**
     * 往后添加数据
     */
    public synchronized void addListData(List<RecyclerBaseModel> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 设置新数据
     */
    public void setListData(List<RecyclerBaseModel> data) {
        dataList = data;
        lastPosition = -1;
        notifyDataSetChanged();
    }

    /**
     * 获取列表数据
     */
    public List<RecyclerBaseModel> getDataList() {
        return dataList;
    }

    /**
     * 是否支持加载更多
     */
    public void setNeedLoadMore(boolean isLoadMore) {
        this.supportLoadMore = isLoadMore;
    }

    /**
     * 加载更多的状态
     */
    public void setLoadMoreState(int loadMoreState) {
        this.loadMoreState = loadMoreState;
    }

    /**
     * 设置点击
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * 设置长按
     */
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    /**
     * 是否显示空数据页面
     */
    public boolean isShowNoData() {
        return showNoData;
    }

    /**
     * 是否显示空数据页面
     */
    public void setShowNoData(boolean showNoData) {
        this.showNoData = showNoData;
    }

    public RecyclerBaseModel getNoDataModel() {
        return noDataModel;
    }

    /**
     * 是否显示空数据model
     */
    public void setNoDataModel(RecyclerBaseModel noDataModel) {
        this.noDataModel = noDataModel;
    }

    public int getNoDataLayoutId() {
        return noDataLayoutId;
    }

    /**
     * 是否显示空数据页面布局
     */
    public void setNoDataLayoutId(int noDataLayoutId) {
        this.noDataLayoutId = noDataLayoutId;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    /**
     * 最后一个的位置
     */
    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public boolean isNeedAnimation() {
        return needAnimation;
    }

    /**
     * 是否需要动画
     */
    public void setNeedAnimation(boolean needAnimation) {
        this.needAnimation = needAnimation;
    }

    /**
     * 加载更多的样式配置
     */
    public void setLoadMoreModel(LoadMoreModel loadMoreModel) {
        this.loadMoreModel = loadMoreModel;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,  int viewType) {

        //是否显示没有数据页面
        if (showNoData && dataList != null && dataList.size() == 0) {

            if (noDataLayoutId == 0) {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.no_data, parent, false);
                return new NoDataHolder(context, view);
            } else {
                return commonRecyclerManager.getViewTypeHolder(context, parent, noDataLayoutId);
            }
        }

        //如果有加载更多
        if (supportLoadMore && viewType == LoadMoreHolder.LAYOUTID) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    LoadMoreHolder.LAYOUTID, parent, false);
            return new LoadMoreHolder(context, view);
        }

        final RecyclerView.ViewHolder holder = commonRecyclerManager.getViewTypeHolder(context, parent, viewType);

        //itemView 的点击事件
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(holder.itemView.getContext(), holder.getPosition());
                }
            });
        }

        if (itemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return itemLongClickListener.onItemClick(holder.itemView.getContext(), holder.getPosition());
                }
            });
        }


        return holder;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (position < 0)
            return;

        RecyclerBaseModel model;

        if (showNoData && dataList != null && dataList.size() == 0) {
            RecyclerBaseHolder recyclerHolder = (RecyclerBaseHolder) holder;
            recyclerHolder.setAdapter(this);
            if (noDataModel != null)
                recyclerHolder.onBind(noDataModel, position);
            return;
        }

        if (supportLoadMore && position + 1 == getItemCount()) {
            //如果有加载更多，设置类型,是否锁定页面
            if (loadMoreModel == null) {
                model = new LoadMoreModel();
            } else {
                model = loadMoreModel;
            }
            model.setExtraTag(loadMoreState);
        } else {
            model = dataList.get(position);
        }

        RecyclerBaseHolder recyclerHolder = (RecyclerBaseHolder) holder;

        recyclerHolder.setAdapter(this);

        recyclerHolder.onBind(model, position);

        if (needAnimation && recyclerHolder.getAnimator(recyclerHolder.itemView) != null
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
        if (showNoData && dataList.size() == 0) {
            return 1;
        }
        return (supportLoadMore) ? dataList.size() + 1 : dataList.size();

    }


    @Override
    public int getItemViewType(int position) {
        //如果位置不对，就返回
        if (position < 0 || (!supportLoadMore && position > getItemCount() - 1
                || (supportLoadMore && position > getItemCount())))
            return 0;

        //如果没有数据，就显示空页面
        if (showNoData && dataList.size() == 0) {
            return R.layout.no_data;
        }

        //如果是最后，就加载更多
        if (supportLoadMore && position + 1 == getItemCount()) {
            return LoadMoreHolder.LAYOUTID;
        }

        //返回需要显示的ID
        int layoutId = dataList.get(position).getResLayoutId();
        if (-1 == layoutId || layoutId == 0 || layoutId == Integer.MAX_VALUE || layoutId == 0xffffffff) {
            try {
                String error = TAG + " Null LayoutId = " + position + " ： Model Layout Id Error  " + layoutId;
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                throw new Exception(error);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return layoutId;
    }

    private boolean isFooter(int position) {
        if (supportLoadMore) {
            return position == getItemCount() - 1;
        } else {
            return false;
        }
    }

}
