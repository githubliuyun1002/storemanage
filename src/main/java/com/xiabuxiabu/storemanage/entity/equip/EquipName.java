package com.xiabuxiabu.storemanage.entity.equip;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;

import javax.persistence.*;
import java.util.Set;

/**
 * 设备名称
 */
@Entity
public class EquipName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int equipId;
    private String name;
    private String className;
    private int classId;
    @ManyToMany(targetEntity = Item.class,fetch = FetchType.EAGER)
    @JoinTable(name = "equipname_item",joinColumns = @JoinColumn(name = "equipname_id"),inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> itemSet;
    @OneToOne
    @JoinColumn(name = "status")
    private PublicStatus publicStatus;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public PublicStatus getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(PublicStatus publicStatus) {
        this.publicStatus = publicStatus;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

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

    public Set<Item> getItemSet() {
        return itemSet;
    }

    public void setItemSet(Set<Item> itemSet) {
        this.itemSet = itemSet;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}

