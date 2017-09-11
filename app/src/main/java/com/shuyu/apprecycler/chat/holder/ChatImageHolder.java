package com.shuyu.apprecycler.chat.holder;

import android.view.View;
import android.widget.ImageView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.data.model.ChatImageModel;
import com.shuyu.apprecycler.chat.utils.img.ImageLoaderManager;
import com.shuyu.apprecycler.chat.utils.img.LoadOption;

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
        ImageLoaderManager.getImageLoaderManager().loadImage(context,
                new LoadOption()
                        .setCircleCrop(true)
                        .setPlaceholderRes(R.drawable.a2)
                        .setErrorRes(R.drawable.a1)
                        .setImageView(mChatDetailHolderImage)
                        .setUrl(chatImageModel.getImgUrl()));
    }
}
