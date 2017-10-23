package example.shuyu.recycler.kotlin.bind.holder

import android.view.View
import android.widget.ImageView

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.bind.model.BindMutliModel
import com.shuyu.commonrecycler.BindRecyclerBaseHolder

/**
 * 继承BindRecyclerBaseHolder的Holder，可用在普通recyclerView的adapter
 * Created by guoshuyu on 2017/8/29.
 */

open class BindMutliHolder (v: View) : BindRecyclerBaseHolder(v) {

    lateinit var itemImage1: ImageView

    lateinit var itemImage2: ImageView

    /**
     * 必须实现的方法，onCreateViewHolder时调用，
     *
     * @param v layoutId实例后的View
     */
    override fun createView(v: View) {
        itemImage1 = v.findViewById(R.id.item_image_1) as ImageView
        itemImage2 = v.findViewById(R.id.item_image_2) as ImageView
    }

    /**
     * 必须实现的方法，onBindViewHolder时调用
     *
     * @param model    对应holder的数据实体
     * @param position 位置
     */
    override fun onBind(model: Any, position: Int) {
        val mutliModel = model as BindMutliModel
        itemImage1.setImageResource(mutliModel.resId)
        itemImage2.setImageResource(mutliModel.res2)
    }

    companion object {

        val ID = R.layout.mutil_item
    }
}
