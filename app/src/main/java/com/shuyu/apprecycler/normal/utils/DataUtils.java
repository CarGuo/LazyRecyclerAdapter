package com.shuyu.apprecycler.normal.utils;


import com.shuyu.apprecycler.R;
import com.shuyu.apprecycler.normal.model.ClickModel;
import com.shuyu.apprecycler.normal.model.ImageModel;
import com.shuyu.apprecycler.normal.model.TextModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoshuyu on 2017/1/7.
 */

public class DataUtils {

    public static List<String> getHomeList() {
        List<String> list = new ArrayList<>();

        return list;
    }


    public static List getRefreshData() {

        List list = new ArrayList<>();

        ImageModel imageModel = new ImageModel();
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);

        TextModel textModel = new TextModel();
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResId(R.drawable.a2);
        list.add(imageModel);

        ClickModel clickModel = new ClickModel();
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);


        textModel = new TextModel();
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);

        clickModel = new ClickModel();
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);

        imageModel = new ImageModel();
        imageModel.setResId(R.drawable.a2);
        list.add(imageModel);

        textModel = new TextModel();
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResId(R.drawable.a1);
        list.add(imageModel);


        clickModel = new ClickModel();
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);


        textModel = new TextModel();
        textModel.setText("你这个老司机，说好的文本呢1");
        list.add(textModel);

        clickModel = new ClickModel();
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        list.add(clickModel);

        return list;
    }

    public static List getLoadMoreData(List datas) {
        List list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            Object object = datas.get(i);
            Object newModel;
            if (object instanceof ClickModel) {
                newModel = new ClickModel();
                ClickModel clickModel = (ClickModel) newModel;
                clickModel.setBtnText("我就老按键哈哈哈哈！！！！！ " + i);
                list.add(newModel);
            } else if (object instanceof TextModel) {
                newModel = new TextModel();
                TextModel textModel = (TextModel) newModel;
                textModel.setText("我就老文本哈哈哈哈！！！！！ " + i);
                list.add(newModel);
            } else if (object instanceof ImageModel) {
                newModel = new ImageModel();
                ImageModel imageModel = (ImageModel) newModel;
                imageModel.setResId(((ImageModel) object).getResId());
                list.add(newModel);
            }
        }
        return list;
    }
}
