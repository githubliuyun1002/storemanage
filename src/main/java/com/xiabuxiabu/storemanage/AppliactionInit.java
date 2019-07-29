package com.xiabuxiabu.storemanage;

import com.xiabuxiabu.storemanage.entity.publicutil.Band;
import com.xiabuxiabu.storemanage.entity.publicutil.MarketEntity;
import com.xiabuxiabu.storemanage.entity.publicutil.PublicStatus;
import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
import com.xiabuxiabu.storemanage.service.publicutil.BandService;
import com.xiabuxiabu.storemanage.service.publicutil.MarketService;
import com.xiabuxiabu.storemanage.service.publicutil.PublicStatusService;
import com.xiabuxiabu.storemanage.service.store.StoreBOHService;
import com.xiabuxiabu.storemanage.service.store.StoreStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.persistence.OneToOne;
import java.util.List;

@Component
@Order(value = 1)   //初始化顺序，value值越小，越容易初始化
public class AppliactionInit implements ApplicationRunner {
    @Autowired
    private PublicStatusService publicStatusService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private StoreStatusService storeStatusService;
    @Autowired
    private StoreBOHService storeBOHService;
    @Autowired
    private BandService bandService;
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
        storeStatus.setStatusId(5);
        storeStatus.setStatusName("门店闭店");
        storeStatus.setRemarks("此时市场IT申请闭店，需要管理员审批闭店操作");
        storeStatusService.save(storeStatus);

        List<String> marketNameList = storeBOHService.marketNameList();
        List<String> bandList = storeBOHService.bandList();
        //findMarketByMarketName
        for (int i = 0; i <marketNameList.size() ; i++) {
            String marketName = marketNameList.get(i);
            if(marketService.findMarketByMarketName(marketName)!=null&&!(marketName.equals("总部"))){
                MarketEntity marketEntityOld = marketService.findByMarketCode(marketService.findMarketByMarketName(marketName).getMarketId());
                marketEntityOld.setName(marketName);
            }else{
                MarketEntity marketEntity = new MarketEntity();
                marketEntity.setName(marketName);
                marketService.save(marketEntity);
            }

        }

        for (int i = 0; i <bandList.size(); i++) {
            String bandName = bandList.get(i);
            if(bandService.findByName(bandName)!=null&&!(bandName.equals("总部"))){
                Band bandOld = bandService.findById(bandService.findByName(bandName).getBandId());
                bandOld.setName(bandName);
            }else{
                Band band = new Band();
                band.setName(bandList.get(i));
                bandService.save(band);
            }
        }

    }
}
