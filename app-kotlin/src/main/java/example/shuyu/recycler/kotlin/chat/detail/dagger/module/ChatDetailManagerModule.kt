package example.shuyu.recycler.kotlin.chat.detail.dagger.module

import android.view.View
import android.widget.AdapterView
import com.shuyu.commonrecycler.listener.OnItemClickListener

import com.shuyu.commonrecycler.listener.OnLoadingListener

import dagger.Module
import dagger.Provides

/**
 * Created by guoshuyu on 2017/9/7.
 */
@Module
open class ChatDetailManagerModule(private val mOnItemClickListener: OnItemClickListener, private val mLoadingListener: OnLoadingListener, private val mOnTouchListener: View.OnTouchListener) {

    @Provides
    fun provideOnItemClickListener(): OnItemClickListener {
        return mOnItemClickListener
    }

    @Provides
    fun provideOnLoadingListener(): OnLoadingListener {
        return mLoadingListener
    }

    @Provides
    fun provideOnTouchListener(): View.OnTouchListener {
        return mOnTouchListener
    }
}
