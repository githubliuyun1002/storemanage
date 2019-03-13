package com.xiabuxiabu.storemanage.entity.store;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 接入方式实体类
 */
@Entity
public class AccessMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String access;
    private String remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
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
                ", access='" + access + '\'' +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
