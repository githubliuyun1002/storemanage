/*
package com.xiabuxiabu.storemanage.alrmtask;
import com.xiabuxiabu.storemanage.entity.ccstore.CCStore;
import com.xiabuxiabu.storemanage.entity.store.*;
import com.xiabuxiabu.storemanage.service.ccstore.CCStoreService;
import com.xiabuxiabu.storemanage.service.store.*;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

*/
/**
 * 整合门店信息的定时任务
 *//*

@Async
@Component
@EnableScheduling
public class AlarmTask {
    @Autowired
    private StoreBOHService storeBOHService;
    @Autowired
    private StoreStatusService storeStatusService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private CCStoreService ccStoreService;

    @Autowired
    private MailListSerivice mailListSerivice;

    private static Logger log    = Logger.getLogger(EmailTaskInfoMsg.class);
    //整合门店信息，每两个小时执行一次
    //0 0 0/2 * * ?   测试时两分钟执行一次
    @Scheduled(cron = "0 0/2 * * * ?")
    public void run() {
         List<StoreBOH> storeBOHList = storeBOHService.findALlStoreBOH();
         System.out.println("size数据的长度---->"+storeBOHList.size());
         for (int i = 0; i <storeBOHList.size() ; i++) {
            StoreBOH  storeBOH = storeBOHList.get(i);
            String storeCode = storeBOH.getStorecode();
            if(storeService.findByStoreCode(storeCode)!=null){
                Store storeOld = storeService.findById(storeService.findByStoreCode(storeCode).getStoreId());
                if(storeOld!=null){
                    //执行更新操作(不需要像待发送列表中存入记录)
                    storeOld.setStoreCode(storeBOH.getStorecode());
                    storeOld.setStoreName(storeBOH.getStorename());
                    storeOld.setAddress(storeBOH.getAddress());
                    storeOld.setMarger(storeBOH.getMarger());
                    storeOld.setMarketName(storeBOH.getMarketname());
                    storeOld.setOpenDate(storeBOH.getOpendate());
                    storeOld.setBand(storeBOH.getBand());
                    storeOld.setStoreStatus(storeOld.getStoreStatus());
                    storeService.save(storeOld);
                    System.out.println("<-----执行更新操作------>"+storeOld.getStoreCode());
                }
            }else{
                //执行保存操作(新增门店)此时，需要向邮件待处理列表中，添加一天记录
                Store store = new Store();
                store.setStoreCode(storeBOH.getStorecode());
                store.setStoreName(storeBOH.getStorename());
                store.setAddress(storeBOH.getAddress());
                store.setMarger(storeBOH.getMarger());
                store.setMarketName(storeBOH.getMarketname());
                store.setOpenDate(storeBOH.getOpendate());
                store.setBand(storeBOH.getBand());
                StoreStatus storeStatus =  storeStatusService.findById(1);
                store.setStoreStatus(storeStatus);
                storeService.save(store);
                //添加邮件列表
                MailList mailList = new MailList();
                mailList.setStoreCode(storeBOH.getStorecode());
                mailList.setStoreName(storeBOH.getStorename());
                mailList.setStoreStatus("待选择");
                mailList.setMailStatus(0);
                //市场名称
                mailList.setMarketName(storeBOH.getMarketname());
                mailListSerivice.save(mailList);
                System.out.println("<----执行保存操作----->");
            }
        }
        System.out.println("整合所有的门店信息--->>>>>>>>>>>>>>>>>>>>"+System.currentTimeMillis());

    }

    */
/**
     * 整合湊湊门店的信息，每三个小时整合一次 0 0 0/3 * * ?
     *//*

    @Scheduled(cron = "0 0/3 * * * ?")
    public void ccrun(){
        List<StoreBOH> storeBOHList = storeBOHService.findALlStoreBOH();
        System.out.println("整合湊湊数据长度------》"+storeBOHList.size());
        for (int i = 0; i <storeBOHList.size() ; i++) {
            StoreBOH storeBOH = storeBOHList.get(i);
            String storeCode = storeBOH.getStorecode();

            //step1:从boh数据库中同步所有的门店信息，然后再同步湊湊门店的信息

            if(storeBOH.getBand().equals("湊湊")){
                if(ccStoreService.findByStoreCode(storeCode)!=null){
                    CCStore ccStoreOld = ccStoreService.findById(ccStoreService.findByStoreCode(storeCode).getStoreId());
                    //该门店已经存在，需要更新信息的门店
                    if(ccStoreOld!=null){
                        ccStoreOld.setStoreName(storeBOH.getStorename());
                        ccStoreOld.setStoreCode(storeBOH.getStorecode());
                        ccStoreOld.setMarketName(storeBOH.getMarketname());
                        ccStoreOld.setStoreManage(storeBOH.getMarger());
                        // ccStoreOld.setBand(storeBOH.getBand());
                        ccStoreOld.setAddress(storeBOH.getAddress());
                        ccStoreOld.setStoreMail(storeBOH.getStorecode()+"@coucouchn.com");
                        ccStoreService.save(ccStoreOld);
                        System.out.println("湊湊门店的更新操作-------》"+ccStoreOld.getStoreCode());
                    }
                }else{
                    CCStore ccStore = new CCStore();
                    ccStore.setStoreCode(storeBOH.getStorecode());
                    ccStore.setStoreName(storeBOH.getStorename());
                    ccStore.setMarketName(storeBOH.getMarketname());
                    ccStore.setStoreManage(storeBOH.getMarger());
                    //ccStore.setBand(storeBOH.getBand());
                    ccStore.setAddress(storeBOH.getAddress());
                    ccStore.setStoreMail(storeBOH.getStorecode()+"@coucouchn.com");
                    ccStoreService.save(ccStore);
                    System.out.println("湊湊门店信息初始化----->");

                }
            }
        }
        System.out.println("整合所有湊湊门店的信息--->>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+System.currentTimeMillis());

    }









}
*/
