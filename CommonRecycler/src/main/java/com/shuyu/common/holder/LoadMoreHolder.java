package com.shuyu.common.holder;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shuyu.common.R;
import com.shuyu.common.RecyclerBaseHolder;
import com.shuyu.common.model.LoadMoreModel;
import com.shuyu.common.model.RecyclerBaseModel;


/**
 * Created by Guo on 2015/11/23.
 * 加载更多Holder
 */
public class LoadMoreHolder extends RecyclerBaseHolder {

    public static final int LAYOUTID = R.layout.load_more;

    public static final int LOAD_MORE_STATE = 0;//加载

    public static final int NULL_DATA_STATE = 1;//没有数据

    public static final int FAIL_STATE = 2;//失败

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
        loadMoreStatusText = (TextView) v.findViewById(R.id.load_more_status_text);
        loadMoreProgressBar = (ProgressBar) v.findViewById(R.id.load_more_progressBar);
        loadMoreLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 借此屏蔽该项的点击事件
                return true;
            }
        });
    }

    @Override
    public void onBind(RecyclerBaseModel model, int position) {

        LoadMoreModel loadMoreModel = (LoadMoreModel) model;


        if (loadMoreModel.getColor() != -11) {
            loadMoreStatusText.setTextColor(loadMoreModel.getColor());
        }

        if (loadMoreModel.getProgressDrawable() != null) {
            loadMoreProgressBar.setProgressDrawable(loadMoreModel.getProgressDrawable());
        }
        switch (model.getExtraTag()) {
            case LOAD_MORE_STATE:
                loadMoreStatusText.setText(loadMoreModel.getLoadText());
                loadMoreProgressBar.setVisibility(View.VISIBLE);
                break;
            case NULL_DATA_STATE:
                loadMoreStatusText.setText(loadMoreModel.getNullText());
                loadMoreProgressBar.setVisibility(View.GONE);
                break;
            case FAIL_STATE:
                loadMoreStatusText.setText(loadMoreModel.getFailText());
                loadMoreProgressBar.setVisibility(View.GONE);
                break;
        }

    }
}
