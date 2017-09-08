package com.shuyu.apprecycler.chat.utils;

import android.content.Context;

import com.shuyu.apprecycler.chat.data.model.UserModel;
import com.shuyu.apprecycler.chat.detail.view.ChatDetailEmojiLayout;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by guoshuyu on 2017/9/5.
 */

public class ChatConst {
    public final static int TYPE_TEXT = 1;
    public final static int TYPE_IMAGE = 2;

    public final static String CHAT_ID = "2017-0101010aabbccjjekdj";

    private static UserModel sDefaultUser = null;
    private static UserModel sReplayUser = null;

    public static void ChatInit(Context context) {
        ChatDetailEmojiLayout.initEmoji(context);
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm.deleteRealm(config);
        Realm.setDefaultConfiguration(config);
    }

    public static UserModel getDefaultUser() {
        if (sDefaultUser == null) {
            sDefaultUser = new UserModel();
            sDefaultUser.setUserId("test_user");
            sDefaultUser.setUserName("大喵");
            sDefaultUser.setUserPic("http://img1.imgtn.bdimg.com/it/u=2165802874,1472309307&fm=214&gp=0.jpg");
        }
        return sDefaultUser;
    }

    public static UserModel getReplayUser() {
        if (sReplayUser == null) {
            sReplayUser = new UserModel();
            sReplayUser.setUserId("replay_user");
            sReplayUser.setUserName("二喵");
            sReplayUser.setUserPic("http://osvlwlt4g.bkt.clouddn.com/17-9-6/50017724.jpg");
        }
        return sReplayUser;
    }

}
