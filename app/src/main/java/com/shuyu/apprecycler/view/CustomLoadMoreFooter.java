package com.shuyu.apprecycler.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.base.BaseLoadMoreFooter;
import com.shuyu.apprecycler.R;

/**
 * Created by guoshuyu on 2017/1/8.
 */

public class CustomLoadMoreFooter extends BaseLoadMoreFooter {

    private ImageView mImageView;

    private TextView mText;


    private AnimationDrawable mAnimationDrawable;


    public CustomLoadMoreFooter(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomLoadMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        setGravity(Gravity.CENTER);
        setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setPadding(0, (int) getResources().getDimension(R.dimen.textandiconmargin), 0,
                (int) getResources().getDimension(R.dimen.textandiconmargin));


        mImageView = new ImageView(getContext());
        mImageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mImageView.setImageResource(R.drawable.progressbar);


        addView(mImageView);

        mText = new TextView(getContext());
        mText.setText("正在加载...");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.textandiconmargin), 0, 0, 0);

        mText.setLayoutParams(layoutParams);
        addView(mText);

        mAnimationDrawable = (AnimationDrawable) mImageView.getDrawable();
    }

    @Override
    public void setProgressStyle(int style) {
    }

    @Override
    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mImageView.setVisibility(View.VISIBLE);
                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.VISIBLE);
                mAnimationDrawable.start();
                break;
            case STATE_COMPLETE:
                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.GONE);
                mAnimationDrawable.stop();
                break;
            case STATE_NOMORE:
                mText.setText(getContext().getText(R.string.nomore_loading));
                mImageView.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                mAnimationDrawable.stop();
                break;
        }
    }
}