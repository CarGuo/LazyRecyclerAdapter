package com.shuyu.apprecycler.chat.detail.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.bind.BindRecyclerBaseHolder;
import com.shuyu.bind.BindSuperAdapter;
import com.shuyu.bind.BindSuperAdapterManager;
import com.shuyu.bind.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by guoshuyu on 2017/9/7.
 */

public class ChatDetailBottomView extends LinearLayout {

    @BindView(R.id.chat_detail_bottom_recycler)
    RecyclerView mChatDetailBottomRecycler;

    BindSuperAdapter mBindSuperAdapter;

    List<ChatDetailBottomMenuModel> mDataList = new ArrayList<>();

    OnItemClickListener mClickListener;

    public ChatDetailBottomView(Context context) {
        super(context);
        init();
    }

    public ChatDetailBottomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatDetailBottomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.chat_detail_bottom_view, this);
        ButterKnife.bind(this);

        BindSuperAdapterManager bindSuperAdapterManager = new BindSuperAdapterManager();
        bindSuperAdapterManager
                .setPullRefreshEnabled(false)
                .setLoadingMoreEnabled(false)
                .bind(ChatDetailBottomMenuModel.class, R.layout.chat_detail_bottom_menu_item, ChatDetailBottomMenuHolder.class)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Context context, int position) {
                        if (mClickListener != null) {
                            mClickListener.onItemClick(context, position);
                        }
                    }
                });
        mBindSuperAdapter = new BindSuperAdapter(getContext(), bindSuperAdapterManager, mDataList);
        mChatDetailBottomRecycler.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mChatDetailBottomRecycler.setAdapter(mBindSuperAdapter);
    }

    public static class ChatDetailBottomMenuHolder extends BindRecyclerBaseHolder {

        @BindView(R.id.chat_detail_bottom_menu_item_img)
        ImageView mChatDetailBottomMenuItemImg;
        @BindView(R.id.chat_detail_bottom_menu_item_txt)
        TextView mChatDetailBottomMenuItemTxt;

        public ChatDetailBottomMenuHolder(View v) {
            super(v);
        }

        @Override
        public void createView(View v) {
            ButterKnife.bind(this, v);
        }

        @Override
        public void onBind(Object model, int position) {
            ChatDetailBottomMenuModel chatDetailBottomMenuModel = (ChatDetailBottomMenuModel) model;
            mChatDetailBottomMenuItemImg.setImageResource(chatDetailBottomMenuModel.getMenuRes());
            mChatDetailBottomMenuItemTxt.setText(chatDetailBottomMenuModel.getMenuName());

        }
    }

    public static class ChatDetailBottomMenuModel {
        private String menuName;
        private int menuRes;

        public ChatDetailBottomMenuModel() {

        }

        public ChatDetailBottomMenuModel(String name, int res) {
            menuName = name;
            menuRes = res;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public int getMenuRes() {
            return menuRes;
        }

        public void setMenuRes(int menuRes) {
            this.menuRes = menuRes;
        }
    }


    public void setDataList(List<ChatDetailBottomMenuModel> dataList) {
        if (dataList != null) {
            this.mDataList.addAll(dataList);
            mBindSuperAdapter.notifyDataSetChanged();
        }
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.mClickListener = clickListener;
    }
}
