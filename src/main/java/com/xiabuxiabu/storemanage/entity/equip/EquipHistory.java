package com.xiabuxiabu.storemanage.entity.equip;
import javax.persistence.*;
import java.util.Set;
/**
 * 设备变更历史表
 */
@Entity
public class EquipHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @ManyToMany
    @JoinTable(name = "history_types",joinColumns = @JoinColumn(name = "history_id"),inverseJoinColumns = @JoinColumn(name = "types_id"))
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
