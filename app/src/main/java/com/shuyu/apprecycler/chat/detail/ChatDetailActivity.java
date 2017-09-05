package com.shuyu.apprecycler.chat.detail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailAdapter;
import com.shuyu.apprecycler.chat.detail.dagger.ChatSuperAdapterManager;
import com.shuyu.apprecycler.chat.detail.dagger.component.DaggerChatDetailComponent;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailPresenterModule;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.listener.OnLoadingListener;
import com.shuyu.textutillib.RichEditText;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 聊天详情
 * Created by guoshuyu on 2017/9/4.
 */

public class ChatDetailActivity extends AppCompatActivity implements ChatDetailContract.IChatDetailView {

    @BindView(R.id.chat_detail_activity_toolbar)
    Toolbar mChatDetailActivityToolbar;
    @BindView(R.id.chat_detail_activity_recycler)
    RecyclerView mChatDetailActivityRecycler;
    @BindView(R.id.chat_detail_activity_edit)
    RichEditText mChatDetailActivityEdit;
    @BindView(R.id.chat_detail_activity_send)
    TextView mChatDetailActivitySend;

    @Inject
    ChatDetailPresenter mPresenter;

    @Inject
    ChatSuperAdapterManager mNormalAdapterManager;

    @Inject
    ChatDetailAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        ButterKnife.bind(this);
        initTitle();
        initActivity();
        initListener();
    }


    private void initActivity() {
        DaggerChatDetailComponent.builder()
                .chatDetailPresenterModule(new ChatDetailPresenterModule(this))
                .build()
                .inject(this);

    }

    private void initTitle() {
        mChatDetailActivityToolbar.setTitle("聊天详情");
        mChatDetailActivityToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mChatDetailActivityToolbar);
    }

    private void initListener() {
        mNormalAdapterManager
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Context context, int position) {
                    }
                })
                .setLoadingListener(new OnLoadingListener() {
                    @Override
                    public void onRefresh() {
                    }

                    @Override
                    public void onLoadMore() {
                    }
                });
        mChatDetailActivityRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mChatDetailActivityRecycler.setAdapter(mAdapter);
    }

    @OnClick(R.id.chat_detail_activity_send)
    public void onViewClicked() {
        mPresenter.sendMsg(mChatDetailActivityEdit.getText().toString());
    }


    @Override
    public Context getContext() {
        return this;
    }

    //TODO 用diff判断
    @Override
    public void notifyView() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void sendSuccess() {
        mChatDetailActivityEdit.setText("");
        mAdapter.notifyDataSetChanged();
    }

}
