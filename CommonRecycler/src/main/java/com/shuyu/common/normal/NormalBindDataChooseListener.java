package com.shuyu.common.normal;

import java.util.List;

/**
 * Created by guoshuyu on 2017/8/29.
 */

public interface NormalBindDataChooseListener {
    int getCurrentDataLayoutId(Object object, Class classType, int position, List<Integer> ids);
}
