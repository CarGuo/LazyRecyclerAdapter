package com.shuyu.apprecycler.chat.data.factory;

/**
 * 数据库工厂对象
 * Created by guoshuyu on 2017/9/11.
 */

public class LocalChatDBFactory {

    public static ILocalChatDBManager getChatDBManager() {
        return LocalChatBDRealmManager.newInstance();
    }
}
