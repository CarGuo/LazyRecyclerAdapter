package com.shuyu.apprecycler.bind.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.bind.BindBaseRefreshHeader;


/**
 * 继承BindCustomRefreshHeader实现的下拉刷新
 * <p>
 * 如果需要自定义程度更高的，即可继承BaseRefreshHeader
 * <p>
 * Created by guoshuyu on 2017/1/8.
 */

public class BindHorizontalCustomRefreshHeader extends BindBaseRefreshHeader {


    private ImageView mCustomRefreshImg;

    private TextView mCustomRefreshTxt;

    private AnimationDrawable mAnimationDrawable;

    private int mMeasuredHeight;

    public BindHorizontalCustomRefreshHeader(Context context) {
        super(context);
    }


    public BindHorizontalCustomRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 继承后配置为横向
     */
    @Override
    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.width;
    }

    /**
     * 继承后配置为横向
     */
    @Override
    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        lp.width = height;
        mContainer.setLayoutParams(lp);
    }


    /**
     * 继承
     *
     * @return 返回布局id
     */
    @Override
    protected int getLayoutId() {
        return R.layout.layout_horizontal_custom_refresh_header;
    }

    /**
     * 继承，将view添加到控件中
     *
     * @param container
     */
    @Override
    protected void addView(ViewGroup container) {
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        addView(container, new LayoutParams(0, LayoutParams.MATCH_PARENT));
        setGravity(Gravity.LEFT);

        mCustomRefreshImg = (ImageView) container.findViewById(R.id.custom_refresh_img);
        mCustomRefreshTxt = (TextView) container.findViewById(R.id.custom_refresh_txt);


        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredWidth();

        mAnimationDrawable = (AnimationDrawable) mCustomRefreshImg.getDrawable();
    }

    /**
     * 继承，返回高度
     */
    @Override
    protected int getCurrentMeasuredHeight() {
        return mMeasuredHeight;
    }

    /**
     * 继承，根据状态调整显示效果
     *
     * @param state
     */
    @Override
    public void setState(int state) {
        if (state == mState) return;

        if (state == STATE_REFRESHING) {    // 显示进度
            mAnimationDrawable.start();
        } else if (state == STATE_DONE) {
            mAnimationDrawable.stop();
        } else {
            // 显示进度
            mAnimationDrawable.stop();
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_RELEASE_TO_REFRESH) {
                }
                if (mState == STATE_REFRESHING) {
                }
                mCustomRefreshTxt.setText("看到了我吧！");
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH) {
                    mCustomRefreshTxt.setText("放开我刷新！");
                }
                break;
            case STATE_REFRESHING:
                mCustomRefreshTxt.setText("刷新中！");
                break;
            case STATE_DONE:
                mCustomRefreshTxt.setText("刷新好了哟！");
                break;
            default:
        }

        mState = state;
    }
}
