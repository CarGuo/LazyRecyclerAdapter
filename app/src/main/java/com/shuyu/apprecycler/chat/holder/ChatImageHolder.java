package com.shuyu.apprecycler.chat.holder;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.data.model.ChatImageModel;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * 聊天图片
 * Created by guoshuyu on 2017/9/4.
 */

public class ChatImageHolder extends ChatBaseHolder {

    private ImageView mChatDetailHolderImage;

    public ChatImageHolder(View v) {
        super(v);
    }

    @Override
    public void createView(View v) {
        super.createView(v);
        mChatDetailHolderImage = (ImageView) v.findViewById(R.id.chat_detail_holder_image);
    }

    @Override
    public void onBind(Object model, int position) {
        super.onBind(model, position);
        ChatImageModel chatImageModel = (ChatImageModel) model;
        Glide.with(context.getApplicationContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.a2)
                        .error(R.drawable.a1)
                ).load(chatImageModel.getImgUrl()).into(mChatDetailHolderImage);
    }
}
