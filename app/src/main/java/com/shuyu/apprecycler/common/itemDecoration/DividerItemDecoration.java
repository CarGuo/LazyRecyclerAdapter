package com.shuyu.apprecycler.common.itemDecoration;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shuyu.bind.BindSuperAdapter;

/**
 * 实现分割线
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    public final static int GRID = 0;
    public final static int LIST = 1;

    private int direct = GRID;

    private int space;

    private int startPosition; //根据你想要的屏蔽不需要的边距的。

    public DividerItemDecoration(int space) {
        this.space = space;
    }

    public DividerItemDecoration(int space, int direct) {
        this.space = space;
        this.direct = direct;
    }

    public DividerItemDecoration(int space, int direct, int startPosition) {
        this.space = space;
        this.direct = direct;
        this.startPosition = startPosition;
    }

    public DividerItemDecoration(int space, int direct, BindSuperAdapter normalBindSuperAdapter) {
        this.space = space;
        this.direct = direct;
        this.startPosition = normalBindSuperAdapter.absFirstPositionWithoutHeader();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (direct == GRID) {
            if ((parent.getChildAdapterPosition(view) >= startPosition || parent.getChildAdapterPosition(view) >= startPosition + 1) ){
                outRect.left = space;
                outRect.bottom = space * 2;
                outRect.right = space;
                if (parent.getChildAdapterPosition(view) == startPosition || parent.getChildAdapterPosition(view) == startPosition + 1) {
                    outRect.top = space;
                }
            }
        } else {
            if (parent.getChildAdapterPosition(view) >= startPosition){
                outRect.left = space;
                outRect.bottom = space * 2;
                outRect.right = space;
                if (parent.getChildAdapterPosition(view) == startPosition) {
                    outRect.top = space * 2;
                }
            }
        }
    }
}