/*
package com.xiabuxiabu.storemanage.entity.ccuser;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Entity
public class CCUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ccId;
    private String username;
    private String name;
    private String password;
    //门店维护人员的邮箱
    private String mail;
    @OneToOne
    @JoinColumn(name = "status")
    private PublicStatus publicStatus;
    @OneToOne
    @JoinColumn(name = "type")
    private CCUserType ccUserType;
    @ManyToMany(targetEntity = CCRole.class,fetch = FetchType.EAGER)
    @JoinTable(name = "ccuser_ccrole",joinColumns = @JoinColumn(name = "ccuser_id"),inverseJoinColumns = @JoinColumn(name = "ccrole_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<CCRole> ccRoleSet;
    private String marketName;  //市场名称
    private String band;        //品牌

    //对于特殊设备管理的人员的标记
    private String sign;


    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getCcId() {
        return ccId;
    }

    public void setCcId(int ccId) {
        this.ccId = ccId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public PublicStatus getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(PublicStatus publicStatus) {
        this.publicStatus = publicStatus;
    }

    public CCUserType getCcUserType() {
        return ccUserType;
    }

    public void setCcUserType(CCUserType ccUserType) {
        this.ccUserType = ccUserType;
    }

    public Set<CCRole> getCcRoleSet() {
        return ccRoleSet;
    }

    public void setCcRoleSet(Set<CCRole> ccRoleSet) {
        this.ccRoleSet = ccRoleSet;
    }

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    public String getBand() {
        return band;
    }

    public void setBand(String band) {
        this.band = band;
    }

    @Override
    public String toString() {

        return JSONObject.toJSONString(this,true);
    }





}
*/
