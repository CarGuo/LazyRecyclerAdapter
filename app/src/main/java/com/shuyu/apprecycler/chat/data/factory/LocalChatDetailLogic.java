package com.shuyu.apprecycler.chat.data.factory;

import com.shuyu.apprecycler.chat.data.factory.vo.ChatMessageModel;

import io.reactivex.BackpressureStrategy;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by guoshuyu on 2017/9/8.
 */

public class LocalChatDetailLogic {

    public void getChatDetail(Realm realm, final String chatId, final int page) {

        /*RealmResults<ChatMessageModel> result = realm.where(ChatMessageModel.class)
                .equalTo("chatId", chatId)
                .findAllAsync();
        //懒加载，读取对象才使用，所以无需分页
        result.addChangeListener(new RealmChangeListener<RealmResults<ChatMessageModel>>() {
            @Override
            public void onChange(RealmResults<ChatMessageModel> chatMessageModels) {

            }
        });

        realm.where(ChatMessageModel.class).equalTo("chatId", chatId).findAllAsync().asObservable()
                .filter(chatMessageModels.isLoaded)
                .flatMap(persons -> Observable.from(persons))
                .flatMap(person -> api.user(person.getGithubUserName())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> showUser(user));*/
        getRealm(realm);
    }


    private io.reactivex.Flowable<Realm> getRealm(final Realm realm) {
        return io.reactivex.Flowable.create(new FlowableOnSubscribe<Realm>() {
            @Override
            public void subscribe(final FlowableEmitter<Realm> emitter)
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
        }, BackpressureStrategy.LATEST);
    }
}
