package com.shuyu.apprecycler.chat.data.factory;

import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;

import java.util.List;

import io.reactivex.Observable;

/**
 * 本地数据库接口
 * Created by guoshuyu on 2017/9/11.
 */

public interface ILocalChatDBManager {

    void saveChatMessage(final ChatBaseModel baseModel);

    void getChatMessage(final String chatId, final int page, final ILocalChatDetailGetListener listener);

    void closeDB();
}
