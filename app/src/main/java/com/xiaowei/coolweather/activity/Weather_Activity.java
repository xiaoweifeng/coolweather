package com.xiaowei.coolweather.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaowei.coolweather.R;
import com.xiaowei.coolweather.service.AutoUpdateService;
import com.xiaowei.coolweather.util.HttpCallbackListener;
import com.xiaowei.coolweather.util.HttpUtil;
import com.xiaowei.coolweather.util.Utility;

/**
 * Created by xiaowei on 2017/3/14.
 */

public class Weather_Activity extends Activity implements View.OnClickListener{
    private LinearLayout  weatherlayoutinfo;
    private TextView  citynametext;
    private  TextView  publishtext;
    private  TextView  weatherdesptext;
    private  TextView  temp1text;
    private   TextView  temp2text;
    private  TextView  currentdatetext;
    private Button   switch_city;
    private   Button refrensh_weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherlayoutinfo  = (LinearLayout) findViewById(R.id.weatherinfo_layout);
        citynametext  = (TextView) findViewById(R.id.city_name);
        publishtext = (TextView) findViewById(R.id.publish_text);
        weatherdesptext = (TextView) findViewById(R.id.weather_desp);
        temp1text = (TextView) findViewById(R.id.temp1);
        temp2text = (TextView) findViewById(R.id.temp2);
        currentdatetext  = (TextView) findViewById(R.id.current_date);
        switch_city = (Button) findViewById(R.id.switch_city);
        refrensh_weather = (Button) findViewById(R.id.refresh_weather);
        switch_city.setOnClickListener(this);
        refrensh_weather.setOnClickListener(this);
        String  countrycode = getIntent().getStringExtra("country_code");
        if(!TextUtils.isEmpty(countrycode))
        {
            publishtext.setText("同步中...");
            weatherlayoutinfo.setVisibility(View.INVISIBLE);
            citynametext.setVisibility(View.INVISIBLE);
            queryweathercode(countrycode);//根据县级代码来查询相应的天气信息；

        }
        else {
            showweather();//没有县级代码时查询本地天气；
        }
    }

    private void showweather() {
        SharedPreferences  preferences = PreferenceManager.getDefaultSharedPreferences(this);
        citynametext.setText(preferences.getString("city_name",""));
        temp1text.setText(preferences.getString("temp1",""));
        temp2text.setText(preferences.getString("temp2",""));
        weatherdesptext.setText(preferences.getString("weather_desp",""));
        publishtext.setText("今天"+preferences.getString("publish_time","")+"发布");
        currentdatetext.setText(preferences.getString("current_date",""));
        weatherlayoutinfo.setVisibility(View.VISIBLE);
        citynametext.setVisibility(View.VISIBLE);
        Intent  intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }

    /**
     *查询县级代号对应的天气代号；
     * @param countrycode
     */
    private void queryweathercode(String countrycode) {
     String  address =  "http://www.weather.com.cn/data/list3/city"+countrycode+".xml";
        queryfromserver(address,"countrycode");
    }

    /**
     * 查询天气代号所对应的天气；
     */
    private   void  queryweatherinfo(String  weathercode)
    {
     String address = "http://www.weather.com.cn/data/cityinfo/"+weathercode+".html";
  queryfromserver(address,"weathercode");
    }

    /**
     *
     * @param address
     * @param countrycode
     * 根据传入的地址和类型去查询相应的天气代号或者是天气信息；
     */

    private void queryfromserver(final String address,final String countrycode) {
        HttpUtil.sendrequest(address, new HttpCallbackListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onfinish(String response) {
                if("countrycode".equals(countrycode))
                {
                    if(!TextUtils.isEmpty(response))
                    {
                        String[]  array =  response.split("\\|");
                        if(array!=null&&array.length==2)
                        {
                            String  weathercode = array[1];
                            queryweatherinfo(weathercode);
                        }
                    }
                }
                else if("weathercode".equals(countrycode))
                {
                    Utility.handleweatherresponse(Weather_Activity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showweather();
                        }
                    });
                }
            }

            @Override
            public void onerror(Exception e) {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         publishtext.setText("同步失败");
                     }
                 });
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case  R.id.switch_city:
                Intent  intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity",true);
                startActivity(intent);
                finish();
                break;
            case   R.id.refresh_weather:
                publishtext.setText("同步中...");
                SharedPreferences  preferences =  PreferenceManager.getDefaultSharedPreferences(this);
                String   weathercode=preferences.getString("weather_code","");
                if(!TextUtils.isEmpty(weathercode))
                {
                    queryweatherinfo(weathercode);
                }
                break;
            default:
                break;
        }
    }
}
