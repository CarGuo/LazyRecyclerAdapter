package example.shuyu.recycler.kotlin.bind.holder

import android.view.View
import android.widget.TextView

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.bind.model.BindTextModel
import com.shuyu.commonrecycler.BindRecyclerBaseHolder

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 *
 *
 * Created by guoshuyu on 2017/8/29.
 */

open class BindTextHolder (v: View) : BindRecyclerBaseHolder(v) {

    lateinit var itemText: TextView

    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    override fun createView(v: View) {
        itemText = v.findViewById(R.id.item_text) as TextView
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    override fun onBind(model: Any, position: Int) {
        val textModel = model as BindTextModel
        itemText.text = textModel.text
    }

    companion object {

        val ID = R.layout.text_item
    }
}
