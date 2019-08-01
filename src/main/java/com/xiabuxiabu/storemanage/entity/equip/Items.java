package com.xiabuxiabu.storemanage.entity.equip;



import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.equip.Item;
import com.xiabuxiabu.storemanage.entity.store.StoreRemarks;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

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
    //记录该设备是否通过审核
    private String sign;  //记录是否通过审核。1通过；2不通过

    private String checkPerson;  //审核

    private String opreationPerson;

    @Temporal(TemporalType.TIMESTAMP)
    private Date chenckTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date upDateTime;

    public String getOpreationPerson() {
        return opreationPerson;
    }

    public void setOpreationPerson(String opreationPerson) {
        this.opreationPerson = opreationPerson;
    }

    public Date getChenckTime() {
        return chenckTime;
    }

    public void setChenckTime(Date chenckTime) {
        this.chenckTime = chenckTime;
    }

    public Date getUpDateTime() {
        return upDateTime;
    }

    public void setUpDateTime(Date upDateTime) {
        this.upDateTime = upDateTime;
    }


    //    @ManyToMany(targetEntity = StoreRemarks.class,fetch = FetchType.EAGER)
//    @JoinTable(name = "items_remark",joinColumns = @JoinColumn(name = "items_id"),inverseJoinColumns = @JoinColumn(name = "remark_id"))
//    private Set<StoreRemarks> storeRemarksSet;


//    public Set<StoreRemarks> getStoreRemarksSet() {
//        return storeRemarksSet;
//    }
//
//    public void setStoreRemarksSet(Set<StoreRemarks> storeRemarksSet) {
//        this.storeRemarksSet = storeRemarksSet;
//    }

    public String getCheckPerson() {
        return checkPerson;
    }

    public void setCheckPerson(String checkPerson) {
        this.checkPerson = checkPerson;
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