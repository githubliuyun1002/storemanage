package com.xiabuxiabu.storemanage;

import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.publicutil.PublicStatusService;
import com.xiabuxiabu.storemanage.service.store.StoreStatusService;
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
    @Autowired
    private StoreStatusService storeStatusService;
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
        marketEntity.setMarketCode(6001);
        marketEntity.setName("北京市场");
        marketService.save(marketEntity);
        marketEntity.setMarketCode(6002);
        marketEntity.setName("上海市场");
        marketService.save(marketEntity);
        marketEntity.setMarketCode(6003);
        marketEntity.setName("天津市场");
        marketService.save(marketEntity);
        marketEntity.setMarketCode(6004);
        marketEntity.setName("东北市场");
        marketService.save(marketEntity);
        marketEntity.setMarketCode(6005);
        marketEntity.setName("河北市场");
        marketService.save(marketEntity);
        marketEntity.setName("深圳市场");
        marketEntity.setMarketCode(6006);
        marketService.save(marketEntity);
        marketEntity.setMarketCode(6666);
        marketEntity.setName("总部");
        marketService.save(marketEntity);
        //初始化门店状态
        StoreStatus storeStatus = new StoreStatus();
        storeStatus.setStatusId(1);
        storeStatus.setStatusName("待选择");
        storeStatus.setRemarks("门店状态为空，或者手动自动建店");
        storeStatusService.save(storeStatus);
        storeStatus.setStatusId(2);
        storeStatus.setStatusName("待审批");
        storeStatus.setRemarks("门店选择了设备，等待管理员审批");
        storeStatusService.save(storeStatus);
        storeStatus.setStatusId(3);
        storeStatus.setStatusName("待调整");
        storeStatus.setRemarks("管理员审批有问题，需要调整的");
        storeStatusService.save(storeStatus);
        storeStatus.setStatusId(4);
        storeStatus.setStatusName("已确认");
        storeStatus.setRemarks("管理员审核过，没有问题的");
        storeStatusService.save(storeStatus);
        //设置门店闭店操作时，首先门店的设备数量请0，然后记录在历史记录中
        storeStatus.setStatusId(5);
        storeStatus.setStatusName("已闭店");
        storeStatus.setRemarks("市场IT、总部管理员可以设置门店闭店");
        storeStatusService.save(storeStatus);





    }
}
