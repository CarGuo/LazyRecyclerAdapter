package com.shuyu.apprecycler.chat.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * Created by guoshuyu on 2017/9/5.
 */

public abstract class ChatBaseHolder extends BindRecyclerBaseHolder {


    private ImageView mChatDetailHolderAvatar;
    private TextView mChatDetailHolderName;
    private ViewGroup mChatDetailContentLayout;

    public ChatBaseHolder(View v) {
        super(v);
    }

    @Override
    public void createView(View v) {
        mChatDetailHolderName = (TextView) v.findViewById(R.id.chat_detail_holder_name);
        mChatDetailHolderAvatar = (ImageView) v.findViewById(R.id.chat_detail_holder_avatar);
        mChatDetailContentLayout = (ViewGroup) v.findViewById(R.id.chat_detail_holder_layout);
    }

    @Override
    public void onBind(Object model, int position) {
        ChatBaseModel chatBaseModel = (ChatBaseModel) model;
        //mChatDetailHolderName.setText(chatBaseModel.getUserModel().getUserName());
        //mChatDetailHolderAvatar.setImageResource(chatBaseModel.getUserModel().get());
    }

}
