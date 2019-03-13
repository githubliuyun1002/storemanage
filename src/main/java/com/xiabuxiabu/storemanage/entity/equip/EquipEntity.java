package com.xiabuxiabu.storemanage.entity.equip;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

/**
 * 设备实体类
 */
@Entity
public class EquipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @ManyToMany
    @JoinTable(name = "equip_type",joinColumns = @JoinColumn(name = "equip_id"),inverseJoinColumns = @JoinColumn(name = "type_id"))
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TypeEntity> equipTypes;

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

    public Set<TypeEntity> getEquipTypes() {
        return equipTypes;
    }

    public void setEquipTypes(Set<TypeEntity> equipTypes) {
        this.equipTypes = equipTypes;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", equipTypes=" + equipTypes +
                '}';
    }
}
