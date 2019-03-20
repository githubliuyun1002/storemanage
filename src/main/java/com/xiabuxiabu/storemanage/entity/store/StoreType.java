package com.xiabuxiabu.storemanage.entity.store;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 门店选择设备型号实体，以及设备数量
 */
@Entity
public class StoreType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int storeId;
    private int typeId;
    private int num;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", typeId=" + typeId +
                ", num=" + num +
                '}';
    }
}
