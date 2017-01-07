package com.jcodecraeer.xrecyclerview.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by guoshuyu on 2017/1/7.
 */

public abstract class BaseLoadMoreFooter extends LinearLayout implements BaseLoadMoreInterface {

    public final static int STATE_LOADING = 0;

    public final static int STATE_COMPLETE = 1;

    public final static int STATE_NOMORE = 2;

    public BaseLoadMoreFooter(Context context) {
        super(context);
    }

    public BaseLoadMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseLoadMoreFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
