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
    private String storeCode;  //门店编码
    private String storeName;  //门店名称
    private String address;     //地址

    @OneToOne
    @JoinColumn(name = "marketCode")
    private MarketEntity marketCode;
    private String marger;     //(有些门店没有门店经理，再列表中展示时需要判断)

    //门店状态
    @OneToOne
    @JoinColumn(name = "storeStatus")
    private StoreStatus storeStatus;

    @Temporal(TemporalType.DATE)
    private Date openDate;

    //设置门店的设备(items)
    @ManyToMany(targetEntity = Items.class,fetch = FetchType.EAGER)
    @JoinTable(name = "store_items",joinColumns = @JoinColumn(name = "store_id"),inverseJoinColumns = @JoinColumn(name = "items_id"))
    private Set<Items> itemsSet;

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

    public MarketEntity getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(MarketEntity marketCode) {
        this.marketCode = marketCode;
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
