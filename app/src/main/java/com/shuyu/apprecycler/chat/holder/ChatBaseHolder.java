package com.shuyu.apprecycler.chat.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.data.model.ChatBaseModel;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
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
        Glide.with(context.getApplicationContext())
                .setDefaultRequestOptions(new RequestOptions()
                        .centerCrop()
                        .circleCrop()
                        .placeholder(R.drawable.a2)
                        .error(R.drawable.a1)
                )
                .load(chatBaseModel.getUserModel().getUserPic()).into(mChatDetailHolderAvatar);
    }

}
