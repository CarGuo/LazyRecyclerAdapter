package com.shuyu.apprecycler.chat.data.factory;

/**
 * 数据库工厂对象
 * Created by guoshuyu on 2017/9/11.
 */

public class LocalChatDBFactory {

    public static ILocalChatDBManager getChatDBManager() {
        //需要替换数据库框架，只需要替换这个实例化即可
        return LocalChatBDRealmManager.newInstance();
    }
}
