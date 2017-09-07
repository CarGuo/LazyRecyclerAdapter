package com.shuyu.apprecycler.chat.detail.view;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.textutillib.EmojiLayout;
import com.shuyu.textutillib.SmileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guoshuyu on 2017/9/7.
 */

public class ChatDetailEmojiLayout extends EmojiLayout {

    private static boolean sInitEmojiData = false;

    public ChatDetailEmojiLayout(Context context) {
        super(context);
    }

    public ChatDetailEmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatDetailEmojiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 处理表情,在控件加载之前初始化
     */
    public static void initEmoji(Context context) {
        if (!sInitEmojiData) {
            sInitEmojiData = true;
            List<Integer> data = new ArrayList<>();
            List<String> strings = new ArrayList<>();
            for (int i = 1; i < 64; i++) {
                int resId = context.getResources().getIdentifier("e" + i, "drawable", context.getPackageName());
                data.add(resId);
                strings.add("[e" + i + "]");
            }
            SmileUtils.addPatternAll(SmileUtils.getEmoticons(), strings, data);
        }
    }

}
