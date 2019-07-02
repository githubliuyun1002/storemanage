package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 该类用于提醒，管理员以及市场IT以及进行门店信息的操作。
 * 1、添加设备
 * 2.进行设备的审批
 */
@Entity
public class MailList {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String storeCode;
    private String storeName;
    private String marketName;
    //邮件的发送状态
    /**
     * 邮件的发送状态：
     * 1.（1）标识提醒市场IT门店已经新建成功，需要添加设备
     * 2.（2）标识市场IT添加设备成功，需要管理员进行审批
     * 3.（3）标识市场IT修改数据成功，需要管理员重新审批
     * 4.（4）标识门店已经通过审核
     */
    private int mailStatus;
    //门店的状态
    private String storeStatus;

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(String storeStatus) {
        this.storeStatus = storeStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(String storeCode) {
        this.storeCode = storeCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getMailStatus() {
        return mailStatus;
    }

    public void setMailStatus(int mailStatus) {
        this.mailStatus = mailStatus;
    }
    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
