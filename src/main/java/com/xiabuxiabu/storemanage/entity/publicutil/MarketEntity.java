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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int marketId;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMarketId() {
        return marketId;
    }

    public void setMarketId(int marketId) {
        this.marketId = marketId;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this,true);
    }
}
