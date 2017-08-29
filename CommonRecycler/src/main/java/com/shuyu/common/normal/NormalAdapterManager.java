package com.shuyu.common.normal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 对应数据
 * Created by guoshuyu on 2017/8/28.
 */

public class NormalAdapterManager {

    //根据model类名绑定layoutId
    private HashMap<String, List<Integer>> modelToId = new HashMap<>();

    //根据id类名绑定holder
    private HashMap<Integer, Class<? extends NormalRecyclerBaseHolder>> idToHolder = new HashMap<>();

    //没有数据的布局id
    private int noDataLayoutId = -1;

    private Object noDataObject;

    private Class<? extends NormalRecyclerBaseHolder> noDataHolder;

    private int loadmoreId = -1;

    private Object loadmoreObject;

    private Class<? extends NormalLoadMoreHolder> loadmoreHolder;

    private NormalBindDataChooseListener normalBindDataChooseListener;

    public NormalAdapterManager bind(Class modelClass, int layoutId, Class<? extends NormalRecyclerBaseHolder> holderClass) {

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
        }
        return this;
    }

    public NormalAdapterManager bindEmpty(Object noDataModel, int layoutId, Class<NormalRecyclerBaseHolder> holderClass) {
        noDataHolder = holderClass;
        noDataObject = noDataModel;
        noDataLayoutId = layoutId;
        return this;
    }

    public NormalAdapterManager bindLoadMore(Object loadMoreModel, int layoutId, Class<? extends NormalLoadMoreHolder> holderClass) {
        loadmoreId = layoutId;
        loadmoreHolder = holderClass;
        loadmoreObject = loadMoreModel;
        return this;
    }

    public NormalAdapterManager bingChooseListener(NormalBindDataChooseListener listener) {
        normalBindDataChooseListener = listener;
        return this;
    }


    HashMap<String, List<Integer>> getModelToId() {
        return modelToId;
    }

    HashMap<Integer, Class<? extends NormalRecyclerBaseHolder>> getIdToHolder() {
        return idToHolder;
    }


    int getNoDataLayoutId() {
        return noDataLayoutId;
    }

    Object getNoDataObject() {
        return noDataObject;
    }

    Class<? extends NormalRecyclerBaseHolder> getNoDataHolder() {
        return noDataHolder;
    }


    int getLoadMoreId() {
        return loadmoreId;
    }

    Object getLoadMoreObject() {
        return loadmoreObject;
    }

    Class<? extends NormalLoadMoreHolder> getLoadMoreHolder() {
        return loadmoreHolder;
    }

    boolean isShowNoData() {
        return noDataLayoutId != -1 && noDataHolder != null && noDataObject != null;
    }

    boolean isSupportLoadMore() {
        return loadmoreId != -1 && loadmoreHolder != null && loadmoreObject != null;
    }

    NormalBindDataChooseListener getNormalBindDataChooseListener() {
        return normalBindDataChooseListener;
    }

    NormalRecyclerBaseHolder getViewTypeHolder(Context context, ViewGroup parent, int viewType) {
        Class<? extends NormalRecyclerBaseHolder> idToHolder = getIdToHolder().get(viewType);

        Constructor object = null;
        try {
            View v = LayoutInflater.from(context).inflate(viewType, parent, false);
            object = idToHolder.getDeclaredConstructor(new Class[]{Context.class, View.class});
            object.setAccessible(true);
            return (NormalRecyclerBaseHolder) object.newInstance(context, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    NormalRecyclerBaseHolder getNoDataViewTypeHolder(Context context, ViewGroup parent) {
        Constructor object;
        try {
            View v = LayoutInflater.from(context).inflate(noDataLayoutId, parent, false);
            object = noDataHolder.getDeclaredConstructor(new Class[]{Context.class, View.class});
            object.setAccessible(true);
            return (NormalRecyclerBaseHolder) object.newInstance(context, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    NormalLoadMoreHolder getLoadMoreViewTypeHolder(Context context, ViewGroup parent) {
        Constructor object;
        try {
            View v = LayoutInflater.from(context).inflate(loadmoreId, parent, false);
            object = loadmoreHolder.getDeclaredConstructor(new Class[]{Context.class, View.class});
            object.setAccessible(true);
            return (NormalLoadMoreHolder) object.newInstance(context, v);
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
