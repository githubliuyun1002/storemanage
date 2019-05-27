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
    @OneToOne
    @JoinColumn(name = "marketcode")
    private MarketEntity marketcode;
    private String marger;     //(有些门店没有门店经理，再列表中展示时需要判断)
    @Temporal(TemporalType.DATE)
    private Date opendate;

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

    public MarketEntity getMarketcode() {
        return marketcode;
    }

    public void setMarketcode(MarketEntity marketcode) {
        this.marketcode = marketcode;
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
