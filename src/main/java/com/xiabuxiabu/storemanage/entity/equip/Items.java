package com.xiabuxiabu.storemanage.entity.equip;



import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.equip.Item;

import javax.persistence.*;

@Entity
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne
    @JoinColumn(name = "item")
    private Item item;
    private int num;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}