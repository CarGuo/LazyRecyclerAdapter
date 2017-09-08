package com.shuyu.apprecycler.chat.data.factory;

import android.util.Log;

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
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposables;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by guoshuyu on 2017/9/8.
 */

public class LocalChatDetailLogic {


    public static void saveChatMessage(final Realm realm, final ChatBaseModel baseModel) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
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

    public static void getChatDetail(Realm realm, final String chatId, final int page) {

        getRealm(realm)
                .subscribeOn(Schedulers.io())
                .map(new Function<Realm, RealmResults<ChatMessageModel>>() {
                    @Override
                    public RealmResults<ChatMessageModel> apply(@NonNull Realm realm) throws Exception {
                        return realm.where(ChatMessageModel.class)
                                .equalTo("chatId", chatId)
                                .findAll();
                    }
                })
                .map(new Function<RealmResults<ChatMessageModel>, List<ChatBaseModel>>() {
                    @Override
                    public List<ChatBaseModel> apply(@NonNull RealmResults<ChatMessageModel> chatMessageModels) throws Exception {
                        if (chatMessageModels == null) {
                            return new ArrayList<>();
                        }
                        return resolveMessageList(chatMessageModels);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ChatBaseModel>>() {
                    @Override
                    public void accept(List<ChatBaseModel> chatBaseModels) throws Exception {
                        Log.e("ffffff", "adsfasdfasdf " + chatBaseModels.size());
                    }
                });
    }

    private static List<ChatBaseModel> resolveMessageList(RealmResults<ChatMessageModel> chatMessageModels) {
        List<ChatBaseModel> list = new ArrayList<>();
        for (ChatMessageModel chatMessage : chatMessageModels) {
            switch (chatMessage.getType()) {
                case ChatConst.TYPE_TEXT: {
                    ChatTextModel chatText = new ChatTextModel();
                    chatText.setContent(chatMessage.getContent());
                    cloneChatBaseModel(chatText, chatMessage);
                    break;
                }
                case ChatConst.TYPE_IMAGE: {
                    ChatImageModel chatImg = new ChatImageModel();
                    chatImg.setImgUrl(chatMessage.getContent());
                    cloneChatBaseModel(chatImg, chatMessage);
                    break;
                }
            }
        }
        return list;
    }

    private static void cloneChatBaseModel(ChatBaseModel chatBase, ChatMessageModel chatMessage) {
        chatBase.setChatId(chatMessage.getChatId());
        chatBase.setMe(ChatConst.getDefaultUser().getUserId().equals(chatMessage.getUserModel().getUserId()));
        chatBase.setId(chatMessage.getId());
        chatBase.setChatType(chatMessage.getType());
        UserModel user = new UserModel();
        user.setUserId(chatMessage.getUserModel().getUserId());
        user.setUserPic(chatMessage.getUserModel().getUserPic());
        user.setUserName(chatMessage.getUserModel().getUserName());
        chatBase.setUserModel(user);
    }

    private static void cloneToChatMessageModel(ChatUserModel chatUser, ChatMessageModel message, ChatBaseModel chatBase) {
        message.setId(chatBase.getId());
        message.setChatId(chatBase.getChatId());
        message.setType(chatBase.getChatType());
        chatUser.setUserName(chatBase.getUserModel().getUserName());
        chatUser.setUserPic(chatBase.getUserModel().getUserPic());
        message.setUserModel(chatUser);
    }

    private static Observable<Realm> getRealm(final Realm realm) {
        return Observable.create(new ObservableOnSubscribe<Realm>() {
            @Override
            public void subscribe(final ObservableEmitter<Realm> emitter)
                    throws Exception {
                RealmConfiguration realmConfiguration = realm.getConfiguration();
                final Realm observableRealm = Realm.getInstance(realmConfiguration);
                final RealmChangeListener<Realm> listener = new RealmChangeListener<Realm>() {
                    @Override
                    public void onChange(Realm realm) {
                        emitter.onNext(realm);
                    }
                };
                emitter.setDisposable(Disposables.fromRunnable(new Runnable() {
                    @Override
                    public void run() {
                        observableRealm.removeChangeListener(listener);
                        observableRealm.close();
                    }
                }));
                observableRealm.addChangeListener(listener);
                emitter.onNext(observableRealm);
            }
        });
    }
}
