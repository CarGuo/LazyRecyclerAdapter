package com.shuyu.apprecycler.chat.detail;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.chat.detail.dagger.ChatDetailAdapter;
import com.shuyu.apprecycler.chat.detail.dagger.ChatSuperAdapterManager;
import com.shuyu.apprecycler.chat.detail.dagger.component.DaggerChatDetailComponent;
import com.shuyu.apprecycler.chat.detail.dagger.module.ChatDetailPresenterModule;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.listener.OnLoadingListener;
import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.KeyBoardLockLayout;
import com.shuyu.textutillib.RichEditText;
import com.shuyu.textutillib.SmileUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;

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
    @BindView(R.id.chat_detail_activity_keyboard_layout)
    KeyBoardLockLayout mChatDetailActivityKeyboardLayout;
    @BindView(R.id.chat_detail_activity_send_emojiLayout)
    EmojiLayout mChatDetailActivitySendEmojiLayout;
    @BindView(R.id.chat_detail_activity_emoji_btn)
    ImageView mChatDetailActivityEmojiBtn;

    @Inject
    ChatDetailPresenter mPresenter;

    @Inject
    ChatSuperAdapterManager mNormalAdapterManager;

    @Inject
    ChatDetailAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEmoji();
        setContentView(R.layout.activity_chat_detail);
        ButterKnife.bind(this);
        initTitle();
        initActivity();
        initListener();
        initEmoji();
    }

    private void initTitle() {
        mChatDetailActivityToolbar.setTitle("聊天详情");
        mChatDetailActivityToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mChatDetailActivityToolbar);
    }

    private void initActivity() {
        DaggerChatDetailComponent.builder()
                .chatDetailPresenterModule(new ChatDetailPresenterModule(this))
                .build()
                .inject(this);

        mChatDetailActivityKeyboardLayout.setBottomView(mChatDetailActivitySendEmojiLayout);
        mChatDetailActivitySendEmojiLayout.setEditTextSmile(mChatDetailActivityEdit);

    }

    /**
     * 处理表情,在控件加载之前初始化
     */
    private void initEmoji() {
        List<Integer> data = new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (int i = 1; i < 64; i++) {
            int resId = getResources().getIdentifier("e" + i, "drawable", getPackageName());
            data.add(resId);
            strings.add("[e" + i + "]");
        }
        SmileUtils.addPatternAll(SmileUtils.getEmoticons(), strings, data);

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

        mChatDetailActivityKeyboardLayout.setKeyBoardStateListener(new KeyBoardLockLayout.KeyBoardStateListener() {
            @Override
            public void onState(boolean show) {
                if (show) {
                    mChatDetailActivitySendEmojiLayout.showKeyboard();
                } else {
                    mChatDetailActivitySendEmojiLayout.hideKeyboard();
                }
            }
        });
    }

    private boolean interceptBack() {
        if (mChatDetailActivitySendEmojiLayout.getVisibility() == View.VISIBLE) {
            mChatDetailActivitySendEmojiLayout.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    @OnClick(R.id.chat_detail_activity_send)
    public void onSendClicked() {
        mPresenter.sendMsg(mChatDetailActivityEdit.getText().toString());
    }

    @OnClick(R.id.chat_detail_activity_edit)
    public void onEditClicked() {
        mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight();
    }

    @OnFocusChange(R.id.chat_detail_activity_edit)
    public void onEditFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight();
        }
    }

    @OnClick(R.id.chat_detail_activity_emoji_btn)
    public void onEmojiBtnClicked() {
        if (mChatDetailActivitySendEmojiLayout.getVisibility() == View.VISIBLE) {
            mChatDetailActivityKeyboardLayout.hideBottomViewLockHeight();
        } else {
            mChatDetailActivityKeyboardLayout.showBottomViewLockHeight();
        }
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

    @Override
    public void onBackPressed() {
        if (interceptBack()) {
            return;
        }
        super.onBackPressed();
    }
}
