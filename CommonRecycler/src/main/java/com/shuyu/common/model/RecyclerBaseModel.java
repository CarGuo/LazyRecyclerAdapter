package com.shuyu.common.model;


import java.io.Serializable;

/**
 * Created by Guo on 2015/11/23.
 */
public class RecyclerBaseModel implements Serializable {

    private Object extraObject;

    private int resLayoutId = -1;

    private int extraTag;

    public int getResLayoutId() {
        return resLayoutId;
    }

    public void setResLayoutId(int resLayoutId) {
        this.resLayoutId = resLayoutId;
    }

    public int getExtraTag() {
        return extraTag;
    }

    public void setExtraTag(int extraTag) {
        this.extraTag = extraTag;
    }

    public Object getExtraObject() {
        return extraObject;
    }

    public void setExtraObject(Object extraObject) {
        this.extraObject = extraObject;
    }
}
