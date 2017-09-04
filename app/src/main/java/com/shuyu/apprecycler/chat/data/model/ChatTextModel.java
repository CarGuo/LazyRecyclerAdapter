package com.shuyu.apprecycler.chat.data.model;

import java.io.Serializable;

/**
 * 文本model
 * Created by guoshuyu on 2017/9/4.
 */

public class ChatTextModel extends ChatBaseModel implements Serializable {

    private UserModel userModel;

    private String content;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
