package com.xiabuxiabu.storemanage.entity.ccuser;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class CCUserType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int typeId;
    private String name;
    private String remark;

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {

        return JSONObject.toJSONString(this,true);
    }
    //
}
