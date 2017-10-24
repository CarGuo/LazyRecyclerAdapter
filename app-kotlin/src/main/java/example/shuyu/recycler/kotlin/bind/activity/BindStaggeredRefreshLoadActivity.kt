package example.shuyu.recycler.kotlin.bind.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.widget.Toast

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.bind.holder.BindClickHolder
import example.shuyu.recycler.kotlin.bind.holder.BindImageHolder
import example.shuyu.recycler.kotlin.bind.holder.BindMutliHolder
import example.shuyu.recycler.kotlin.bind.holder.BindNoDataHolder
import example.shuyu.recycler.kotlin.bind.holder.BindTextHolder
import example.shuyu.recycler.kotlin.bind.model.BindClickModel
import example.shuyu.recycler.kotlin.bind.model.BindImageModel
import example.shuyu.recycler.kotlin.bind.model.BindMutliModel
import example.shuyu.recycler.kotlin.bind.model.BindTextModel
import example.shuyu.recycler.kotlin.bind.utils.BindDataUtils
import example.shuyu.recycler.kotlin.bind.view.BindCustomLoadMoreFooter
import example.shuyu.recycler.kotlin.bind.view.BindCustomRefreshHeader
import com.shuyu.commonrecycler.BindSuperAdapter
import com.shuyu.commonrecycler.BindSuperAdapterManager
import com.shuyu.commonrecycler.decoration.BindDecorationBuilder
import com.shuyu.commonrecycler.listener.OnItemClickListener
import com.shuyu.commonrecycler.listener.OnLoadingListener
import kotlinx.android.synthetic.main.activity_normal_recycler_layout.*

import java.util.ArrayList

/**
 * 带上下拉的瀑布流
 * Created by guoshuyu on 2017/1/7.
 */
class BindStaggeredRefreshLoadActivity : AppCompatActivity() {

    private var datas = ArrayList<Any>()

    private var adapter: BindSuperAdapter? = null

    private var normalAdapterManager: BindSuperAdapterManager? = null

    private val lock = Any()

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_recycler_layout)
        init()
        refresh()
    }


    fun init() {
        val header = LayoutInflater.from(this).inflate(R.layout.layout_header, null)

        normalAdapterManager = BindSuperAdapterManager()
        normalAdapterManager
                ?.bind(BindImageModel::class.java, BindImageHolder.ID, BindImageHolder::class.java)
                ?.bind(BindTextModel::class.java, BindTextHolder.ID, BindTextHolder::class.java)
                ?.bind(BindMutliModel::class.java, BindMutliHolder.ID, BindMutliHolder::class.java)
                ?.bind(BindClickModel::class.java, BindClickHolder.ID, BindClickHolder::class.java)
                ?.bindEmpty(BindNoDataHolder.NoDataModel::class.java, BindNoDataHolder.ID, BindNoDataHolder::class.java)
                ?.setNeedAnimation(true)
                ?.setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(context: Context, position: Int) {
                        //需要减去你的header和刷新的view的数量
                        Toast.makeText(context, "点击了！！　" + position, Toast.LENGTH_SHORT).show()
                    }
                })
                ?.addHeaderView(header)
                ?.setPullRefreshEnabled(true)
                ?.setLoadingMoreEnabled(true)
                ?.setFootView(BindCustomLoadMoreFooter(this))
                ?.setRefreshHeader(BindCustomRefreshHeader(this))
                ?.setLoadingListener(object : OnLoadingListener {
                    override fun onRefresh() {
                        recycler?.postDelayed({ refresh() }, 3000)
                    }

                    override fun onLoadMore() {
                        recycler?.postDelayed({ loadMore() }, 2000)
                    }
                })

        adapter = BindSuperAdapter(this, normalAdapterManager!!, datas)


        //瀑布流管理器
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        //staggeredGridLayoutManager.setReverseLayout(true);
        recycler?.layoutManager = staggeredGridLayoutManager

        //使能拖拽
        /*BindDragCallBack bindDragCallBack = new BindDragCallBack(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(bindDragCallBack);
        itemTouchHelper.attachToRecyclerView(recycler);*/


        //间隔线
        recycler?.addItemDecoration(BindDecorationBuilder(adapter)
                .setColor(resources
                        .getColor(R.color.material_deep_teal_500))
                .setSpace(dip2px(this, 5f))
                .setNeedGridRightLeftEdge(true)
                .setNeedFirstTopEdge(true)
                .builder())

        recycler?.adapter = adapter


    }

    private fun refresh() {
        val list = BindDataUtils.refreshData
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized(lock) {
            datas = list
            adapter?.setListData(datas)
            normalAdapterManager?.refreshComplete()
        }

    }

    private fun loadMore() {
        val list = BindDataUtils.getLoadMoreData(datas)
        //组装好数据之后，再一次性给list，在加多个锁，这样能够避免和上拉数据更新冲突
        //数据要尽量组装好，避免多个异步操作同个内存，因为多个异步更新一个数据源会有问题。
        synchronized(lock) {
            adapter?.addListData(list)
            if (count < 1) {
                normalAdapterManager?.loadMoreComplete()
            } else {
                normalAdapterManager?.setNoMore(true)
            }
            count++
        }
    }

    companion object {

        /**
         * dip转为PX
         */
        fun dip2px(context: Context, dipValue: Float): Int {
            val fontScale = context.resources.displayMetrics.density
            return (dipValue * fontScale + 0.5f).toInt()
        }
    }


}
