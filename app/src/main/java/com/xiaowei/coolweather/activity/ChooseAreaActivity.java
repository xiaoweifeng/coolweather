package com.xiaowei.coolweather.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaowei.coolweather.R;
import com.xiaowei.coolweather.db.CoolWeatherDB;
import com.xiaowei.coolweather.model.City;
import com.xiaowei.coolweather.model.Country;
import com.xiaowei.coolweather.model.Province;
import com.xiaowei.coolweather.util.HttpCallbackListener;
import com.xiaowei.coolweather.util.HttpUtil;
import com.xiaowei.coolweather.util.Utility;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaowei on 2017/3/13.
 */

public class ChooseAreaActivity extends Activity {
    public   static   final   int  PROVINCE_LEVEL=0;
    public   static   final   int  CITY_LEVEL=1;
    public   static   final   int  COUNTRY_LEVEL = 2;
    private ProgressDialog  progressDialog;
    private TextView  titletext;
    private ListView   listView;
    private ArrayAdapter<String>  adapter;
    private CoolWeatherDB  coolWeatherDB;
    private  List<String>  datalist = new ArrayList<String>();
    private List<Province>   provinceList;
    private  List<City>  cityList;
    private  List<Country>  countryList;
    private   Province  selectedprovince;
    private   City  selectedcity;
    private  int  currentlevel;
    private   boolean isfromweatherlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isfromweatherlayout = getIntent().getBooleanExtra("from_weather_activity",false);
        SharedPreferences   preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(preferences.getBoolean("city_selected",false)&&!isfromweatherlayout)
        {
            Intent  intent = new Intent(this,Weather_Activity.class);
            startActivity(intent);
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
         listView = (ListView) findViewById(R.id.list_view);
          titletext = (TextView) findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, datalist);
        listView.setAdapter(adapter);
        coolWeatherDB =  CoolWeatherDB.getinstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currentlevel==PROVINCE_LEVEL)
                {
                    selectedprovince = provinceList.get(i);
                    querycities();
                }else  if(currentlevel==CITY_LEVEL){
                    selectedcity = cityList.get(i);
                            querycountries();}
                else if(currentlevel==COUNTRY_LEVEL)
                {
                    String  countrycode =  countryList.get(i).getCountrycode();
                    Intent  intent =  new Intent(ChooseAreaActivity.this,Weather_Activity.class);
                    intent.putExtra("country_code",countrycode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        queryprovince();
    }

    private void queryprovince() {
        provinceList = coolWeatherDB.loadprovince();
        if(provinceList.size()>0)
        {
            datalist.clear();
            for(Province  province:provinceList)
            {
                datalist.add(province.getProvincename());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titletext.setText("中国");
            currentlevel =  PROVINCE_LEVEL;

        }
        else {
            queryfromserver(null,"province");
        }
    }

    private void querycities() {
      cityList = coolWeatherDB.loadcity(selectedprovince.getId());
        if(cityList.size()>0)
        {
            datalist.clear();
            for(City  city:cityList)
            {
                datalist.add(city.getCityname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titletext.setText(selectedprovince.getProvincename());
            currentlevel=CITY_LEVEL;
        }else
        {
            queryfromserver(selectedprovince.getProvincecode(),"city");
        }
    }

    private void querycountries() {
        countryList = coolWeatherDB.loadcountry(selectedcity.getId());
        if(countryList.size()>0)
        {
            datalist.clear();
            for(Country  country:countryList)
            {
                datalist.add(country.getCountryname());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titletext.setText(selectedcity.getCityname());
            currentlevel=COUNTRY_LEVEL;
        }
        else {
            queryfromserver(selectedcity.getCitycode(),"country");

        }
    }


    private void queryfromserver(final  String code,final String type) {
                String  address;
          if(!TextUtils.isEmpty(code))
          {
              address=  "http://www.weather.com.cn/data/list3/city"+code+".xml";
          }
        else
          {
              address = "http://www.weather.com.cn/data/list3/city.xml";
          }
        showprogressdialog();
        HttpUtil.sendrequest(address, new HttpCallbackListener() {
            @Override
            public void onfinish(String response) {
                boolean  result = false;
                if("province".equals(type))
                {
                    result = Utility.handleprovinceresponse(coolWeatherDB,response);
                }
                else  if("city".equals(type))
                {
                    result =  Utility.handlecityresponse(coolWeatherDB,response,selectedprovince.getId());
                }
                else  if("country".equals(type))
                {
                    result=  Utility.handlecountryresponse(coolWeatherDB,response,selectedcity.getId());
                }
                if(result)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeprogressdialog();
                            if("province".equals(type))
                            {
                                queryprovince();
                            }
                            else if("city".equals(type))
                            {
                                querycities();
                            }
                            else  if("country".equals(type))
                            {
                                querycountries();
                            }
                        }
                    });
                }
            }

            @Override
            public void onerror(Exception e) {
              runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                      closeprogressdialog();
                      Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                  }
              });
            }
        });
    }

    private void closeprogressdialog() {
         if(progressDialog!=null)
         {
             progressDialog.dismiss();
         }
    }

    private void showprogressdialog() {
        if(progressDialog==null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {

        if(currentlevel==COUNTRY_LEVEL)
            querycities();
        else  if(currentlevel==CITY_LEVEL)
            queryprovince();
        else {
     if(isfromweatherlayout){
                Intent  intent = new Intent(this,Weather_Activity.class);
         startActivity(intent);
            }
            finish();
        }
    }
}
