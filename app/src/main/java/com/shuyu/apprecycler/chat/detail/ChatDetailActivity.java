package com.shuyu.apprecycler.chat.detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.view.BindCustomLoadMoreFooter;
import com.shuyu.apprecycler.chat.data.model.ChatImageModel;
import com.shuyu.apprecycler.chat.data.model.ChatTextModel;
import com.shuyu.apprecycler.chat.holder.ChatImageHolder;
import com.shuyu.apprecycler.chat.holder.ChatTextHolder;
import com.shuyu.bind.BindSuperAdapter;
import com.shuyu.bind.BindSuperAdapterManager;
import com.shuyu.bind.listener.OnBindDataChooseListener;
import com.shuyu.bind.listener.OnItemClickListener;
import com.shuyu.bind.listener.OnLoadingListener;
import com.shuyu.textutillib.RichEditText;

import java.util.List;

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

    private BindSuperAdapter mAdapter;

    private BindSuperAdapterManager mNormalAdapterManager;

    @Inject
    ChatDetailPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        ButterKnife.bind(this);

        initView();
    }


    private void initView() {

        DaggerChatDetailComponent.builder()
                .chatDetailModule(new ChatDetailModule(this))
                .build()
                .inject(this);

        initRecycler();
    }

    private void initRecycler() {
        mNormalAdapterManager = new BindSuperAdapterManager();

        //注意，一个manager中，一个id只能绑定一个holder
        //一个model class可以绑定多对id + Holder
        mNormalAdapterManager.bind(ChatImageModel.class, R.layout.chat_layout_image_left, ChatImageHolder.class)
                .bind(ChatImageModel.class, R.layout.chat_layout_image_right, ChatImageHolder.class)
                .bind(ChatTextModel.class, R.layout.chat_layout_text_left, ChatTextHolder.class)
                .bind(ChatTextModel.class, R.layout.chat_layout_text_right, ChatTextHolder.class)
                .bingChooseListener(new OnBindDataChooseListener() {
                    @Override
                    public int getCurrentDataLayoutId(Object object, Class classType, int position, List<Integer> ids) {
                        if (object instanceof ChatTextModel) {
                            ChatTextModel chatTextModel = (ChatTextModel) object;
                            return (chatTextModel.isMe()) ? R.layout.chat_layout_text_right : R.layout.chat_layout_text_left;
                        } else if (object instanceof ChatImageModel) {
                            ChatImageModel chatImageModel = (ChatImageModel) object;
                            return (chatImageModel.isMe()) ? R.layout.chat_layout_image_right : R.layout.chat_layout_image_left;
                        }
                        return ids.get(ids.size() - 1);
                    }
                })
                .setPullRefreshEnabled(false)
                .setLoadingMoreEnabled(false)
                .setFootView(new BindCustomLoadMoreFooter(this))
                .setNeedAnimation(true)
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
        //DaggerChatDetailComponent.b;
        mAdapter = new BindSuperAdapter(this, mNormalAdapterManager, mPresenter.getDataList());
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

}
