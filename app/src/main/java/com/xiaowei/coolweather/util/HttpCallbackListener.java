package com.xiaowei.coolweather.util;

/**
 * Created by xiaowei on 2017/3/13.
 */

public interface HttpCallbackListener {
    void  onfinish(String  response);
    void  onerror(Exception  e);
}
