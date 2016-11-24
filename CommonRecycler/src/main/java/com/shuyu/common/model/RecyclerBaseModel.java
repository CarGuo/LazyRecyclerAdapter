package com.shuyu.common.model;


import java.io.Serializable;

/**
 * Created by Guo on 2015/11/23.
 * 数据model必须继承
 */
public class RecyclerBaseModel implements Serializable {

    private Object extraObject; //设置一些你需要的数据

    private int resLayoutId = -1; //必须设置的，布局ID

    private int extraTag;//一些其他的标志位

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
