package com.xiabuxiabu.storemanage.entity.equip;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 设备型号实体类
 */
@Entity
public class TypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //型号名称
    private String typeName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
