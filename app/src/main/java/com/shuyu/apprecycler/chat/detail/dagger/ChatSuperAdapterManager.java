package com.shuyu.apprecycler.chat.detail.dagger;

import android.content.Context;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.view.BindCustomLoadMoreFooter;
import com.shuyu.apprecycler.chat.data.model.ChatImageModel;
import com.shuyu.apprecycler.chat.data.model.ChatTextModel;
import com.shuyu.apprecycler.chat.holder.ChatImageHolder;
import com.shuyu.apprecycler.chat.holder.ChatTextHolder;
import com.shuyu.bind.BindSuperAdapterManager;
import com.shuyu.bind.listener.OnBindDataChooseListener;

import java.util.List;

import javax.inject.Inject;

/**
 * 继承之后实现注入与初始化
 * Created by guoshuyu on 2017/9/5.
 */

@ChatDetailSingleton
public class ChatSuperAdapterManager extends BindSuperAdapterManager {

    @Inject
    public ChatSuperAdapterManager() {
        super();
    }

    @Inject
    public void initManager(Context context) {
        bind(ChatImageModel.class, R.layout.chat_layout_image_left, ChatImageHolder.class)
                .bind(ChatImageModel.class, R.layout.chat_layout_image_right, ChatImageHolder.class)
                .bind(ChatTextModel.class, R.layout.chat_layout_text_left, ChatTextHolder.class)
                .bind(ChatTextModel.class, R.layout.chat_layout_text_right, ChatTextHolder.class)
                .bingChooseListener(new OnBindDataChooseListener() {
                    @Override
                    public int getCurrentDataLayoutId(Object object, Class classType, int position, List<Integer> ids) {
                        if (object instanceof ChatTextModel) {
                            ChatTextModel chatTextModel = (ChatTextModel) object;
                            return (chatTextModel.isMe()) ? R.layout.chat_layout_text_right : R.layout.chat_layout_text_left;
                        } else if (object instanceof ChatImageModel) {
                            ChatImageModel chatImageModel = (ChatImageModel) object;
                            return (chatImageModel.isMe()) ? R.layout.chat_layout_image_right : R.layout.chat_layout_image_left;
                        }
                        return ids.get(ids.size() - 1);
                    }
                })
                .setPullRefreshEnabled(false)
                .setLoadingMoreEnabled(false)
                .setFootView(new BindCustomLoadMoreFooter(context))
                .setNeedAnimation(true);
    }

}
