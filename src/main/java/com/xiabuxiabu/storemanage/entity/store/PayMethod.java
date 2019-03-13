package com.xiabuxiabu.storemanage.entity.store;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class PayMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //支付方式选择
    private String method;
    private String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "{" +
                "id=" + id +
                ", method='" + method + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }

}
