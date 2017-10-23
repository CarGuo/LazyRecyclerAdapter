package example.shuyu.recycler.kotlin.bind.holder

import android.content.Context
import android.view.View

import example.shuyu.recycler.kotlin.R
import com.shuyu.commonrecycler.BindRecyclerBaseHolder

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 *
 *
 * 空数据Holder
 * Created by shuyu on 2016/11/23.
 */

open class BindNoDataHolder (context: Context, v: View) : BindRecyclerBaseHolder(context, v) {

    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    override fun createView(v: View) {}

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    override fun onBind(model: Any, position: Int) {}

    class NoDataModel

    companion object {

        val ID = R.layout.no_data
    }
}
