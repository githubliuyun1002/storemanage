package com.xiabuxiabu.storemanage.alrmtask;

import com.xiabuxiabu.storemanage.emailsend.EMailTask;
import com.xiabuxiabu.storemanage.entity.ccequip.ccItems;
import com.xiabuxiabu.storemanage.entity.ccstore.CCStore;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.publicutils.DateTool;
import com.xiabuxiabu.storemanage.publicutils.EMailProperties;
import com.xiabuxiabu.storemanage.service.cceuipservice.CCItemsService;
import com.xiabuxiabu.storemanage.service.ccstore.CCStoreService;
import com.xiabuxiabu.storemanage.service.ccuser.CCUserService;
import com.xiabuxiabu.storemanage.service.store.StoreBOHService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 湊湊门店特殊设备到期提示
 */
@Component
@Order(value = 2)
public class CCEquipTask implements ApplicationRunner {
    //step0:拿出湊湊品牌下的市场信息
    //step1.拿出所有人员----》市场信息
    //step2.在湊湊门店中，拿出人员所属市场的所有的门店
    //step3.拿到门店中设备的最新一次维修周期+设备的维修周期
    //step4.然后，进行门店设备的到期提示
    @Autowired
    private CCUserService ccUserService;

    @Autowired
    private CCStoreService ccStoreService;

    @Autowired
    private StoreBOHService storeBOHService;

    @Autowired
    private DateTool dateTool;

    @Autowired
    private EMailTask eMailTask;

    @Autowired
    private EMailProperties eMailProperties;

    @Autowired
    private CCItemsService ccItemsService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("湊湊特殊设备调用定时任务进行执行");
        //湊湊门店的市场信息
        List<String> marketList = storeBOHService.bandMarketList("湊湊");
       // System.out.println("湊湊门店的市场信息-----》"+marketList.size());
        for (int i = 0; i <marketList.size() ; i++) {

            String marketName = marketList.get(i);
            List<CCStore> ccStoreList = ccStoreService.findByMarketName(marketName);
            //System.out.println("湊湊门店数量统计-------》"+marketName+",门店数量----》"+ccStoreList.size());
            List<User>  ccUserList = ccUserService.ccfindByMarketName(marketName,"teshu");
            if(ccStoreList.size()!=0 && ccUserList.size()!=0){
                String mailAddress = "";
                for (int j = 0; j <ccUserList.size() ; j++) {
                    mailAddress += ccUserList.get(j).getMail() + ",";
                }
                for (int j = 0; j <ccStoreList.size() ; j++) {
                    CCStore ccStore = ccStoreList.get(j);

                    if(ccStore.getCcItemsSet().size()!=0){
                        for(ccItems items : ccStore.getCcItemsSet()){
                            //到期日期
                            Date endDate =   items.getLastDate();
                            //最近一次维修周期
                            int weekDays = items.getDays();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date nowDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                                //日期差
                                int lastDays = dateTool.differentDaysByMillisecond(nowDate,endDate);
                               // System.out.println("lastDays-------->"+lastDays);

                                //邮件的正文
                                StringBuffer content = new StringBuffer();
                                content.append("<html><head></head>");
                                content.append("<body><div><h2>设备维修到期通知</h2>" +
                                        "亲爱的用户:您好!门店："+ccStore.getStoreName()+"("+ccStore.getStoreCode()+")的<font color=\"red\">"+items.getCcEquip().getName()+"</font>设备还有<font color=\"red\">"+lastDays+"</font>天需要进行维保工作。请您及时进行准备，" +
                                        "如已处理，请忽略。谢谢！</div>");
                                content.append("</body></html>");
                                //还未过期
                                if(lastDays>0){
                                    //提前1-3天进行通知，一天发送两封通知邮件
                                    if(lastDays>=1&&lastDays<=3){
                                        mailAddress += ccStore.getStoreMail()+",";
                                        String[] allSend = mailAddress.split(",");
                                        Runnable runnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    eMailTask.sendHtmlMail(eMailProperties.getNickname(),allSend,eMailProperties.getEquipname(),content.toString(),
                                                            eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
                                                    logger.info("邮件发送成功");
                                                    items.setRemarks("邮件发送成功");
                                                    ccItemsService.save(items);
                                                } catch (Exception e) {
                                                    logger.error(e+"-----邮件未发出去");
                                                }
                                            }
                                        };
                                        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                                        service.scheduleAtFixedRate(runnable, 0, 12, TimeUnit.HOURS);
                                    }
                                }else{
                                    //此时是设备的维修期已过，需要计算维修周期得出下次维修的日期
                                    long last = (endDate.getTime()/1000)+60*60*24*weekDays;
                                    endDate.setTime(last*1000);
                                    String callPariTime = simpleDateFormat.format(endDate);
                                    Date parse = simpleDateFormat.parse(callPariTime);
                                    items.setLastDate(parse);
                                    ccItemsService.save(items);
                                    ccStore.getCcItemsSet().add(items);
                                    ccStoreService.save(ccStore);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }



            }
        }
    }


}
