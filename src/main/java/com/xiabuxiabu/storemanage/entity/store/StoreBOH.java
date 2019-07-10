package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class StoreBOH {
    @Id
    private String storecode;  //门店编码
    private String storename;  //门店名称
    private String address;     //地址

    private String marketname;
    private String marger;     //(有些门店没有门店经理，再列表中展示时需要判断)
    @Temporal(TemporalType.DATE)
    private Date opendate;
    private String band;

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getStorecode() {
        return storecode;
    }

    public void setStorecode(String storecode) {
        this.storecode = storecode;
    }

    public String getStorename() {
        return storename;
    }

    public void setStorename(String storename) {
        this.storename = storename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMarketname() {
        return marketname;
    }

    public void setMarketname(String marketname) {
        this.marketname = marketname;
    }

    public String getMarger() {
        return marger;
    }

    public void setMarger(String marger) {
        this.marger = marger;
    }

    public Date getOpendate() {
        return opendate;
    }

    public void setOpendate(Date opendate) {
        this.opendate = opendate;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
