package com.xiabuxiabu.storemanage.entity.store;

import com.xiabuxiabu.storemanage.entity.equip.EquipEntity;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
public class Store{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String code;
    private String name;
    private String address;
    //市场IT
    private String marketIT;
    //门店所属市场
    @OneToOne
    @JoinColumn(name = "marketentity")
    private MarketEntity marketEntity;
    //门店经理
    private String manager;
    @OneToOne
    @JoinColumn(name = "storestatus")
    //餐厅状态
    private StoreStatus storeStatus;
    @OneToOne
    @JoinColumn(name = "widthband")
    //餐厅宽带
    private WidthBand widthBand;
    //开店日期
    @Temporal(TemporalType.DATE)
    private Date startDate;
    //闭店日期
    @Temporal(TemporalType.DATE)
    private Date closeDate;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public StoreStatus getStoreStatus() {
        return storeStatus;
    }

    public void setStoreStatus(StoreStatus storeStatus) {
        this.storeStatus = storeStatus;
    }

    public WidthBand getWidthBand() {
        return widthBand;
    }

    public void setWidthBand(WidthBand widthBand) {
        this.widthBand = widthBand;
    }


    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public String getMarketIT() {
        return marketIT;
    }

    public void setMarketIT(String marketIT) {
        this.marketIT = marketIT;
    }

    public MarketEntity getMarketEntity() {
        return marketEntity;
    }

    public void setMarketEntity(MarketEntity marketEntity) {
        this.marketEntity = marketEntity;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", marketIT='" + marketIT + '\'' +
                ", marketEntity=" + marketEntity +
                ", manager='" + manager + '\'' +
                ", storeStatus=" + storeStatus +
                ", widthBand=" + widthBand +
                ", startDate=" + startDate +
                ", closeDate=" + closeDate +
                '}';
    }
}
