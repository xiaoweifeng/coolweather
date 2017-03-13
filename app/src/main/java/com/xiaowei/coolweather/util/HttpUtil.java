package com.xiaowei.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by xiaowei on 2017/3/13.
 */
//创建相应的服务连接类;发送相应的服务请求，进行封装。
public class HttpUtil {
    public   static   void sendrequest(final   String  address,final   HttpCallbackListener  listener)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection  connection =  null;
                try {
                    URL  url = new URL(address);
                    connection  = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream  in =  connection.getInputStream();
                    BufferedReader   reader =  new BufferedReader(new InputStreamReader(in));
                    StringBuilder  response =  new StringBuilder();
                    String  line;
                    while ((line=reader.readLine())!=null)
                    {
                        response.append(line);
                    }
                    if(listener!=null)
                    {
                        listener.onfinish(response.toString());
                    }
                } catch (Exception e) {
                    if(listener!=null)
                        listener.onerror(e);
                } finally {
                    if(connection!=null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}
