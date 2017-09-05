package com.shuyu.apprecycler.chat.data.model;

import java.io.Serializable;

/**
 * 基础数据model
 * Created by guoshuyu on 2017/9/4.
 */

public abstract class ChatBaseModel implements Serializable {

    private int chatId;

    private int chatType;

    private int id;

    private boolean isMe;

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }
}
