package com.xiaowei.coolweather.model;

/**
 * Created by xiaowei on 2017/3/13.
 * 创建市实体类
 */

public class City {
    private   int  id;
    private  String  cityname;
    private  String  citycode;
    private   int     provinceid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public int getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(int provinceid) {
        this.provinceid = provinceid;
    }
}
