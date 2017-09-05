package com.shuyu.apprecycler.chat.data.model;

import java.io.Serializable;

/**
 * 文本model
 * Created by guoshuyu on 2017/9/4.
 */

public class ChatTextModel extends ChatBaseModel implements Serializable {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
