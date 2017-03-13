package com.xiaowei.coolweather.util;

import android.icu.text.RelativeDateTimeFormatter;
import android.text.TextUtils;

import com.xiaowei.coolweather.db.CoolWeatherDB;
import com.xiaowei.coolweather.model.City;
import com.xiaowei.coolweather.model.Country;
import com.xiaowei.coolweather.model.Province;

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
}
