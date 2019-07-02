package com.xiabuxiabu.storemanage.entity.equip;



import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.equip.Item;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String className;   //设备类型（网络设备，通讯设备的不同展示）
    private String equipName;    //设备名称
    @OneToOne
    @JoinColumn(name = "item")
    private Item item;
    private int num;
    private int origin;        //该字段记录修改之前，设备的数量
    private String personName; //修改人的姓名
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;   //修改时间
    //记录该设备是否通过审核
    private String sign;  //记录是否通过审核。1通过；2不通过

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }



    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Items(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}