package com.shuyu.apprecycler.chat.data.factory.vo;


import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.LinkingObjects;

/**
 * Created by guoshuyu on 2017/9/8.
 */

public class ChatMessageModel extends RealmObject {

    private String id;

    private String chatId;

    private String content;

    private ChatUserModel userModel;

    private int type;

    private long createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ChatUserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(ChatUserModel userModel) {
        this.userModel = userModel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
