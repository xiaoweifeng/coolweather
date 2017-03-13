package com.xiaowei.coolweather.model;

/**
 * Created by xiaowei on 2017/3/13.
 * 创建县实体类；
 */

public class Country {
    private  int id;
    private   String  countryname;
    private  String  countrycode;
    private   int   cityid;

    public int getCityid() {
        return cityid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }


}
