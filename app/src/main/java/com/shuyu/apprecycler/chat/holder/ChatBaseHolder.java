package com.shuyu.apprecycler.chat.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;
import com.shuyu.apprecycler.chat.utils.img.ImageLoaderManager;
import com.shuyu.apprecycler.chat.utils.img.LoadOption;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * holder基类，处理头像加载和姓名显示
 * Created by guoshuyu on 2017/9/5.
 */

public abstract class ChatBaseHolder extends BindRecyclerBaseHolder {


    private ImageView mChatDetailHolderAvatar;
    private TextView mChatDetailHolderName;

    public ChatBaseHolder(View v) {
        super(v);
    }

    @Override
    public void createView(View v) {
        mChatDetailHolderName = (TextView) v.findViewById(R.id.chat_detail_holder_name);
        mChatDetailHolderAvatar = (ImageView) v.findViewById(R.id.chat_detail_holder_avatar);
    }

    @Override
    public void onBind(Object model, int position) {
        ChatBaseModel chatBaseModel = (ChatBaseModel) model;
        mChatDetailHolderName.setText(chatBaseModel.getUserModel().getUserName());
        ImageLoaderManager.getImageLoaderManager().loadImage(context,
                new LoadOption()
                        .setCircleCrop(true)
                        .setPlaceholderRes(R.drawable.a2)
                        .setErrorRes(R.drawable.a1)
                        .setImageView(mChatDetailHolderAvatar)
                        .setUrl(chatBaseModel.getUserModel().getUserPic()));
    }

}
