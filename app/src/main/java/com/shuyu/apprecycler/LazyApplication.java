package com.shuyu.apprecycler;

import android.app.Application;

import com.shuyu.apprecycler.chat.utils.ChatConst;

/**
 * Created by guoshuyu on 2017/9/8.
 */

public class LazyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ChatConst.ChatInit(this);
    }

}
