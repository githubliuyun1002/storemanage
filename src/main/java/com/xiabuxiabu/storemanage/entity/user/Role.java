package com.xiabuxiabu.storemanage.entity.user;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String  description;
    @OneToOne
    @JoinColumn(name = "status")
    private PublicStatus publicStatus;
    @ManyToMany(targetEntity = Permission.class,fetch = FetchType.EAGER)
    @JoinTable(name = "role_permission",joinColumns = @JoinColumn(name = "role_id"),inverseJoinColumns = @JoinColumn(name = "permission_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Permission> permissions;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
