package com.shuyu.apprecycler.chat.data.model;

import java.io.Serializable;

/**
 * 用户数据
 * Created by guoshuyu on 2017/9/4.
 */

public class UserModel implements Serializable {

    private String userId;

    private String userName;

    private String userPic;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }
}
