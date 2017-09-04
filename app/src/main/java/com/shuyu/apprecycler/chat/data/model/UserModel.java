package com.shuyu.apprecycler.chat.data.model;

import java.io.Serializable;

/**
 * 用户数据
 * Created by guoshuyu on 2017/9/4.
 */

public class UserModel implements Serializable {
    private int userId;
    private int userName;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserName() {
        return userName;
    }

    public void setUserName(int userName) {
        this.userName = userName;
    }
}
