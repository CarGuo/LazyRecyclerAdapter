### 通用RecylerAdapter，内置XRecyclerView，兼容上下拉与空页面，高复用，一个Adapter通用所有页面，支持动画，懒人专属

```
<dependency>
  <groupId>com.shuyu</groupId>
  <artifactId>CommonRecycler</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

```
dependencies {
    compile 'com.shuyu:CommonRecycler:1.0.1'
}
```


### 效果
<img src="https://github.com/CarGuo/CommonRecycler/blob/master/01.jpg" width="120px" height="218px"/>
<img src="https://github.com/CarGuo/CommonRecycler/blob/master/02.jpg" width="120px" height="218px"/>
<img src="https://github.com/CarGuo/CommonRecycler/blob/master/03.jpg" width="120px" height="218px"/>


### GIF效果

![](https://github.com/CarGuo/CommonRecycler/blob/master/01.gif)

## 说明，可以对照Demo阅读

　Holder可以理解为列表中一个Item的逻辑处理类，每一种类型的Item有一种Holder。

　只需要一个Adapter，你就可以实现各种类型的列表，在一个列表里兼容不同类型的Item，你需要做的仅仅是维护你的Holder（类似List里的一个Item）和Model，无需再关心其他，实现高复用与多样式逻辑，外带支持自定义动画，多种上下拉实现方式，不需要再写任何Adapter代码。

#### 1、 CommonRecyclerManager ：绑定layoutId和你的Holder类名。

　这个管理类是用于绑定Holder和R.layout.xxx，这样在后面CommonRecyclerAdapter 用它通过数据Model的layoutId找到对应的Holder并创建它。

```
//将布局的ID和holder类型关联
commonRecyclerManager.addType(TextHolder.ID, TextHolder.class.getName());
```
#### 2、 RecyclerBaseHolder ：继承这个Holder，实现你的需求。

　RecyclerBaseHolder的所有Holder的基类，他继承了RecyclerView.ViewHolder并定义写两个方法，所以你继承它就对了，在createView的时候找到控件，在onBind读取数据填充画面。这里就是实现你梦想的地方！

```
//实现的hodler继承RecyclerBaseHolder，重载下面方式实现你的需求
public class TextHolder extends RecyclerBaseHolder {
    //布局id，一般我习惯吧这个Holder需要处理的id都写在这里，方便管理
    public final static int ID = R.layout.text_item;
    @BindView(R.id.item_text)
    TextView itemText;

    public TextHolder(Context context, View v) {
        super(context, v);
    }

    //view创建好了
    @Override
    public void createView(View v) {
        ButterKnife.bind(this, v);
    }

    //view创建好了，更具数据处理逻辑
    @Override
    public void onBind(RecyclerBaseModel model, int position) {
        //转化为你的model
        TextModel textModel = (TextModel) (model);
        itemText.setText(textModel.getText());
    }

    //不需要可以不写
    @Override
    public AnimatorSet getAnimator(View view) {
        //实现你的动画
        return null;
    }
}
```

#### 3、 CommonRecyclerAdapter ：通用的适配器

　只需要传入数据List和CommonRecyclerManager，就会根据Model的顺序，通过数据的layoutId在RecyclerView中自动生成对应的Holder，其他的功能只需要简单的配置即可。

```
//用数据和manager创建
adapteradapter = new CommonRecyclerAdapter(getActivity(), commonRecyclerManager, datas);

//支持需要加载更多
adapter.setNeedLoadMore(true);

//支持空数据显示 空页面
adapter.setShowNoData(true);

//设置item显示动画支持打开
adapter.setNeedAnimation(true);
```

#### 4、RecyclerBaseModel ：数据model的积累，必须继承它，不离不弃。

　继承它的作用是因为整个Adapter都是以它为基类，你需要继承他，最终的是，你需要这个Model对应的布局Id，这样它才能找到属于自己的Holder。

```
//继承RecyclerBaseModel实现你需要的数据类型
public class TextModel extends RecyclerBaseModel {
    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
```

## 流程
1、实现你的Holder并继承RecyclerBaseHolder，这里是你实现需求的地方，相当于Item的逻辑。

2、让你的数据model继承RecyclerBaseModel，设置Model的LayoutId(很重要)，这样model就会通过CommonRecyclerManager，找到LayoutId对应关联的Holder，并生成它。

3、你需要一个CommonRecyclerManager来绑定你的LayoutId和处理这布局的Holder类名。

4、通过CommonRecyclerManager和Model的数据列表生成CommonRecyclerAdapter

5、把Adapter交给Recycler。



### 普通下拉刷新与上拉加载更多
　
　普通的列表，直接使用系统的SwipeRefreshLayout就可以啦，简单有好用。下拉加载更多直接添加下方方法，轻松实现上下拉刷新<(￣︶￣)↗，简单粗暴，就是记得要加个锁避免重复进入。

```
//打开支持需要加载更多
adapter.setNeedLoadMore(true);

recycler.addOnScrollListener(new LoadMoreScrollListener() {
    @Override
    public void onLoadMore() {
        //注意加锁
        if (!isLoadMore) {
            isLoadMore = true;
            recycler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isLoadMore = false;
                    loadMore();
                }

            }, 2000);
        }
    }

    //当前第一个可视的是哪个item
    @Override
    public void onScrolled(int firstPosition) {
    }
});

```

### 其他配置
　
　你还可以配置是否显示动画效果，配置上拉loading的颜色，单击和长按等，看下面。

```
//支持空数据显示 空页面
adapter.setShowNoData(true);

//显示空数据model,不设置显示默认空页面
adapter.setNoDataModel(noDataModel);

//显示空数据页面布局,不设置显示默认，布局id需要通过CommonRecyclerManager关联hodler
adapter.setNoDataLayoutId(noDataLayoutId);

//设置动画支持打开
adapter.setNeedAnimation(true);

//添加点击
adapter.setOnItemClickListener();

```

## [XRecyclerView兼容支持](https://github.com/jianghejie/XRecyclerView)

　这里添加了XRecyclerView，并且对其进行了修改。

　XRecyclerView内置了内部Adapter，使其支持添加头部，自带上下拉效果的控件，部分调整之后，全面支持CommonRecyclerAdapter。

　这里使用方式和普通的RecyclerView一样，通过和CommonRecyclerAdapter的配合使用通用配置，而且它同样支持空页面显示，还支持添加各种头部，唯一需要注意的是，添加分割线类addItemDecoration和点击的时候，需要针对添加了头部和刷新的绝对的position位置换算成相对的位置。

```

//是否屏蔽下拉
//xRecycler.setPullRefreshEnabled(false);
//上拉加载更多样式，也可以设置下拉
xRecycler.setLoadingMoreProgressStyle(ProgressStyle.SysProgress);
//设置管理器，关联布局与holder类名，不同id可以管理一个holder
CommonRecyclerManager commonRecyclerManager = new CommonRecyclerManager();
commonRecyclerManager.addType(ImageHolder.ID, ImageHolder.class.getName());
commonRecyclerManager.addType(TextHolder.ID, TextHolder.class.getName());
commonRecyclerManager.addType(ClickHolder.ID, ClickHolder.class.getName());
//初始化通用管理器
commonRecyclerAdapter = new CommonRecyclerAdapter(getActivity(), commonRecyclerManager, dataList);
xRecycler.setAdapter(commonRecyclerAdapter);

ImageView imageView = new ImageView(getActivity());
imageView.setImageResource(R.drawable.xxx1);
imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
imageView.setMinimumHeight(dip2px(getActivity(), 100));
//添加头部
xRecycler.addHeaderView(imageView);
//本身也支持设置空局部
//xRecycler.setEmptyView();
xRecycler.setLoadingListener(new XRecyclerView.LoadingListener() {
    @Override
    public void onRefresh() {
        xRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                xRecycler.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        xRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMore();
            }
        }, 2000);
    }
});

commonRecyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
    @Override
    public void onItemClick(Context context, int position) {
        //需要减去你的header和刷新的view的数量
        Toast.makeText(getActivity(), "点击了！！　" + (position - 2), Toast.LENGTH_SHORT).show();
    }
});
```
