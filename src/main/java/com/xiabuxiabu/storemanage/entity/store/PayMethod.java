package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**

* @Description:    付款方式

* @Author:         liuyun

* @CreateDate:     2019/5/28 11:34

* @UpdateUser:     liuyun

* @UpdateDate:     2019/5/28 11:34

* @Version:        1.0

*/

@Entity
public class PayMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int payid;
    private String method;
    private String remarks;

    public int getPayid() {
        return payid;
    }

    public void setPayid(int payid) {
        this.payid = payid;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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
