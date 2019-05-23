package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class StoreStatus {
    @Id
    private int statusId;
    private String statusName;
    private String remarks;

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
