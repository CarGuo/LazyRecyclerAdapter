package com.shuyu.apprecycler.chat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.data.model.ChatTextModel;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * 聊天文本
 * Created by guoshuyu on 2017/9/4.
 */

public class ChatTextHolder extends BindRecyclerBaseHolder {

    private ImageView mChatDetailHolderAvatar;
    private TextView mChatDetailHolderText;

    public ChatTextHolder(View v) {
        super(v);
    }

    @Override
    public void createView(View v) {
        mChatDetailHolderAvatar = (ImageView) v.findViewById(R.id.chat_detail_holder_avatar);
        mChatDetailHolderText = (TextView) v.findViewById(R.id.chat_detail_holder_image);
    }

    @Override
    public void onBind(Object model, int position) {
        ChatTextModel chatTextModel = (ChatTextModel) model;
        mChatDetailHolderText.setText(chatTextModel.getContent());
    }
}
