package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.beans.IDProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * 门店日志表
 * 记录设备由 0---》有
 * 记录门店确认后市场It----》调整状态
 * 在记录闭店时候的清0的状态
 */
@Entity
public class StoreRemarks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int srId;
    private String storeName;
    private String storeCode;
    private String marketName;
    private String itemName;
    private int itemId;
    //操作人
    private String operatePerson;
    //审批人
    private String checkMan;
    //原来的数量
    private int orignnum;
    //修改过的数量
    private int nownum;
    //变化的数量
    private int changenum;
    //搜索的关键词
    private String storeAnditem;
    //修改设备数量的时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    //设备审批时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date checkTime;


    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getNownum() {
        return nownum;
    }

    public void setNownum(int nownum) {
        this.nownum = nownum;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getSrId() {
        return srId;
    }

    public void setSrId(int srId) {
        this.srId = srId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getOperatePerson() {
        return operatePerson;
    }

    public void setOperatePerson(String operatePerson) {
        this.operatePerson = operatePerson;
    }

    public String getCheckMan() {
        return checkMan;
    }

    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan;
    }

    public int getOrignnum() {
        return orignnum;
    }

    public void setOrignnum(int orignnum) {
        this.orignnum = orignnum;
    }

    public int getChangenum() {
        return changenum;
    }

    public void setChangenum(int changenum) {
        this.changenum = changenum;
    }

    public String getStoreAnditem() {
        return storeAnditem;
    }

    public void setStoreAnditem(String storeAnditem) {
        this.storeAnditem = storeAnditem;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
