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
open class ChatDetailManagerModule(private val onItemClickListener: OnItemClickListener, private val loadingListener: OnLoadingListener, private val  onTouchListener: View.OnTouchListener) {

    @Provides
    fun provideOnItemClickListener(): OnItemClickListener = onItemClickListener

    @Provides
    fun provideOnLoadingListener(): OnLoadingListener = loadingListener

    @Provides
    fun provideOnTouchListener(): View.OnTouchListener = onTouchListener
}
