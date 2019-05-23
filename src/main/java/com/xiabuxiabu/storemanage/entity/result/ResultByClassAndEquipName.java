package com.xiabuxiabu.storemanage.entity.result;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "result_ClassAndEquipname")
public class ResultByClassAndEquipName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int resultId;
    private int classId;
    private String name;
    private int status;
    private int equipId;
    private String equipName;

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getEquipId() {
        return equipId;
    }

    public void setEquipId(int equipId) {
        this.equipId = equipId;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
