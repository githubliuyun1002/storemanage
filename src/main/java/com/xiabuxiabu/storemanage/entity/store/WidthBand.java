package com.xiabuxiabu.storemanage.entity.store;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.util.Date;


@Entity
public class WidthBand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int wid;
    //运营商
    @OneToOne
    @JoinColumn(name = "serviceperson")
    private ServicePerson servicePerson;
    //接入方式
    @OneToOne
    @JoinColumn(name = "accessmethod")
    private AccessMethod accessMethod;
    //付款方式
    @OneToOne
    @JoinColumn(name = "paymethod")
    private PayMethod payMethod;
    private Double payMoney;   //付款金额
    private String identity;   //账号
    private String password;   //密码
    private String tapewidth;  //带宽

    @Temporal(TemporalType.DATE)
    private Date endDate;   //到期日期

    public Double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
    }

    public int getWid() {
        return wid;
    }

    public void setWid(int wid) {
        this.wid = wid;
    }

    public ServicePerson getServicePerson() {
        return servicePerson;
    }

    public void setServicePerson(ServicePerson servicePerson) {
        this.servicePerson = servicePerson;
    }

    public AccessMethod getAccessMethod() {
        return accessMethod;
    }

    public void setAccessMethod(AccessMethod accessMethod) {
        this.accessMethod = accessMethod;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTapewidth() {
        return tapewidth;
    }

    public void setTapewidth(String tapewidth) {
        this.tapewidth = tapewidth;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
