package example.shuyu.recycler.kotlin.chat.detail.view

import android.content.Context
import android.util.AttributeSet

import com.shuyu.textutillib.EmojiLayout
import com.shuyu.textutillib.SmileUtils

import java.util.ArrayList

/**
 * Created by guoshuyu on 2017/9/7.
 */

open class ChatDetailEmojiLayout : EmojiLayout {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    companion object {

        private var sInitEmojiData = false


        /**
         * 处理表情,在控件加载之前初始化
         */
        fun initEmoji(context: Context) {
            if (!sInitEmojiData) {
                sInitEmojiData = true
                val data = ArrayList<Int>()
                val strings = ArrayList<String>()
                for (i in 1..63) {
                    val resId = context.resources.getIdentifier("e" + i, "drawable", context.packageName)
                    data.add(resId)
                    strings.add("[e$i]")
                }
                SmileUtils.addPatternAll(SmileUtils.getEmoticons(), strings, data)
            }
        }
    }

}
