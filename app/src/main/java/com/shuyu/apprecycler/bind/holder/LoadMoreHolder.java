package com.shuyu.apprecycler.bind.holder;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.bind.NormalLoadMoreHolder;

/**
 * Created by guoshuyu on 2017/8/29.
 */

public class LoadMoreHolder extends NormalLoadMoreHolder {

    public static final int ID = R.layout.load_more;

    private Context context;

    private View loadMoreLayout;

    private ProgressBar loadMoreProgressBar;

    private TextView loadMoreStatusText;

    public LoadMoreHolder(Context context, View v) {
        super(context, v);
        this.context = context;
    }

    @Override
    public void createView(View v) {
        loadMoreLayout = v;
        loadMoreStatusText = (TextView) v.findViewById(com.shuyu.common.R.id.load_more_status_text);
        loadMoreProgressBar = (ProgressBar) v.findViewById(com.shuyu.common.R.id.load_more_progressBar);
        loadMoreLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 借此屏蔽该项的点击事件
                return true;
            }
        });
    }

    @Override
    public void onBind(Object model, int position) {

    }

    @Override
    public void loadingState(Object object) {
        loadMoreStatusText.setText("加载中");
        loadMoreProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadedAllState(Object object) {
        loadMoreStatusText.setText("没有更多");
        loadMoreProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void loadedFailState(Object object) {
        loadMoreStatusText.setText("加载失败");
        loadMoreProgressBar.setVisibility(View.GONE);
    }

    public static class LoadMoreModel {

    }


}
