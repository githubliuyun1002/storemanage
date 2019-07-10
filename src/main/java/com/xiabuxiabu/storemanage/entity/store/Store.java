package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.equip.Items;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**

* @Description:    java类门店实体类
*/
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int storeId;
    private String storeCode;           //门店编码
    private String storeName;           //门店名称
    private String address;             //地址
    private String marketName;   //所属市场
    private String marger;             //(有些门店没有门店经理，再列表中展示时需要判断)
    private String band;            //品牌
    //门店状态
    @OneToOne
    @JoinColumn(name = "storeStatus")
    private StoreStatus storeStatus;
    //开店日期
    @Temporal(TemporalType.DATE)
    private Date openDate;
    //闭店日期
    @Temporal(TemporalType.DATE)
    private Date closeDate;
    //设置闭店标志
    private String closeSign;
    //带宽信息(对应多个宽带信息)
    @ManyToMany(targetEntity = WidthBand.class,fetch = FetchType.EAGER)
    @JoinTable(name = "store_widthband",joinColumns = @JoinColumn(name = "store_id"),inverseJoinColumns = @JoinColumn(name = "widthband_id"))
    private Set<WidthBand> widthBandSet;

    //设置门店的设备(items)
    @ManyToMany(targetEntity = Items.class,fetch = FetchType.EAGER)
    @JoinTable(name = "store_items",joinColumns = @JoinColumn(name = "store_id"),inverseJoinColumns = @JoinColumn(name = "items_id"))
    private Set<Items> itemsSet;


    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    public String getCloseSign() {
        return closeSign;
    }

    public void setCloseSign(String closeSign) {
        this.closeSign = closeSign;
    }

    public Set<WidthBand> getWidthBandSet() {
        return widthBandSet;
    }

    public void setWidthBandSet(Set<WidthBand> widthBandSet) {
        this.widthBandSet = widthBandSet;
    }

    public StoreStatus getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getMarger() {
        return marger;
    }

    public void setMarger(String marger) {
        this.marger = marger;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Set<Items> getItemsSet() {
        return itemsSet;
    }

    public void setItemsSet(Set<Items> itemsSet) {
        this.itemsSet = itemsSet;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
