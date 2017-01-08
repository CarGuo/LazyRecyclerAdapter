### 项目拥有一个通用的RecyclerAdapter，支持一个列表多种Item类型，让你无需维护和编写任何Adapter代码，参考项目配置逻辑，你可以快速集成拥有点击，动画，自定义刷新，自定义上拉，自定义空页面显示等多种配置模式的RecyclerView效果。高复用，你只需要编写维护Holder代码，通过Model来管理你的Holder显示，一个Holder可处理多种布局，存在多个RecyclerView逻辑里。

[![](https://jitpack.io/v/CarGuo/LazyRecyclerAdapter.svg)](https://jitpack.io/#CarGuo/LazyRecyclerAdapter)
[![Build Status](https://travis-ci.org/CarGuo/LazyRecyclerAdapter.svg?branch=master)](https://travis-ci.org/CarGuo/LazyRecyclerAdapter)

#### 在你的项目project下的build.gradle添加
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
#### 在module下的build.gradle添加依赖
```
dependencies {
    compile 'com.github.CarGuo:LazyRecyclerAdapter:v2.0.0'
}

```


## [wiki 文档](https://github.com/CarGuo/LazyRecyclerAdapter/wiki)

## [简书详解说明](http://www.jianshu.com/p/9c9aede9a19a)

### 新版本 2.0.0 优化了代码，修复了bug，更新了多种demo。

### QQ群，有兴趣的可以进来，无底线欢迎：174815284 。

--------------------------------------------------------------------------------


### 效果
<img src="https://github.com/CarGuo/CommonRecycler/blob/master/01.jpg" width="120px" height="218px"/>
<img src="https://github.com/CarGuo/CommonRecycler/blob/master/02.jpg" width="120px" height="218px"/>
<img src="https://github.com/CarGuo/CommonRecycler/blob/master/03.jpg" width="120px" height="218px"/>


### GIF效果

![](https://github.com/CarGuo/CommonRecycler/blob/master/01.gif)

### Demo 多样式列表 类型

* 普通列表 不刷新。
* 普通列表 + 系统下拉 + Adapter上拉。
* 瀑布流 + 系统下拉 + Adapter上拉。
* 普通列表 xRecycler 不刷新。
* 普通列表 + XRecycler + 上下拉。
* 瀑布流 + XRecycler + 上下拉。
* 瀑布流 + XRecycler + 自定义上下拉。
* ViewPager下。
* adapter 实现空页面。
* XRecycler 实现空页面。
