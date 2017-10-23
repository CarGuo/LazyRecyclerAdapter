package example.shuyu.recycler.kotlin

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import example.shuyu.recycler.kotlin.bind.BindHomeActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by guoshuyu on 2017/8/29.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        normalBtn.setOnClickListener { startActivity(Intent(this@MainActivity, BindHomeActivity::class.java)) }
    }
}
