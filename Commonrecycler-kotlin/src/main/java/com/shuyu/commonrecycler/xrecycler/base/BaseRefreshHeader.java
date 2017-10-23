package com.jcodecraeer.xrecyclerview.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by jianghejie on 15/11/22.
 */
public abstract class BaseRefreshHeader extends LinearLayout implements BaseRefreshInterface{

    public final static int STATE_NORMAL = 0;

    public final static int STATE_RELEASE_TO_REFRESH = 1;

    public final static int STATE_REFRESHING = 2;

    public final static int STATE_DONE = 3;

    public BaseRefreshHeader(Context context) {
        super(context);
    }

    public BaseRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}