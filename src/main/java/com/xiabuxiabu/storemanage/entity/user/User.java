package com.xiabuxiabu.storemanage.entity.user;
import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String name;
    private String password;
    private String mail;
    @OneToOne
    @JoinColumn(name = "status")
    private PublicStatus publicStatus;
    @ManyToMany(targetEntity = Role.class,fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",joinColumns = @JoinColumn(name = "user_id"),inverseJoinColumns = @JoinColumn(name = "role_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Role> roles;
    //人员所属的市场,根据人员所属的市场进行统计各个门店的信息
    @OneToOne
    @JoinColumn(name = "market")
    private MarketEntity marketEntity;

    public MarketEntity getMarketEntity() {
        return marketEntity;
    }

    public void setMarketEntity(MarketEntity marketEntity) {
        this.marketEntity = marketEntity;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
