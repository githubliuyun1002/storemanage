package com.xiabuxiabu.storemanage.entity.equip;

import com.alibaba.fastjson.JSONObject;

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
    @ManyToMany(targetEntity = Item.class,fetch = FetchType.EAGER)
    @JoinTable(name = "equipname_item",joinColumns = @JoinColumn(name = "equipname_id"),inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> itemSet;

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

