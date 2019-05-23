package com.xiabuxiabu.storemanage.entity.publicutil;

import com.alibaba.fastjson.JSONObject;
import com.xiabuxiabu.storemanage.entity.user.User;

import javax.persistence.*;
import java.util.Set;

/**
 * 市场的基本信息实体类，市场要和相应的人员进行对应
 */
@Entity
public class MarketEntity {
   @Id
    private int id;
    private String marketCode;
    private String name;


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

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
