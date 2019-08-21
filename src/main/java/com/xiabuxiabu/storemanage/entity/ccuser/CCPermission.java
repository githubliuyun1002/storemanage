package com.xiabuxiabu.storemanage.entity.ccuser;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CCPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int perId;
    private int pid;
    private String url;
    private String name;
    private String description;

    public int getPerId() {
        return perId;
    }

    public void setPerId(int perId) {
        this.perId = perId;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
