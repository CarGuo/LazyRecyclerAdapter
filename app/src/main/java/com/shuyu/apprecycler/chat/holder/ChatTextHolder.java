package com.shuyu.apprecycler.chat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * 聊天文本
 * Created by guoshuyu on 2017/9/4.
 */

public class ChatTextHolder extends BindRecyclerBaseHolder {

    private ImageView mChatDetailHolderAvatar;
    private TextView mChatDetailHolderImage;

    public ChatTextHolder(View v) {
        super(v);
    }

    @Override
    public void createView(View v) {
        mChatDetailHolderAvatar = (ImageView) v.findViewById(R.id.chat_detail_holder_avatar);
        mChatDetailHolderImage = (TextView) v.findViewById(R.id.chat_detail_holder_image);
    }

    @Override
    public void onBind(Object model, int position) {

    }
}
