package com.xiaowei.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xiaowei.coolweather.service.AutoUpdateService;

/**
 * Created by xiaowei on 2017/3/15.
 */

public class AutoUpdateReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent  i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
