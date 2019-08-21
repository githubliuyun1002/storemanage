package com.xiabuxiabu.storemanage.entity.ccequip;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CCEquipInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String storeCode;
    private String storeName;
    private String marketName;
    private String equipName;
    private int days;
    @Temporal(TemporalType.DATE)
    private Date weeks;

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Date getWeeks() {
        return weeks;
    }

    public void setWeeks(Date weeks) {
        this.weeks = weeks;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }

}
