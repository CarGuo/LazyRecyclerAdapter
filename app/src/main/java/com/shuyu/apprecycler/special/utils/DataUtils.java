package com.shuyu.apprecycler.special.utils;

import com.shuyu.apprecycler.special.holder.ClickHolder;
import com.shuyu.apprecycler.special.holder.ImageHolder;
import com.shuyu.apprecycler.special.holder.TextHolder;
import com.shuyu.apprecycler.special.model.ClickModel;
import com.shuyu.apprecycler.special.model.ImageModel;
import com.shuyu.apprecycler.special.model.TextModel;
import com.shuyu.apprecycler.R;
import com.shuyu.common.model.RecyclerBaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoshuyu on 2017/1/7.
 */

public class DataUtils {

    public static List<String> getHomeList() {
        List<String> list = new ArrayList<>();

        list.add("普通列表 不刷新");
        list.add("普通列表 + 系统下拉 + Adapter上拉");
        list.add("瀑布流 + 系统下拉 + Adapter上拉");
        list.add("普通列表 xRecycler 不刷新");
        list.add("普通列表 + XRecycler + 上下拉");
        list.add("瀑布流 + XRecycler + 上下拉");
        list.add("瀑布流 + XRecycler + 自定义上下拉");
        list.add("ViewPager下");
        list.add("adapter 实现空页面");
        list.add("XRecycler 实现空页面");
        list.add("Grid + XRecycler + 上下拉");
        list.add("Grid + 普通上下拉");

        return list;
    }


    public static List<RecyclerBaseModel> getRefreshData() {

        List<RecyclerBaseModel> list = new ArrayList<>();

        ImageModel imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);

        TextModel textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        list.add(imageModel);

        ClickModel clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);


        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);

        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        list.add(imageModel);

        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);


        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);


        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);

        return list;
    }

    public static List<RecyclerBaseModel> getLoadMoreData(List<RecyclerBaseModel> datas) {
        List<RecyclerBaseModel> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            RecyclerBaseModel recyclerBaseModel = datas.get(i);
            RecyclerBaseModel newModel;
            if (recyclerBaseModel instanceof ClickModel) {
                newModel = new ClickModel();
                ClickModel clickModel = (ClickModel) newModel;
                clickModel.setResLayoutId(recyclerBaseModel.getResLayoutId());
                clickModel.setBtnText("我就老按键哈哈哈哈！！！！！ " + i);
                list.add(newModel);
            } else if (recyclerBaseModel instanceof TextModel) {
                newModel = new TextModel();
                TextModel textModel = (TextModel) newModel;
                textModel.setResLayoutId(recyclerBaseModel.getResLayoutId());
                textModel.setText("我就老文本哈哈哈哈！！！！！ " + i);
                list.add(newModel);
            } else if (recyclerBaseModel instanceof ImageModel) {
                newModel = new ImageModel();
                ImageModel imageModel = (ImageModel) newModel;
                imageModel.setResLayoutId(recyclerBaseModel.getResLayoutId());
                imageModel.setResId(((ImageModel) recyclerBaseModel).getResId());
                list.add(newModel);
            }
        }
        return list;
    }
}
