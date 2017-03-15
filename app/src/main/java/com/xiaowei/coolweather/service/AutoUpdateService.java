package com.xiaowei.coolweather.service;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.xiaowei.coolweather.receiver.AutoUpdateReciver;
import com.xiaowei.coolweather.util.HttpCallbackListener;
import com.xiaowei.coolweather.util.HttpUtil;
import com.xiaowei.coolweather.util.Utility;

/**
 * Created by xiaowei on 2017/3/15.
 */

public class AutoUpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
         new  Thread(new Runnable() {
             @Override
             public void run() {
                 updateweather();
             }
         }).start();
        AlarmManager   manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int  hours =  8*60*60*1000;
        long  triggerattime = SystemClock.elapsedRealtime()+hours;
        Intent i =  new Intent(this, AutoUpdateReciver.class);
        PendingIntent  pi = PendingIntent.getBroadcast(this,0,i,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerattime,pi);
        return super.onStartCommand(intent, flags, startId);

    }

    private void updateweather() {
        SharedPreferences   preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String   weathercode =  preferences.getString("weather_code","");
        String  address = "http://www.weather.com.cn/data/cityinfo/"+weathercode+".html";
        HttpUtil.sendrequest(address, new HttpCallbackListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onfinish(String response) {
               Utility.handleweatherresponse(AutoUpdateService.this,response);
            }

            @Override
            public void onerror(Exception e) {
               e.printStackTrace();
            }
        });
    }
}
