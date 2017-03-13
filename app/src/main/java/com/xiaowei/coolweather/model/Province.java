package com.xiaowei.coolweather.model;

/**
 * Created by xiaowei on 2017/3/13.
 * 创建省实体类
 */

public class Province {
    private  int  id;
    private   String  provincename;
    private   String   provincecode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename;
    }

    public String getProvincecode() {
        return provincecode;
    }

    public void setProvincecode(String provincecode) {
        this.provincecode = provincecode;
    }
}
