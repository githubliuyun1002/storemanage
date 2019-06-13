package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**

* @Description:    宽带运行商

* @Author:         liuyun

* @CreateDate:     2019/5/28 11:18

* @UpdateUser:     liuyun
*/

@Entity
public class ServicePerson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int sid;
    private String serviceName;
    private String remarks;

    public int getSid() {

        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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
