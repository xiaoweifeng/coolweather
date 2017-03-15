package com.xiaowei.coolweather.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.RelativeDateTimeFormatter;

import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.text.style.TtsSpan;

import com.xiaowei.coolweather.db.CoolWeatherDB;
import com.xiaowei.coolweather.model.City;
import com.xiaowei.coolweather.model.Country;
import com.xiaowei.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

/**
 * Created by xiaowei on 2017/3/13.
 * 解析从服务器返回的省市县的相关数据
 */

public class Utility {
    public synchronized   static   boolean  handleprovinceresponse(CoolWeatherDB   coolWeatherDB, String  response)
    {
        if(!TextUtils.isEmpty(response))
        {
            String[]  allprovinces = response.split(",");
            if(allprovinces!=null&&allprovinces.length>0)
            {
                for(String  p:allprovinces)
                {
                    String[]  array  =  p.split("\\|");
                    Province  province  = new Province();
                    province.setProvincecode(array[0]);
                    province.setProvincename(array[1]);
                    coolWeatherDB.saveprovince(province);
                }
                return  true;
            }
        }
        return   false;
    }
    public   static   boolean  handlecityresponse(CoolWeatherDB  coolWeatherDB,String response,int  provinceid)
    {
        if(!TextUtils.isEmpty(response))
        {
            String[]  allcities =  response.split(",");
            if(allcities!=null&&allcities.length>0)
            {
                for(String p:allcities)
                {
                    String[]  array=  p.split("\\|");
                    City  city =  new City();
                    city.setProvinceid(provinceid);
                    city.setCitycode(array[0]);
                    city.setCityname(array[1]);
                    coolWeatherDB.savecity(city);
                }
                return   true;
            }
        }
        return   false;
    }
    public   static   boolean   handlecountryresponse(CoolWeatherDB  coolWeatherDB,String  response,int  cityid)
    {
       if (!TextUtils.isEmpty(response))
       {
           String[]  allcountries = response.split(",");
           if(allcountries!=null&&allcountries.length>0)
           {
               for(String p:allcountries)
               {
                   String[]  array = p.split("\\|");
                   Country  country  =  new Country();
                   country.setCityid(cityid);
                   country.setCountrycode(array[0]);
                   country.setCountryname(array[1]);
                   coolWeatherDB.savecountry(country);
               }
               return   true;
           }
       }
        return   false;
    }
    /**
     * 解析从服务器返回的json数据，并将解析出的json数据存储在本地；
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public  static   void  handleweatherresponse(Context  context, String  response)
    {
        try {
            JSONObject  jsonObject =  new JSONObject(response);
             JSONObject   weatherinfo = jsonObject.getJSONObject("weatherinfo");
              String  cityname =  weatherinfo.getString("city");
              String  weathercode =  weatherinfo.getString("cityid");
              String  temp1=   weatherinfo.getString("temp1");
            String   temp2= weatherinfo.getString("temp2");
            String  weatherdesp = weatherinfo.getString("weather");
            String  publishtime = weatherinfo.getString("ptime");
             saveweatherinfo(context,cityname,weathercode,temp1,temp2,weatherdesp,publishtime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private static void saveweatherinfo(Context context, String cityname, String weathercode, String temp1, String temp2, String weatherdesp, String publishtime) {
        java.text.SimpleDateFormat sdf =  new java.text.SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor  editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected",true);
        editor.putString("city_name",cityname);
        editor.putString("weather_code",weathercode);
        editor.putString("temp1",temp1);
        editor.putString("temp2",temp2);
        editor.putString("weather_desp",weatherdesp);
        editor.putString("publish_time",publishtime);
        editor.putString("current_date",sdf.format(new Date()));
        editor.commit();
    }
}
