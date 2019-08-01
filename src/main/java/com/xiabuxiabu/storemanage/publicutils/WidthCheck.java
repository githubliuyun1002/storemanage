package com.xiabuxiabu.storemanage.publicutils;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class WidthCheck {
   // private int id;
    private Double payMoney;   //付款金额
    private String identity;   //账号
    private String password;   //密码
    private String tapewidth;  //带宽
    private String endDate;

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    //    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }

    public Double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTapewidth() {
        return tapewidth;
    }

    public void setTapewidth(String tapewidth) {
        this.tapewidth = tapewidth;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
