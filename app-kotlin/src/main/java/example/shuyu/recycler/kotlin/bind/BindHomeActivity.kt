package example.shuyu.recycler.kotlin.bind

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

import example.shuyu.recycler.kotlin.R
import example.shuyu.recycler.kotlin.bind.activity.BindDragSwipeActivity
import example.shuyu.recycler.kotlin.bind.activity.BindEmptyActivity
import example.shuyu.recycler.kotlin.bind.activity.BindGridActivity
import example.shuyu.recycler.kotlin.bind.activity.BindHorizontalGridActivity
import example.shuyu.recycler.kotlin.bind.activity.BindHorizontalRefreshLoadActivity
import example.shuyu.recycler.kotlin.bind.activity.BindHorizontalStaggeredRefreshLoadActivity
import example.shuyu.recycler.kotlin.bind.activity.BindNormalActivity
import example.shuyu.recycler.kotlin.bind.activity.BindRefreshLoadActivity
import example.shuyu.recycler.kotlin.bind.activity.BindStaggeredRefreshLoadActivity
import example.shuyu.recycler.kotlin.bind.utils.BindDataUtils


/**
 * 主页
 * Created by guoshuyu on 2017/8/29.
 */

class BindHomeActivity : AppCompatActivity(), AdapterView.OnItemClickListener {


    lateinit var homeList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        homeList = findViewById(R.id.home_list) as ListView

        homeList.onItemClickListener = this

        val listAdapter = ArrayAdapter<String>(this, R.layout.home_item, BindDataUtils.homeList)

        homeList.adapter = listAdapter

    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (position) {
            0 -> {
                val intent = Intent(this@BindHomeActivity, BindNormalActivity::class.java)
                startActivity(intent)
            }
            1 -> {
                val intent = Intent(this@BindHomeActivity, BindRefreshLoadActivity::class.java)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this@BindHomeActivity, BindStaggeredRefreshLoadActivity::class.java)
                startActivity(intent)
            }
            3 -> {
                val intent = Intent(this@BindHomeActivity, BindEmptyActivity::class.java)
                startActivity(intent)
            }
            4 -> {
                val intent = Intent(this@BindHomeActivity, BindGridActivity::class.java)
                startActivity(intent)
            }
            5 -> {
                val intent = Intent(this@BindHomeActivity, BindHorizontalRefreshLoadActivity::class.java)
                startActivity(intent)
            }
            6 -> {
                val intent = Intent(this@BindHomeActivity, BindHorizontalGridActivity::class.java)
                startActivity(intent)
            }
            7 -> {
                val intent = Intent(this@BindHomeActivity, BindHorizontalStaggeredRefreshLoadActivity::class.java)
                startActivity(intent)
            }
            8 -> {
                val intent = Intent(this@BindHomeActivity, BindDragSwipeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
