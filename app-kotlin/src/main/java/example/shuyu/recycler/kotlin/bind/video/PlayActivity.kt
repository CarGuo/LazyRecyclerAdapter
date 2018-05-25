package example.shuyu.recycler.kotlin.bind.video

import android.annotation.TargetApi
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils


import example.shuyu.recycler.kotlin.R
import kotlinx.android.synthetic.main.activity_play.*

/**
 * 单独的视频播放页面
 * Created by shuyu on 2016/11/11.
 */
class PlayActivity : AppCompatActivity() {


    internal var orientationUtils: OrientationUtils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        init()
    }

    private fun init() {
        val url = "https://res.exexm.com/cw_145225549855002"

        //借用了jjdxm_ijkplayer的URL
        val source1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"

        videoPlayer.setUp(source1, true, "测试视频")

        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE)

        //设置旋转
        orientationUtils = OrientationUtils(this, videoPlayer)

        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(View.OnClickListener { orientationUtils!!.resolveByClick() })

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true)

        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(View.OnClickListener { onBackPressed() })

        //过渡动画
        initTransition()
    }


    override fun onPause() {
        super.onPause()
        videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer.onVideoResume()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    override fun onDestroy() {
        super.onDestroy()
        if (orientationUtils != null)
            orientationUtils!!.releaseListener()
    }

    override fun onBackPressed() {
        //先返回正常状态
        if (orientationUtils!!.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick()
            return
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
        super.onBackPressed()
    }


    private fun initTransition() {
        videoPlayer.startPlayLogic()
    }

}
