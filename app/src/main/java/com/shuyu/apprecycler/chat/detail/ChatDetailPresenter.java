package com.shuyu.apprecycler.chat.detail;

import android.os.Handler;
import android.text.TextUtils;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.data.factory.LocalChatDetailLogic;
import com.shuyu.apprecycler.chat.data.model.ChatImageModel;
import com.shuyu.apprecycler.chat.detail.view.ChatDetailBottomView;
import com.shuyu.apprecycler.chat.utils.ChatConst;
import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;
import com.shuyu.apprecycler.chat.data.model.ChatTextModel;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.Realm;

/**
 * Created by guoshuyu on 2017/9/4.
 */

@ChatDetailSingleton
public class ChatDetailPresenter implements ChatDetailContract.IChatDetailPresenter<ChatBaseModel> {

    private List<ChatBaseModel> mDataList = new ArrayList<>();

    private List<ChatDetailBottomView.ChatDetailBottomMenuModel> mMenuList = new ArrayList<>();

    private ChatDetailContract.IChatDetailView mView;

    private Handler mHandler = new Handler();

    private Realm mRealm;

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
        ChatTextModel textModel = new ChatTextModel();
        textModel.setContent(text);
        textModel.setChatId(ChatConst.CHAT_ID);
        textModel.setChatType(ChatConst.TYPE_TEXT);
        textModel.setId(UUID.randomUUID().toString());
        textModel.setMe(true);
        textModel.setUserModel(ChatConst.getDefaultUser());
        mDataList.add(0, textModel);
        LocalChatDetailLogic.saveChatMessage(mRealm, textModel);
        mView.sendSuccess();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                replyTextMsg();
            }
        }, 500);
    }

    @Override
    public void sendMenuItem(int position) {
        switch (position) {
            case 0: {
                ChatImageModel chatImageModel = new ChatImageModel();
                chatImageModel.setImgUrl("http://osvlwlt4g.bkt.clouddn.com/17-9-6/50017724.jpg");
                chatImageModel.setChatId(ChatConst.CHAT_ID);
                chatImageModel.setChatType(ChatConst.TYPE_IMAGE);
                chatImageModel.setId(UUID.randomUUID().toString());
                chatImageModel.setMe(true);
                chatImageModel.setUserModel(ChatConst.getDefaultUser());
                mDataList.add(0, chatImageModel);
                LocalChatDetailLogic.saveChatMessage(mRealm, chatImageModel);
                mView.notifyView();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        replyImgMsg();
                    }
                }, 500);
                break;
            }
        }

    }

    @Override
    public void release() {
        mRealm.close();
    }

    private void init() {
        mMenuList.add(new ChatDetailBottomView.ChatDetailBottomMenuModel("图片", R.mipmap.ic_launcher));
        mRealm = Realm.getDefaultInstance();

        LocalChatDetailLogic.getChatDetail(mRealm, ChatConst.CHAT_ID, 0);
    }

    private void replyImgMsg() {
        ChatImageModel chatImageModel = new ChatImageModel();
        chatImageModel.setImgUrl("http://osvlwlt4g.bkt.clouddn.com/17-9-6/50017724.jpg");
        chatImageModel.setChatId(ChatConst.CHAT_ID);
        chatImageModel.setChatType(ChatConst.TYPE_IMAGE);
        chatImageModel.setId(UUID.randomUUID().toString());
        chatImageModel.setMe(false);
        chatImageModel.setUserModel(ChatConst.getReplayUser());
        mDataList.add(0, chatImageModel);
        LocalChatDetailLogic.saveChatMessage(mRealm, chatImageModel);
        mView.notifyView();
    }

    private void replyTextMsg() {
        ChatTextModel textModel = new ChatTextModel();
        textModel.setContent("我回复你啦，萌萌哒");
        textModel.setChatId(ChatConst.CHAT_ID);
        textModel.setChatType(ChatConst.TYPE_TEXT);
        textModel.setId(UUID.randomUUID().toString());
        textModel.setMe(false);
        textModel.setUserModel(ChatConst.getReplayUser());
        mDataList.add(0, textModel);
        LocalChatDetailLogic.saveChatMessage(mRealm, textModel);
        mView.notifyView();
    }

}
