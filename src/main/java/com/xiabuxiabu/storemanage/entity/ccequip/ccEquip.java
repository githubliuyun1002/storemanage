package com.xiabuxiabu.storemanage.entity.ccequip;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;

import javax.persistence.*;

@Entity
public class ccEquip {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int equipId;
    private String name;
    @OneToOne
    @JoinColumn(name = "status")
    private PublicStatus publicStatus;

    public int getEquipId() {
        return equipId;
    }

    public void setEquipId(int equipId) {
        this.equipId = equipId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PublicStatus getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(PublicStatus publicStatus) {
        this.publicStatus = publicStatus;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }


}
