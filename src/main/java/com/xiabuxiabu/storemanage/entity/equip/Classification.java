package com.xiabuxiabu.storemanage.entity.equip;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;

import javax.persistence.*;
import java.util.Set;
/**
 * 类别 网络设备，通讯设备
 */
@Entity
public class Classification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int classId;
    private String name;
    @OneToOne
    @JoinColumn(name = "status")
    private PublicStatus publicStatus;
    @ManyToMany(targetEntity = EquipName.class,fetch = FetchType.EAGER)
    @JoinTable(name = "class_equipname",joinColumns = @JoinColumn(name = "class_id"),inverseJoinColumns = @JoinColumn(name = "equipname_id"))
    private Set<EquipName> equipNames;
    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Column(name = "name",nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EquipName> getEquipNames() {
        return equipNames;
    }

    public void setEquipNames(Set<EquipName> equipNames) {
        this.equipNames = equipNames;
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

