package example.shuyu.recycler.kotlin.bind.activity

import android.content.Context
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.shuyu.commonrecycler.BindRecyclerAdapter
import com.shuyu.commonrecycler.BindSuperAdapterManager
import com.shuyu.commonrecycler.decoration.BindDecorationBuilder
import com.shuyu.commonrecycler.listener.OnItemClickListener
import kotlinx.android.synthetic.main.activity_normal_recycler_layout.*

import java.util.ArrayList


/**
 * 利用BindRecyclerAdapter实现，不带上下拉
 */
class BindNormalActivity : AppCompatActivity() {

    private var datas  = ArrayList<Any>()

    private var adapter: BindRecyclerAdapter? = null

    private var normalAdapterManager: BindSuperAdapterManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_normal_recycler_layout)
        init()
        initDatas()
    }

    fun init() {

        normalAdapterManager = BindSuperAdapterManager()
        normalAdapterManager!!
                .bind(BindImageModel::class.java, BindImageHolder.ID, BindImageHolder::class.java)
                .bind(BindTextModel::class.java, BindTextHolder.ID, BindTextHolder::class.java)
                .bind(BindMutliModel::class.java, BindMutliHolder.ID, BindMutliHolder::class.java)
                .bind(BindClickModel::class.java, BindClickHolder.ID, BindClickHolder::class.java)
                .bindEmpty(BindNoDataHolder.NoDataModel::class.java, BindNoDataHolder.ID, BindNoDataHolder::class.java)
                .setNeedAnimation(true)
                .setOnItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(context: Context, position: Int) {
                        Toast.makeText(context, "点击了！！　" + position, Toast.LENGTH_SHORT).show()
                    }
                })


        adapter = BindRecyclerAdapter(this, normalAdapterManager!!, datas)

        recycler!!.layoutManager = LinearLayoutManager(this)


        //间隔线
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.color = resources.getColor(R.color.material_deep_teal_200)
        val effects = DashPathEffect(floatArrayOf(5f, 5f, 5f, 5f), 1f)
        paint.pathEffect = effects
        paint.strokeWidth = dip2px(this, 5f).toFloat()
        recycler!!.addItemDecoration(BindDecorationBuilder(adapter!!).setPaint(paint).setSpace(dip2px(this, 5f)).builder())

        recycler!!.adapter = adapter

    }


    fun initDatas() {
        val list = BindDataUtils.refreshData
        this.datas = list
        if (adapter != null) {
            adapter!!.setListData(datas)
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
