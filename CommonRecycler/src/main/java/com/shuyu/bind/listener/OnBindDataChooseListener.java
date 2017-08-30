package com.shuyu.bind.listener;

import java.util.List;

/**
 * 多个layoutId的情况下，判断该model使用哪个id
 * Created by guoshuyu on 2017/8/29.
 */

public interface OnBindDataChooseListener {
    int getCurrentDataLayoutId(Object object, Class classType, int position, List<Integer> ids);
}
