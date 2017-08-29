package com.shuyu.apprecycler.bind.utils;


import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.bind.model.BindClickModel;
import com.shuyu.apprecycler.bind.model.BindImageModel;
import com.shuyu.apprecycler.bind.model.BindTextModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoshuyu on 2017/1/7.
 */

public class BindDataUtils {

    public static List<String> getHomeList() {
        List<String> list = new ArrayList<>();

        list.add("普通列表 不刷新");
        list.add("普通列表 + 系统下拉 + Adapter上拉");
        list.add("瀑布流 + 系统下拉 + Adapter上拉");
        list.add("普通列表 xRecycler 不刷新");
        list.add("普通列表 + XRecycler + 上下拉");
        list.add("瀑布流 + XRecycler + 上下拉");
        list.add("瀑布流 + XRecycler + 自定义上下拉");
        list.add("adapter 实现空页面");
        list.add("XRecycler 实现空页面");
        list.add("Grid + XRecycler + 上下拉");
        list.add("Grid + 普通上下拉");
        return list;
    }


    public static List getRefreshData() {

        List list = new ArrayList<>();

        BindImageModel imageModel = new BindImageModel();
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);

        BindTextModel textModel = new BindTextModel();
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new BindImageModel();
        imageModel.setResId(R.drawable.a2);
        list.add(imageModel);

        BindClickModel clickModel = new BindClickModel();
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);


        textModel = new BindTextModel();
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new BindImageModel();
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);

        clickModel = new BindClickModel();
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);

        imageModel = new BindImageModel();
        imageModel.setResId(R.drawable.a2);
        list.add(imageModel);

        textModel = new BindTextModel();
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new BindImageModel();
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);


        clickModel = new BindClickModel();
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);


        textModel = new BindTextModel();
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        clickModel = new BindClickModel();
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);

        return list;
    }

    public static List getLoadMoreData(List datas) {
        List list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            Object object = datas.get(i);
            Object newModel;
            if (object instanceof BindClickModel) {
                newModel = new BindClickModel();
                BindClickModel clickModel = (BindClickModel) newModel;
                clickModel.setBtnText("我就老按键哈哈哈哈！！！！！ " + i);
                list.add(newModel);
            } else if (object instanceof BindTextModel) {
                newModel = new BindTextModel();
                BindTextModel textModel = (BindTextModel) newModel;
                textModel.setText("我就老文本哈哈哈哈！！！！！ " + i);
                list.add(newModel);
            } else if (object instanceof BindImageModel) {
                newModel = new BindImageModel();
                BindImageModel imageModel = (BindImageModel) newModel;
                imageModel.setResId(((BindImageModel) object).getResId());
                list.add(newModel);
            }
        }
        return list;
    }
}
