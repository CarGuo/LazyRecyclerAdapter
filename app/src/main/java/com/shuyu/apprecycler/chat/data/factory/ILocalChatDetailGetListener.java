package com.shuyu.apprecycler.chat.data.factory;

import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;

import java.util.List;

/**
 * 获取本地聊天数据毁掉
 * Created by guoshuyu on 2017/9/11.
 */

public interface ILocalChatDetailGetListener {
    void getData(List<ChatBaseModel> datList);
}
