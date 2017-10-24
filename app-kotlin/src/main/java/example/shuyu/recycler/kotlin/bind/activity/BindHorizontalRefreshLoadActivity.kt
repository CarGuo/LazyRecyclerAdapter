package example.shuyu.recycler.kotlin.bind.activity

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast


import example.shuyu.recycler.kotlin.bind.holder.BindClickHolder
import example.shuyu.recycler.kotlin.bind.holder.BindImageHolder
import example.shuyu.recycler.kotlin.bind.holder.BindMutliHolder
import example.shuyu.recycler.kotlin.bind.holder.BindTextHolder
import example.shuyu.recycler.kotlin.bind.model.BindClickModel
import example.shuyu.recycler.kotlin.bind.model.BindImageModel
import example.shuyu.recycler.kotlin.bind.model.BindMutliModel
import example.shuyu.recycler.kotlin.bind.model.BindTextModel
import example.shuyu.recycler.kotlin.bind.utils.BindDataUtils
import example.shuyu.recycler.kotlin.bind.view.BindHorizontalCustomLoadMoreFooter
import example.shuyu.recycler.kotlin.bind.view.BindHorizontalCustomRefreshHeader
import com.shuyu.commonrecycler.BindSuperAdapter
import com.shuyu.commonrecycler.BindSuperAdapterManager
import com.shuyu.commonrecycler.decoration.BindDecorationBuilder
import com.shuyu.commonrecycler.listener.OnBindDataChooseListener
import com.shuyu.commonrecycler.listener.OnItemClickListener
import com.shuyu.commonrecycler.listener.OnLoadingListener

import java.util.ArrayList

import example.shuyu.recycler.kotlin.R
import kotlinx.android.synthetic.main.activity_horizontal_normal_recycler_layout.*


/**
 * 带上下拉的线性布局
 * Created by guoshuyu on 2017/1/7.
 */

class BindHorizontalRefreshLoadActivity : AppCompatActivity() {

    private var datas = ArrayList<Any>()

    private var adapter: BindSuperAdapter? = null

    private var normalAdapterManager: BindSuperAdapterManager? = null

    private val lock = Any()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_normal_recycler_layout)

        init()
        refresh()
    }

    fun init() {

        normalAdapterManager = BindSuperAdapterManager()

        //注意，一个manager中，一个id只能绑定一个holder
        //一个model class可以绑定多对id + Holder
        normalAdapterManager?.bind(BindTextModel::class.java, BindTextHolder.ID, BindTextHolder::class.java)
                ?.bind(BindImageModel::class.java, BindImageHolder.ID, BindImageHolder::class.java)
                ?.bind(BindMutliModel::class.java, BindImageHolder.ID, BindImageHolder::class.java)
                ?.bind(BindMutliModel::class.java, BindMutliHolder.ID, BindMutliHolder::class.java)
                ?.bind(BindClickModel::class.java, BindClickHolder.ID, BindClickHolder::class.java)
                ?.bingChooseListener(object : OnBindDataChooseListener {
                    //一种model类型对应多个Holder时，根据model实体判断选择holder
                    override fun getCurrentDataLayoutId(`object`: Any, classType: Class<*>, position: Int, ids: List<Int>): Int {
                        return if (`object` is BindMutliModel && ids.size > 1) {
                            if (`object`.type > 1) {
                                BindMutliHolder.ID
                            } else {
                                BindImageHolder.ID
                            }
                        } else ids[ids.size - 1]
                    }
                })
                ?.setPullRefreshEnabled(true)
                ?.setLoadingMoreEnabled(true)
                ?.setFootView(BindHorizontalCustomLoadMoreFooter(this))
                ?.setRefreshHeader(BindHorizontalCustomRefreshHeader(this))
                ?.setNeedAnimation(true)
                ?.setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(context: Context, position: Int) {
                        //需要减去你的header和刷新的view的数量
                        Toast.makeText(context, "点击了！！　" + position, Toast.LENGTH_SHORT).show()
                    }
                })
                ?.setLoadingListener(object : OnLoadingListener {
                    override fun onRefresh() {
                        recycler?.postDelayed({ refresh() }, 1000)
                    }

                    override fun onLoadMore() {
                        recycler?.postDelayed({ loadMore() }, 1000)
                    }
                })


        adapter = BindSuperAdapter(this, normalAdapterManager!!, datas)

        recycler?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        //recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        //间隔线
        recycler?.addItemDecoration(BindDecorationBuilder(adapter)
                .setColor(resources
                        .getColor(R.color.material_deep_teal_500))
                .setSpace(dip2px(this, 2f))
                .builder())

        recycler?.adapter = adapter
    }

    private fun refresh() {
        val list = BindDataUtils.refreshData

        var mutliModel = BindMutliModel()

        mutliModel.resId = R.drawable.a1
        mutliModel.res2 = R.drawable.a2
        mutliModel.type = 1
        list.add(0, mutliModel)

        mutliModel = BindMutliModel()

        mutliModel.resId = R.drawable.a1
        mutliModel.res2 = R.drawable.a2
        mutliModel.type = 2
        list.add(1, mutliModel)

        mutliModel = BindMutliModel()

        mutliModel.resId = R.drawable.a1
        mutliModel.res2 = R.drawable.a2
        mutliModel.type = 1
        list.add(4, mutliModel)


        mutliModel = BindMutliModel()
        mutliModel.resId = R.drawable.a1
        mutliModel.res2 = R.drawable.a2
        mutliModel.type = 2
        list.add(7, mutliModel)

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
            //adapter.setLoadMoreState(BindLoadMoreHolder.NULL_DATA_STATE);
            adapter?.addListData(list)
            normalAdapterManager?.loadMoreComplete()
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
