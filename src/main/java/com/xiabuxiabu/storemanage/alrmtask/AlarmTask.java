package com.xiabuxiabu.storemanage.alrmtask;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.StoreBOH;
import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
import com.xiabuxiabu.storemanage.service.store.StoreBOHService;
import com.xiabuxiabu.storemanage.service.store.StoreService;
import com.xiabuxiabu.storemanage.service.store.StoreStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class AlarmTask {
    @Autowired
    private StoreBOHService storeBOHService;
    @Autowired
    private StoreStatusService storeStatusService;
    @Autowired
    private StoreService storeService;
    @Scheduled(cron = "0 0 12 * * ?")  //每天12点触发
    public void run() {
         List<StoreBOH> storeBOHList = storeBOHService.findALlStoreBOH();
         System.out.println("size数据的长度---->"+storeBOHList.size());
         for (int i = 0; i <storeBOHList.size() ; i++) {
            StoreBOH  storeBOH = storeBOHList.get(i);
            String storeCode = storeBOH.getStorecode();
            if(storeService.findByStoreCode(storeCode)!=null){
                Store storeOld = storeService.findById(storeService.findByStoreCode(storeCode).getStoreId());
                if(storeOld!=null){
                    //执行更新操作
                    storeOld.setStoreCode(storeBOH.getStorecode());
                    storeOld.setStoreName(storeBOH.getStorename());
                    storeOld.setAddress(storeBOH.getAddress());
                    storeOld.setMarger(storeBOH.getMarger());
                    storeOld.setMarketCode(storeBOH.getMarketcode());
                    storeOld.setOpenDate(storeBOH.getOpendate());
                    storeOld.setStoreStatus(storeOld.getStoreStatus());
                    storeService.save(storeOld);
                    System.out.println("执行更新操作");
                }
            }else{
                //执行保存操作
                Store store = new Store();
                store.setStoreCode(storeBOH.getStorecode());
                store.setStoreName(storeBOH.getStorename());
                store.setAddress(storeBOH.getAddress());
                store.setMarger(storeBOH.getMarger());
                store.setMarketCode(storeBOH.getMarketcode());
                store.setOpenDate(storeBOH.getOpendate());
                StoreStatus storeStatus =  storeStatusService.findById(1);
                store.setStoreStatus(storeStatus);
                storeService.save(store);
                System.out.println("执行保存操作");
            }



        }
    }



}
