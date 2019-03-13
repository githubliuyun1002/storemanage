package com.xiabuxiabu.storemanage.entity.store;
import javax.persistence.*;
import java.util.Date;
/**
 * 宽带实体类对象
 */
@Entity
public class WidthBand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //运行商
    @OneToOne
    @JoinColumn(name = "serviceperson")
    private ServicePerson servicePerson;
    //接入方式
    @OneToOne
    @JoinColumn(name = "accessmethod")
    private AccessMethod accessMethod;
    //宽带账号
    private String identity;
    //密码
    private String password;
    //带宽
    private String tapeWidth;
    //付款方式
    @OneToOne
    @JoinColumn(name = "paymethod")
    private PayMethod payMethod;
    //付款金额
    private double payMoney;
    //到期日期
    @Temporal(TemporalType.DATE)
    private Date endDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTapeWidth() {
        return tapeWidth;
    }

    public void setTapeWidth(String tapeWidth) {
        this.tapeWidth = tapeWidth;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    public double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(double payMoney) {
        this.payMoney = payMoney;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", servicePerson=" + servicePerson +
                ", accessMethod=" + accessMethod +
                ", identity='" + identity + '\'' +
                ", password='" + password + '\'' +
                ", tapeWidth='" + tapeWidth + '\'' +
                ", payMethod=" + payMethod +
                ", payMoney=" + payMoney +
                ", endDate=" + endDate +
                '}';
    }
}
