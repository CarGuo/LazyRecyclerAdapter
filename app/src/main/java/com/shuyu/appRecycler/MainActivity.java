package com.shuyu.apprecycler;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.shuyu.CommonRecyclerAdapter.model.RecyclerBaseModel;
import com.shuyu.apprecycler.Holder.ClickHolder;
import com.shuyu.apprecycler.Holder.ImageHolder;
import com.shuyu.apprecycler.Holder.TextHolder;
import com.shuyu.apprecycler.Model.ClickModel;
import com.shuyu.apprecycler.Model.ImageModel;
import com.shuyu.apprecycler.Model.TextModel;
import com.shuyu.apprecycler.adapter.ListFragmentPagerAdapter;
import com.shuyu.apprecycler.view.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.layouttabStrip)
    PagerSlidingTabStrip tabStrip;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private List<RecyclerBaseModel> allList = new ArrayList<>();
    private List<RecyclerBaseModel> stagList = new ArrayList<>();
    private List<RecyclerBaseModel> imageList = new ArrayList<>();

    ListFragment allFragment;
    ListFragment emptyFragment;
    ListFragment imageFragment;
    List2Fragment stagFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        allList();

        imageList();

        stagList();

        allFragment = new ListFragment();
        allFragment.setDatas(allList);
        emptyFragment = new ListFragment();
        imageFragment = new ListFragment();
        imageFragment.setDatas(imageList);
        stagFragment = new List2Fragment();
        stagFragment.setDatas(stagList);

        List<Fragment> list = new ArrayList<>();
        list.add(allFragment);
        list.add(emptyFragment);
        list.add(imageFragment);
        list.add(stagFragment);

        List<String> title = new ArrayList<>();
        title.add("111");
        title.add("222");
        title.add("333");
        title.add("444");

        ListFragmentPagerAdapter listFragmentPagerAdapter =
                new ListFragmentPagerAdapter(getSupportFragmentManager(), list, title);
        viewpager.setAdapter(listFragmentPagerAdapter);

        tabStrip.setShouldExpand(true);
        tabStrip.setDividerColor(Color.WHITE);
        tabStrip.setIndicatorColor(Color.YELLOW);
        tabStrip.setUnderlineColor(Color.TRANSPARENT);
        tabStrip.setTabTextSelectColor(Color.YELLOW);
        tabStrip.setViewPager(viewpager);
        tabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewpager.setCurrentItem(0);
    }

    private void allList() {

        ImageModel imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        allList.add(imageModel);

        TextModel textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        allList.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        allList.add(imageModel);

        ClickModel clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        allList.add(clickModel);


        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        allList.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        allList.add(imageModel);

        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        allList.add(clickModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        allList.add(imageModel);

        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        allList.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        allList.add(imageModel);


        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        allList.add(clickModel);


        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        allList.add(textModel);

        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        allList.add(clickModel);
    }

    private void stagList() {

        ImageModel imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        stagList.add(imageModel);

        TextModel textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        stagList.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        stagList.add(imageModel);

        ClickModel clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        stagList.add(clickModel);


        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        stagList.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        stagList.add(imageModel);

        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        stagList.add(clickModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        stagList.add(imageModel);

        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        stagList.add(textModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        stagList.add(imageModel);


        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        stagList.add(clickModel);


        textModel = new TextModel();
        textModel.setResLayoutId(TextHolder.ID);
        textModel.setText("你这个老司机，说好的文本呢1");
        stagList.add(textModel);

        clickModel = new ClickModel();
        clickModel.setResLayoutId(ClickHolder.ID);
        clickModel.setBtnText("我是老按键，按啊按啊按啊····");
        stagList.add(clickModel);
    }


    private void imageList() {

        ImageModel imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);

        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a2);
        imageList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setResLayoutId(ImageHolder.ID);
        imageModel.setResId(R.drawable.a1);
        imageList.add(imageModel);

    }

}
