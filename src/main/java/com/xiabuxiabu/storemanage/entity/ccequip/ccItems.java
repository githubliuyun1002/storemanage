package com.xiabuxiabu.storemanage.entity.ccequip;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xiabuxiabu.storemanage.entity.ccstore.CCStore;
import java.util.Set;


import javax.persistence.*;
import java.util.Date;

@Entity
public class ccItems {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int itemsId;

    //该设备的维保天数
    private int days;

    //最近一次到期日期
    @Temporal(TemporalType.DATE)
    private Date lastDate;

    @OneToOne
    @JoinColumn(name = "equip")
    private ccEquip ccEquip;

    private String remarks;

    private String keywords;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "ccItemsSet")
    //不要使用CascadeType.REMOVE会级联删除门店
    @JsonIgnoreProperties("ccItemsSet")
    private Set<CCStore> ccStoresSet;

    public String getKeywords() {
        return keywords;
    }

    public Set<CCStore> getCcStoresSet() {
        return ccStoresSet;
    }

    public void setCcStoresSet(Set<CCStore> ccStoresSet) {
        this.ccStoresSet = ccStoresSet;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public int getItemsId() {
        return itemsId;
    }

    public void setItemsId(int itemsId) {
        this.itemsId = itemsId;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public com.xiabuxiabu.storemanage.entity.ccequip.ccEquip getCcEquip() {
        return ccEquip;
    }

    public void setCcEquip(com.xiabuxiabu.storemanage.entity.ccequip.ccEquip ccEquip) {
        this.ccEquip = ccEquip;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
