package com.xiabuxiabu.storemanage;

import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.publicutil.PublicStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.persistence.OneToOne;

@Component
@Order(value = 1)   //初始化顺序，value值越小，越容易初始化
public class AppliactionInit implements ApplicationRunner {
    @Autowired
    private PublicStatusService publicStatusService;
    @Autowired
    private MarketService marketService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //初始化公共状态
        PublicStatus publicStatus = new PublicStatus();
        publicStatus.setId(1);
        publicStatus.setName("启用");
        publicStatus.setRemarks("公共状态启用");
        publicStatusService.save(publicStatus);
        publicStatus.setId(2);
        publicStatus.setName("禁用");
        publicStatus.setRemarks("公共状态禁用");
        publicStatusService.save(publicStatus);
        //初始化市场的基本信息
        MarketEntity marketEntity = new MarketEntity();
        marketEntity.setId(1);
        marketEntity.setName("北京市场");
        marketEntity.setRemarks("北京市场");
        marketService.save(marketEntity);
        marketEntity.setId(2);
        marketEntity.setName("天津市场");
        marketEntity.setRemarks("天津市场");
        marketService.save(marketEntity);
        marketEntity.setId(3);
        marketEntity.setName("河北市场");
        marketEntity.setRemarks("河北市场");
        marketService.save(marketEntity);
        marketEntity.setId(4);
        marketEntity.setName("东北市场");
        marketEntity.setRemarks("东北市场");
        marketService.save(marketEntity);
        marketEntity.setId(5);
        marketEntity.setName("深圳市场");
        marketEntity.setRemarks("深圳市场");
        marketService.save(marketEntity);
        marketEntity.setId(6);
        marketEntity.setName("上海市场");
        marketEntity.setRemarks("上海市场");
        marketService.save(marketEntity);
    }
}
