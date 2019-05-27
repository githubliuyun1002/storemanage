package com.xiabuxiabu.storemanage.alrmtask;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.StoreBOH;
import com.xiabuxiabu.storemanage.service.store.StoreBOHService;
import com.xiabuxiabu.storemanage.service.store.StoreService;
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
    private StoreService storeService;
     /**
     * 定时任务已经实现，在此定时任务中完成BOH同步门店信息的更新。
     * 使用cron表达式
      * cron有六个表达式：秒，分，时，天，月，星期
     */
    @Scheduled(cron = "0/10 * * * * *")
    public void run() {
         List<StoreBOH> storeBOHList = storeBOHService.findALlStoreBOH();
         System.out.println("size数据的长度---->"+storeBOHList.size());

         for (int i = 0; i <storeBOHList.size() ; i++) {
            StoreBOH  storeBOH = storeBOHList.get(i);
            Store store = new Store();
            store.setStoreCode(storeBOH.getStorecode());
            store.setStoreName(storeBOH.getStorename());
            store.setAddress(storeBOH.getAddress());
            store.setMarger(storeBOH.getMarger());
            store.setMarketCode(storeBOH.getMarketcode());
            store.setOpenDate(storeBOH.getOpendate());
            storeService.save(store);
        }
    }



}
