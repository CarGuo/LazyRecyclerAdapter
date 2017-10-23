package example.shuyu.recycler.kotlin.bind.holder

import android.view.View
import android.widget.Button

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.bind.model.BindClickModel
import com.shuyu.commonrecycler.BindRecyclerBaseHolder

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 * Created by guoshuyu on 2017/8/29.
 */

open class BindClickHolder (v: View) : BindRecyclerBaseHolder(v) {

    lateinit var itemButton: Button

    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    override fun createView(v: View) {
        itemButton = v.findViewById(R.id.item_button) as Button
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    override fun onBind(model: Any, position: Int) {
        val clickModel = model as BindClickModel
        itemButton.text = clickModel.btnText
    }

    companion object {

        val ID = R.layout.click_item
    }
}
