package com.shuyu.bind;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shuyu.bind.listener.OnBindDataChooseListener;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.listener.OnItemLongClickListener;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 通用适配管理基类
 * Created by guoshuyu on 2017/8/28.
 */

abstract class BindBaseAdapterManager<T extends BindBaseAdapterManager> {

    private final String TAG = BindBaseAdapterManager.class.getName();

    //根据model类名绑定layoutId
    private HashMap<String, List<Integer>> modelToId = new HashMap<>();

    //根据id类名绑定holder
    private HashMap<Integer, Class<? extends BindRecyclerBaseHolder>> idToHolder = new HashMap<>();

    //没有数据的布局id
    private int noDataLayoutId = -1;

    //没有数据的object
    private Object noDataObject;

    //没有数据的holder
    private Class<? extends BindRecyclerBaseHolder> noDataHolder;

    //一个model对应多种Holder的数据的筛选，不设置默认使用最后一个
    private OnBindDataChooseListener normalBindDataChooseListener;

    //单击
    OnItemClickListener itemClickListener;

    //长按
    OnItemLongClickListener itemLongClickListener;

    //是否支持动画
    boolean needAnimation = false;

    /**
     * 绑定加载更多模块显示
     *
     * @param modelClass  数据实体类名，一个model可以对用多个布局和Holder
     * @param layoutId    布局id，一个manager中，一个id只能对应一个holder。
     * @param holderClass holder类名，一个manager中，一个holder只能对应一个id。
     */
    public T bind(Class modelClass, int layoutId, Class<? extends BindRecyclerBaseHolder> holderClass) {

        if (!modelToId.containsKey(modelClass.getName())) {
            List<Integer> list = new ArrayList<>();
            list.add(layoutId);
            modelToId.put(modelClass.getName(), list);
        } else {
            List<Integer> list = modelToId.get(modelClass.getName());
            if (!queryIdIn(list, layoutId)) {
                list.add(layoutId);
                modelToId.put(modelClass.getName(), list);
            }
        }
        if (!idToHolder.containsKey(layoutId)) {
            idToHolder.put(layoutId, holderClass);
        } else {
            Log.e(TAG, "*******************layoutId had bind Holder******************* \n" + holderClass.getName());
        }
        return (T)this;
    }

    /**
     * 绑定空数据显示
     *
     * @param noDataModel 数据实体
     * @param layoutId    布局id
     * @param holderClass holder类名
     */
    public T bindEmpty(Object noDataModel, int layoutId, Class<? extends BindRecyclerBaseHolder> holderClass) {
        noDataHolder = holderClass;
        noDataObject = noDataModel;
        noDataLayoutId = layoutId;
        return (T)this;
    }

    /**
     * 一个model对应多种Holder的数据的筛选回调
     * 不设置默认使用最后一个
     */
    public T bingChooseListener(OnBindDataChooseListener listener) {
        normalBindDataChooseListener = listener;
        return (T)this;
    }

    /**
     * 设置点击
     */
    public T setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
        return (T)this;
    }

    /**
     * 设置长按
     */
    public T setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
        return (T)this;
    }

    /**
     * 是否支持动画
     */
    public boolean isNeedAnimation() {
        return needAnimation;
    }

    /**
     * 设置是否需要动画
     */
    public T setNeedAnimation(boolean needAnimation) {
        this.needAnimation = needAnimation;
        return (T)this;
    }

    boolean isSupportLoadMore() {
        return false;
    }

    HashMap<String, List<Integer>> getModelToId() {
        return modelToId;
    }

    HashMap<Integer, Class<? extends BindRecyclerBaseHolder>> getIdToHolder() {
        return idToHolder;
    }

    Object getNoDataObject() {
        return noDataObject;
    }

    Class<? extends BindRecyclerBaseHolder> getNoDataHolder() {
        return noDataHolder;
    }

    int getNoDataLayoutId() {
        return noDataLayoutId;
    }

    /**
     * 是否需要显示空数据
     */
    boolean isShowNoData() {
        return noDataLayoutId != -1 && noDataHolder != null && noDataObject != null;
    }

    OnBindDataChooseListener getNormalBindDataChooseListener() {
        return normalBindDataChooseListener;
    }

    /**
     * 创建正常数据的holder
     */
    BindRecyclerBaseHolder getViewTypeHolder(Context context, ViewGroup parent, int viewType) {
        Class<? extends BindRecyclerBaseHolder> idToHolder = getIdToHolder().get(viewType);
        return contructorHolder(context, parent, idToHolder, viewType);
    }

    /**
     * 创建空数据的holder
     */
    BindRecyclerBaseHolder getNoDataViewTypeHolder(Context context, ViewGroup parent) {
        return contructorHolder(context, parent, getNoDataHolder(), noDataLayoutId);
    }

    /**
     * 根据参数构造
     */
    @SuppressWarnings("all")
    private <T> T contructorHolder(Context context, ViewGroup parent, Class<T> classType, int layoutId) {
        Constructor object = null;
        boolean constructorFirst = true;

        View v = LayoutInflater.from(context).inflate(layoutId, parent, false);

        try {
            object = classType.getDeclaredConstructor(new Class[]{Context.class, View.class});
        } catch (NoSuchMethodException e) {
            constructorFirst = false;
            //e.printStackTrace();
        }
        if (!constructorFirst) {
            try {
                object = classType.getDeclaredConstructor(new Class[]{View.class});
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            }
        }
        if (object == null) {
            throw new RuntimeException("Holder Constructor Error For : " + classType.getName());
        }
        try {
            object.setAccessible(true);
            if (constructorFirst) {
                return (T) object.newInstance(context, v);
            } else {
                return (T) object.newInstance(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private boolean queryIdIn(List<Integer> list, int id) {
        for (int i : list) {
            if (i == id) {
                return true;
            }
        }
        return false;
    }

}
