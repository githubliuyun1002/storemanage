package com.xiabuxiabu.storemanage.entity.ccstore;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.ccequip.ccItems;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;

import javax.persistence.*;
import java.util.Set;

@Entity
public class CCStore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int storeId;
    private String address;
    private String marketName;
    private String storeCode;
    private String storeName;
    private String storeManage;
    private String storeMail;

    @ManyToMany(targetEntity = ccItems.class,fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
    @JoinTable(name = "ccstore_items",joinColumns = @JoinColumn(name = "ccstore_id"),inverseJoinColumns = @JoinColumn(name = "items_id"))
    private Set<ccItems> ccItemsSet;


    public String getStoreMail() {
        return storeMail;
    }

    public void setStoreMail(String storeMail) {
        this.storeMail = storeMail;
    }

    public Set<ccItems> getCcItemsSet() {
        return ccItemsSet;
    }

    public void setCcItemsSet(Set<ccItems> ccItemsSet) {
        this.ccItemsSet = ccItemsSet;
    }

    public String getStoreManage() {
        return storeManage;
    }

    public void setStoreManage(String storeManage) {
        this.storeManage = storeManage;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }

}
