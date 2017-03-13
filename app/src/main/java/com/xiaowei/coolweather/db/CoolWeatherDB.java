package com.xiaowei.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.camera2.CameraCaptureSession;
import android.icu.text.RelativeDateTimeFormatter;
import android.icu.text.UCharacterIterator;
import android.provider.ContactsContract;

import com.xiaowei.coolweather.model.City;
import com.xiaowei.coolweather.model.Country;
import com.xiaowei.coolweather.model.Province;

import org.w3c.dom.ProcessingInstruction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaowei on 2017/3/13.
 */

public class CoolWeatherDB {
    public  static  final   String  DB_NAME = "cool_weather";
    public   static   final   int  VERSION =1;
    private  static  CoolWeatherDB   coolDB;
    private SQLiteDatabase   db;
    private   CoolWeatherDB(Context  context)//创建私有化的初始化方法；
    {
        CoolWeatherOpenHelper   dbhelper =  new CoolWeatherOpenHelper(context,DB_NAME,null,VERSION);
        db =  dbhelper.getWritableDatabase();
    }
    public   synchronized   static   CoolWeatherDB   getinstance(Context  context)//创建coolweatherdb的获取实例的方法；
    {
        if(coolDB==null)
        {
            coolDB =   new CoolWeatherDB(context);
        }
        return   coolDB;
    }
    public   void     saveprovince(Province province)//将省份信息存入表中；
    {
        if(province!=null)
        {
            ContentValues  values  =  new ContentValues();
            values.put("province_name",province.getProvincename());
            values.put("province_code",province.getProvincecode());
            db.insert("Province",null,values);
        }
    }
    public List<Province>   loadprovince()//从表中查询相应的数据；
    {
        List<Province>   provinces =  new ArrayList<Province>();
        Cursor  cursor =  db.query("Province",null,null,null,null,null,null);
        if(cursor.moveToFirst())
        {
            do {
                Province  province  =  new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvincename(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvincecode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
            }while (cursor.moveToNext());
        }
        if(cursor!=null)
        {
            cursor.close();
        }
        return   provinces;
    }
    public   void  savecity(City  city)//将市的信息存储到表中；
    {
        if(city!=null)
        {
           ContentValues  values  =  new ContentValues();
            values.put("city_name",city.getCityname());
            values.put("city_code",city.getCitycode());
            values.put("province_id",city.getProvinceid());
            db.insert("City",null,values);
        }
    }
    public   List<City>   loadcity(int  proviceid)//从表中读取市的信息；
    {
       List<City>  cities =  new ArrayList<City>();
        Cursor  cursor = db.query("City",null,"province_id=?",new String[]{String.valueOf(proviceid)},null,null,null);
        if(cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityname(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCitycode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceid(proviceid);
                cities.add(city);
            } while (cursor.moveToNext());
        }
        if(cursor!=null)
            cursor.close();
        return   cities;
    }
    public   void    savecountry(Country  country)
    {
        if(country!=null)
        {
            ContentValues  values = new ContentValues();
            values.put("country_name",country.getCountryname());
            values.put("country_code",country.getCountrycode());
            values.put("city_id",country.getCityid());
            db.insert("Country",null,values);
        }
    }
    public   List<Country>  loadcountry(int cityid)
    {
        List<Country>  countries  =  new ArrayList<Country>();
        Cursor   cursor =  db.query("Country",null,"city_id=?",new String[]{String.valueOf(cityid)},null,null,null);
        if(cursor.moveToFirst())
        {
            do {
                Country  country  = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("id")));
                country.setCountrycode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCountryname(cursor.getString(cursor.getColumnIndex("country_name")));
                country.setCityid(cityid);
                countries.add(country);
            }while (cursor.moveToNext());
        }
        if(cursor!=null)
        {
            cursor.close();
        }
        return   countries;
    }


}
