package com.shuyu.apprecycler.bind.holder;

import android.view.View;
import android.widget.TextView;

import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.model.BindTextModel;
import com.shuyu.bind.BindRecyclerBaseHolder;

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 * <p>
 * Created by guoshuyu on 2017/8/29.
 */

public class BindTextHolder extends BindRecyclerBaseHolder {

    public final static int ID = R.layout.text_item;

    TextView itemText;

    /**
     * 二选一继承构造方法
     *
     * @param v layoutId实例化view
     */
    public BindTextHolder(View v) {
        super(v);
    }

    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    @Override
    public void createView(View v) {
        itemText = (TextView) v.findViewById(R.id.item_text);
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    @Override
    public void onBind(Object model, int position) {
        BindTextModel textModel = (BindTextModel) (model);
        itemText.setText(textModel.getText());
    }
}
