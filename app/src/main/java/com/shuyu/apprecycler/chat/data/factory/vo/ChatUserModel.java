package com.shuyu.apprecycler.chat.data.factory.vo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by guoshuyu on 2017/9/8.
 */

public class ChatUserModel extends RealmObject {

    @PrimaryKey
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
