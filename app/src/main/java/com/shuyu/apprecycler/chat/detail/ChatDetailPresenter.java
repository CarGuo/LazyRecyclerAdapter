package com.shuyu.apprecycler.chat.detail;

import android.os.Handler;
import android.text.TextUtils;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.data.factory.ILocalChatDetailGetListener;
import com.shuyu.apprecycler.chat.data.factory.LocalChatDBFactory;
import com.shuyu.apprecycler.chat.data.factory.vo.ChatUserModel;
import com.shuyu.apprecycler.chat.data.model.ChatImageModel;
import com.shuyu.apprecycler.chat.data.model.UserModel;
import com.shuyu.apprecycler.chat.detail.view.ChatDetailBottomView;
import com.shuyu.apprecycler.chat.utils.ChatConst;
import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;
import com.shuyu.apprecycler.chat.data.model.ChatTextModel;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailSingleton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * 聊天想起处理逻辑
 * Created by guoshuyu on 2017/9/4.
 */

@ChatDetailSingleton
public class ChatDetailPresenter implements ChatDetailContract.IChatDetailPresenter<ChatBaseModel> {

    private List<ChatBaseModel> mDataList = new ArrayList<>();

    private List<ChatDetailBottomView.ChatDetailBottomMenuModel> mMenuList = new ArrayList<>();

    private ChatDetailContract.IChatDetailView mView;

    private Handler mHandler = new Handler();

    @Inject
    public ChatDetailPresenter(ChatDetailContract.IChatDetailView view) {
        this.mView = view;
        init();
    }

    @Override
    public void loadMoreData(int page) {

    }

    @Override
    public List<ChatBaseModel> getDataList() {
        return mDataList;
    }

    @Override
    public List getMenuList() {
        return mMenuList;
    }

    @Override
    public void sendMsg(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        //发送文本
        ChatTextModel textModel = new ChatTextModel();
        textModel.setContent(text);
        resolveBaseData(textModel, ChatConst.getDefaultUser(), ChatConst.TYPE_TEXT, true);
        resolveAutoReply(ChatConst.TYPE_TEXT);
    }

    @Override
    public void sendMenuItem(int position) {
        switch (position) {
            case 0: {
                //发送图片
                ChatImageModel chatImageModel = new ChatImageModel();
                chatImageModel.setImgUrl("http://osvlwlt4g.bkt.clouddn.com/17-9-6/50017724.jpg");
                resolveBaseData(chatImageModel, ChatConst.getDefaultUser(), ChatConst.TYPE_IMAGE, true);
                resolveAutoReply(ChatConst.TYPE_IMAGE);
                break;
            }
        }

    }

    @Override
    public void release() {
        LocalChatDBFactory.getChatDBManager().closeDB();
    }

    private void init() {
        //初始化底部menu
        mMenuList.add(new ChatDetailBottomView.ChatDetailBottomMenuModel("图片", R.mipmap.ic_launcher));
        //初始化读取本地数据库
        LocalChatDBFactory.getChatDBManager().getChatMessage(ChatConst.CHAT_ID, 0, new ILocalChatDetailGetListener() {
            @Override
            public void getData(List<ChatBaseModel> datList) {
                if (datList != null && datList.size() > 0) {
                    mDataList.addAll(datList);
                    mView.notifyView();
                }
            }
        });
    }

    /**
     * 获取图片
     */
    private void replyImgMsg() {
        ChatImageModel chatImageModel = new ChatImageModel();
        chatImageModel.setImgUrl("http://osvlwlt4g.bkt.clouddn.com/17-9-6/50017724.jpg");
        resolveBaseData(chatImageModel, ChatConst.getReplayUser(), ChatConst.TYPE_IMAGE, false);
    }

    /**
     * 回复文本
     */
    private void replyTextMsg() {
        ChatTextModel textModel = new ChatTextModel();
        textModel.setContent("我回复你啦，萌萌哒");
        resolveBaseData(textModel, ChatConst.getReplayUser(), ChatConst.TYPE_TEXT, false);
    }

    /**
     * 聊天基础数据同意处理
     */
    private void resolveBaseData(ChatBaseModel chatBaseModel, UserModel chatUserModel, int type, boolean isMe) {
        chatBaseModel.setChatId(ChatConst.CHAT_ID);
        chatBaseModel.setChatType(type);
        chatBaseModel.setId(UUID.randomUUID().toString());
        chatBaseModel.setMe(isMe);
        chatBaseModel.setCreateTime(new Date().getTime());
        chatBaseModel.setUserModel(chatUserModel);
        mDataList.add(0, chatBaseModel);
        mView.sendSuccess();
        LocalChatDBFactory.getChatDBManager().saveChatMessage(chatBaseModel);
    }

    /**
     * 自动回复
     */
    private void resolveAutoReply(final int type) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (type) {
                    case ChatConst.TYPE_TEXT:
                        replyTextMsg();
                        break;
                    case ChatConst.TYPE_IMAGE:
                        replyImgMsg();
                        break;
                }
            }
        }, 500);
    }

}
