package com.xiabuxiabu.storemanage.entity.ccuser;

import com.alibaba.fastjson.JSONObject;
import com.sun.javafx.beans.IDProperty;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Entity
public class CCRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roleId;
    private String name;
    private String description;
    @OneToOne
    @JoinColumn(name = "status")
    private PublicStatus publicStatus;
    @ManyToMany(targetEntity = CCPermission.class,fetch = FetchType.EAGER)
    @JoinTable(name ="ccrole_ccpermission",joinColumns = @JoinColumn(name = "ccrole_id"),inverseJoinColumns = @JoinColumn(name ="ccpermission_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<CCPermission> ccPermissions;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PublicStatus getPublicStatus() {
        return publicStatus;
    }

    public void setPublicStatus(PublicStatus publicStatus) {
        this.publicStatus = publicStatus;
    }

    public Set<CCPermission> getCcPermissions() {
        return ccPermissions;
    }

    public void setCcPermissions(Set<CCPermission> ccPermissions) {
        this.ccPermissions = ccPermissions;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }

}
