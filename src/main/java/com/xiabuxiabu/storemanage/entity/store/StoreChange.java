package com.xiabuxiabu.storemanage.entity.store;

import javax.persistence.*;
import java.util.Date;

/**
 * 设备变更实体
 */
@Entity
public class StoreChange {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int storeId;
    private String storeName;   //storeName
    private int typeId;
    private String typeName;    //typeName
    //变更人
    private String changePerson;
    //变更原因
    private String changeReason;

    //变更时间
    @Temporal(TemporalType.DATE)
    private Date changeDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
    public String getChangePerson() {
        return changePerson;
    }
    public void setChangePerson(String changePerson) {
        this.changePerson = changePerson;
    }

    public String getChangeReason() {
        return changeReason;
    }
    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", changePerson='" + changePerson + '\'' +
                ", changeReason='" + changeReason + '\'' +
                ", changeDate=" + changeDate +
                '}';
    }
}
