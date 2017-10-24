package com.shuyu.apprecycler.chat.data.factory;

import android.support.annotation.NonNull;

import com.shuyu.apprecycler.chat.data.factory.vo.ChatMessageModel;
import com.shuyu.apprecycler.chat.data.factory.vo.ChatUserModel;
import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;
import com.shuyu.apprecycler.chat.data.model.ChatImageModel;
import com.shuyu.apprecycler.chat.data.model.ChatTextModel;
import com.shuyu.apprecycler.chat.data.model.UserModel;
import com.shuyu.apprecycler.chat.utils.ChatConst;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * realm 数据库
 * Created by guoshuyu on 2017/9/8.
 */

public class LocalChatBDRealmManager implements ILocalChatDBManager {

    private Realm mRealmDB;

    private LocalChatBDRealmManager() {

    }

    public static LocalChatBDRealmManager newInstance() {
        return new LocalChatBDRealmManager();
    }


    @Override
    public void saveChatMessage(final ChatBaseModel baseModel) {
        getRealmDB().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm bgRealm) {
                ChatMessageModel chatMessage = bgRealm.createObject(ChatMessageModel.class);
                RealmResults<ChatUserModel> userList = bgRealm.where(ChatUserModel.class).equalTo("userId", baseModel.getUserModel().getUserId()).findAll();
                ChatUserModel chatUser;
                if (userList != null && userList.size() > 0) {
                    chatUser = userList.get(0);
                } else {
                    chatUser = bgRealm.createObject(ChatUserModel.class, baseModel.getUserModel().getUserId());
                }
                cloneToChatMessageModel(chatUser, chatMessage, baseModel);
                if (baseModel instanceof ChatTextModel) {
                    ChatTextModel chatModel = (ChatTextModel) baseModel;
                    chatMessage.setContent(chatModel.getContent());
                } else if (baseModel instanceof ChatImageModel) {
                    ChatImageModel chatModel = (ChatImageModel) baseModel;
                    chatMessage.setContent(chatModel.getImgUrl());
                }
            }
        });
    }

    /**
     * TODO
     * 因为懒加载，所以realm没有分页改变
     * 所以需要对realmResults进行分页读取，读取完毕才可以close当前线程的realm
     */
    @Override
    public void getChatMessage(final String chatId, final int page, final ILocalChatDetailGetListener listener) {

        getRealm().subscribeOn(Schedulers.io())
                .map(new Function<Realm, List<ChatBaseModel>>() {
                    @Override
                    public List<ChatBaseModel> apply(@NonNull Realm realm) throws Exception {
                        RealmResults<ChatMessageModel> realmResults = realm.where(ChatMessageModel.class)
                                .equalTo("chatId", chatId)
                                .findAll().sort("createTime", Sort.DESCENDING);
                        List<ChatBaseModel> list;
                        if (realmResults == null) {
                            list = new ArrayList<>();
                        } else {
                            list = resolveMessageList(realmResults);
                        }
                        realm.close();
                        return list;

                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ChatBaseModel>>() {
                    @Override
                    public void accept(List<ChatBaseModel> chatBaseModels) throws Exception {
                        if (chatBaseModels != null && chatBaseModels.size() > 0) {
                            if (listener != null) {
                                listener.getData(chatBaseModels);
                            }
                        }
                    }
                });
    }

    @Override
    public void closeDB() {
        if (mRealmDB != null) {
            mRealmDB.close();
            mRealmDB = null;
        }
    }

    private List<ChatBaseModel> resolveMessageList(RealmResults<ChatMessageModel> chatMessageModels) {
        List<ChatBaseModel> list = new ArrayList<>();
        for (ChatMessageModel chatMessage : chatMessageModels) {
            switch (chatMessage.getType()) {
                case ChatConst.TYPE_TEXT: {
                    ChatTextModel chatText = new ChatTextModel();
                    chatText.setContent(chatMessage.getContent());
                    cloneChatBaseModel(chatText, chatMessage);
                    list.add(chatText);
                    break;
                }
                case ChatConst.TYPE_IMAGE: {
                    ChatImageModel chatImg = new ChatImageModel();
                    chatImg.setImgUrl(chatMessage.getContent());
                    cloneChatBaseModel(chatImg, chatMessage);
                    list.add(chatImg);
                    break;
                }
            }
        }
        return list;
    }

    private void cloneChatBaseModel(ChatBaseModel chatBase, ChatMessageModel chatMessage) {
        chatBase.setChatId(chatMessage.getChatId());
        chatBase.setMe(ChatConst.getDefaultUser().getUserId().equals(chatMessage.getUserModel().getUserId()));
        chatBase.setId(chatMessage.getId());
        chatBase.setChatType(chatMessage.getType());
        chatBase.setCreateTime(chatMessage.getCreateTime());
        UserModel user = new UserModel();
        user.setUserId(chatMessage.getUserModel().getUserId());
        user.setUserPic(chatMessage.getUserModel().getUserPic());
        user.setUserName(chatMessage.getUserModel().getUserName());
        chatBase.setUserModel(user);
    }

    private void cloneToChatMessageModel(ChatUserModel chatUser, ChatMessageModel message, ChatBaseModel chatBase) {
        message.setId(chatBase.getId());
        message.setChatId(chatBase.getChatId());
        message.setType(chatBase.getChatType());
        message.setCreateTime(chatBase.getCreateTime());
        chatUser.setUserName(chatBase.getUserModel().getUserName());
        chatUser.setUserPic(chatBase.getUserModel().getUserPic());
        message.setUserModel(chatUser);
    }

    private Realm getRealmDB() {
        if (mRealmDB == null) {
            mRealmDB = Realm.getDefaultInstance();
        }
        return mRealmDB;
    }


    private Observable<Realm> getRealm() {
        return Observable.create(new ObservableOnSubscribe<Realm>() {
            @Override
            public void subscribe(final ObservableEmitter<Realm> emitter)
                    throws Exception {
                final Realm observableRealm = Realm.getDefaultInstance();
                emitter.onNext(observableRealm);
                emitter.onComplete();
            }
        });
    }
}
