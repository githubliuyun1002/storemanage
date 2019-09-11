package com.xiabuxiabu.storemanage.alrmtask;


import com.xiabuxiabu.storemanage.emailsend.EMailTask;
import com.xiabuxiabu.storemanage.entity.store.MailList;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.WidthBand;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.publicutils.DateTool;
import com.xiabuxiabu.storemanage.publicutils.EMailProperties;
import com.xiabuxiabu.storemanage.service.store.MailListSerivice;
import com.xiabuxiabu.storemanage.service.store.StoreService;
import com.xiabuxiabu.storemanage.service.store.WidthBandService;
import com.xiabuxiabu.storemanage.service.user.UserService;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 宽带到期提醒，项目启动后成功后执行下面的代码
 */


@Component
@Order(value = 1)
public class WidthTask implements ApplicationRunner {
    @Autowired
    private StoreService storeService;
    @Autowired
    private UserService userService;
    @Autowired
    private DateTool dateTool;
    @Autowired
    private EMailTask eMailTask;
    @Autowired
    private EMailProperties eMailProperties;
    @Autowired
    private WidthBandService widthBandService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> marketNameList = storeService.marketNameList();
        //根据市场拿出该市场的人员进行通知
        for (int i = 0; i <marketNameList.size() ; i++) {
            String marketName = marketNameList.get(i);
            List<Store> storeList = storeService.findByMarketName(marketName);
            List<User> userList = userService.findByMarketName(marketName,"it");
            if(storeList.size()!=0&&userList.size()!=0){
                String mailAddress = "";
                for (int j = 0; j <userList.size() ; j++) {
                    mailAddress += userList.get(j).getMail() + ",";
                }
                String[] allSend = mailAddress.split(",");
                System.out.println("市场基本信息:市场名称" + marketName + ",门店数：" + storeList.size() + ",人员数：" + userList.size());
               // logger.info("市场基本信息:市场名称" + marketName + ",门店数：" + storeList.size() + ",人员数：" + userList.size());
                for (int j = 0; j <storeList.size() ; j++) {
                    Store store = storeList.get(j);
                    if(store.getWidthBandSet().size()!=0){
                        for(WidthBand widthBand : store.getWidthBandSet()) {
                            if (widthBand.getPayMethod().getMethod().equals("年付")) {
                            Date endDate = widthBand.getEndDate();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date nowDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                                int lastDays = dateTool.differentDaysByMillisecond(nowDate, endDate);
                                StringBuffer content = new StringBuffer();
                                content.append("<html><head></head>");
                                content.append("<body><div><h2>宽带到期通知</h2>" +
                                        "亲爱的用户:您好!门店：" + store.getStoreName() + "(" + store.getStoreCode() + ")的宽带还有" + lastDays + "天即将到期。请您及时进行处理，" +
                                        "如已处理，请忽略。谢谢！</div>");
                                content.append("<div><span style ='float: right;'>总部资讯</span></div>");
                                content.append("</body></html>");
                                if (lastDays >= 30 && lastDays < 40) {
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                eMailTask.sendHtmlMail(eMailProperties.getNickname(), allSend, eMailProperties.getAdditems(), content.toString(),
                                                        eMailProperties.getHost(), eMailProperties.getUsername(), eMailProperties.getPassword());
                                                logger.info("邮件发送成功");
                                            } catch (Exception e) {
                                                logger.error(e + "-----邮件未发出去");
                                            }
                                        }
                                    };
                                    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                                    service.scheduleAtFixedRate(runnable, 0, 1, TimeUnit.DAYS);
                                } else if (lastDays > 15 && lastDays < 30) {
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                eMailTask.sendHtmlMail(eMailProperties.getNickname(), allSend, eMailProperties.getAdditems(), content.toString(),
                                                        eMailProperties.getHost(), eMailProperties.getUsername(), eMailProperties.getPassword());
                                                logger.info("邮件发送成功");
                                            } catch (Exception e) {
                                                logger.error(e + "-----邮件未发出去");
                                            }
                                        }
                                    };
                                    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                                    service.scheduleAtFixedRate(runnable, 0, 12, TimeUnit.HOURS);
                                } else if (lastDays > 0 && lastDays <= 15) {
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                eMailTask.sendHtmlMail(eMailProperties.getNickname(), allSend, eMailProperties.getAdditems(), content.toString(),
                                                        eMailProperties.getHost(), eMailProperties.getUsername(), eMailProperties.getPassword());
                                                logger.info("邮件发送成功");
                                            } catch (Exception e) {
                                                logger.error(e + "-----邮件未发出去");
                                            }

                                        }
                                    };
                                    ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
                                    service.scheduleAtFixedRate(runnable, 0, 8, TimeUnit.HOURS);
                                } else if (lastDays == 0) {
                                    long yearAfter = (endDate.getTime() / 1000) + 60 * 60 * 24 * 365;
                                    endDate.setTime(yearAfter * 1000);
                                    String yearAfterStr = simpleDateFormat.format(endDate);
                                    Date parse = simpleDateFormat.parse(yearAfterStr);
                                    System.out.println("一年以后的日期----》" + parse);
                                    widthBand.setEndDate(parse);
                                    widthBandService.save(widthBand);
                                    store.getWidthBandSet().add(widthBand);
                                    storeService.save(store);
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

}
