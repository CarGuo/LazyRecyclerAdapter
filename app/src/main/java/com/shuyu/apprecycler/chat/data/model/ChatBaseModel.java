package com.shuyu.apprecycler.chat.data.model;

import java.io.Serializable;

/**
 * 基础数据model
 * Created by guoshuyu on 2017/9/4.
 */

public abstract class ChatBaseModel implements Serializable {

    private String chatId;

    private int chatType;

    private String id;

    private boolean isMe;

    private UserModel userModel;

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
