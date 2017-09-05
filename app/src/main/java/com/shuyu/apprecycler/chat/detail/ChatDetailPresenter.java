package com.shuyu.apprecycler.chat.detail;

import com.shuyu.apprecycler.chat.data.ChatConst;
import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;
import com.shuyu.apprecycler.chat.data.model.ChatTextModel;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailSingleton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

/**
 * Created by guoshuyu on 2017/9/4.
 */

@ChatDetailSingleton
public class ChatDetailPresenter implements ChatDetailContract.IChatDetailPresenter<ChatBaseModel> {

    private List<ChatBaseModel> mDataList = new ArrayList<>();

    private ChatDetailContract.IChatDetailView mView;


    @Inject
    public ChatDetailPresenter(ChatDetailContract.IChatDetailView view) {
        this.mView = view;
    }

    @Override
    public void loadMoreData(int page) {

    }

    @Override
    public List<ChatBaseModel> getDataList() {
        return mDataList;
    }

    @Override
    public void sendMsg(String text) {
        ChatTextModel textModel = new ChatTextModel();
        textModel.setContent(text);
        textModel.setChatId("ALKSJDFLKSDJFLAKSDJFLKASDJF");
        textModel.setChatType(ChatConst.TYPE_TEXT);
        textModel.setId(UUID.randomUUID().toString());
        textModel.setMe(true);
        mDataList.add(0, textModel);
        mView.sendSuccess();
    }

}
