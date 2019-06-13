package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**

* @Description:    宽带接入方式

* @Author:         liuyun

* @CreateDate:     2019/5/28 11:32

* @UpdateUser:     liuyun

* @UpdateDate:     2019/5/28 11:32

* @Version:        1.0

*/

@Entity
public class AccessMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int aid;
    private String accessName;
    private String remarks;

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getAccessName() {
        return accessName;
    }

    public void setAccessName(String accessName) {
        this.accessName = accessName;
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
