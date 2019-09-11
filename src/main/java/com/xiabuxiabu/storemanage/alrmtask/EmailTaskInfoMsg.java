package com.xiabuxiabu.storemanage.alrmtask;

import com.xiabuxiabu.storemanage.emailsend.EMailTask;
import com.xiabuxiabu.storemanage.entity.ccstore.CCStore;
import com.xiabuxiabu.storemanage.entity.store.MailList;
import com.xiabuxiabu.storemanage.entity.store.Store;
import com.xiabuxiabu.storemanage.entity.store.StoreBOH;
import com.xiabuxiabu.storemanage.entity.store.StoreStatus;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.publicutils.EMailProperties;
import com.xiabuxiabu.storemanage.service.ccstore.CCStoreService;
import com.xiabuxiabu.storemanage.service.store.MailListSerivice;
import com.xiabuxiabu.storemanage.service.store.StoreBOHService;
import com.xiabuxiabu.storemanage.service.store.StoreService;
import com.xiabuxiabu.storemanage.service.store.StoreStatusService;
import com.xiabuxiabu.storemanage.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * 门店状态扭转通知（通知添加设备、审批等工作）
 * //@EnableAsync   //开启多线程
 * // @Async
 *
 */

@Component
@EnableAsync
@EnableScheduling
public class EmailTaskInfoMsg {
    @Autowired
    private MailListSerivice mailListSerivice;
    @Autowired
    private UserService userService;

    @Autowired
    private EMailTask eMailTask;

    @Autowired
    private EMailProperties eMailProperties;

    @Autowired
    private StoreBOHService storeBOHService;

    @Autowired
    private StoreStatusService storeStatusService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CCStoreService ccStoreService;


    private static Logger log    = Logger.getLogger(EmailTaskInfoMsg.class);

    //发送给市场IT,让市场IT及时进行添加相应的设备（每4个小时执行一次）
    //发送给管理员的邮件列表(没两个小时执行一次)
    @Async
    @Scheduled(cron = "0 0 0/3 * * ?")
    public void run() {
        List<StoreBOH> storeBOHList = storeBOHService.findALlStoreBOH();
        System.out.println("整合size数据的长度---->"+storeBOHList.size());
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
        System.out.println("Q1整合所有的门店信息--->>>>>>>>>>>>>>>>>>>> sync1--------->"+System.currentTimeMillis());

    }


    @Async
    @Scheduled(cron = "0 0 0/4 * * ?")
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
        System.out.println("Q2整合所有湊湊门店的信息--->>>>>>>>>>>>>>>>>>>>>>>>>>>>>> sync2-----"+System.currentTimeMillis());

    }

    @Async
    @Scheduled(cron = "0 0 0/5 * * ?")
    public void emailRun() {
        //市场名称
        List<String> marketNameList = mailListSerivice.marketList();
        //根据市场名称，取出人员，以及待发送的门店信息
        for (int i = 0; i < marketNameList.size(); i++) {
            String marketName = marketNameList.get(i);
            List<MailList> mailListList = mailListSerivice.findByMarketName(marketName);
            List<User> userList = userService.findByMarketName(marketName,"it");
            if (mailListList.size() != 0 && userList.size() != 0) {
                System.out.println("市场基本信息:市场名称" + marketName + ",门店数：" + mailListList.size() + ",人员数：" + userList.size());
                for (int j = 0; j < mailListList.size(); j++) {
                    MailList mailListDemo = mailListList.get(j);
                    if (mailListDemo.getMailStatus() == 0 && mailListDemo.getStoreStatus().equals("待选择")) {
                        StringBuffer content = new StringBuffer();
                        content.append("<html><head></head>");
                        content.append("<body><div><h2>门店设备添加通知</h2>" +
                                "亲爱的用户：您好！门店：" + mailListDemo.getStoreName() + "(" + mailListDemo.getStoreCode() + ")基本" +
                                "信息已经创建成功，请您尽快添加相应的宽带以及设备信息。如已处理请忽略。谢谢！</div>");
                        content.append("<div><span style ='float: right;'>总部资讯</span></div>");
                        content.append("</body></html>");
                        String mailAddress = "";
                        for (int k = 0; k < userList.size(); k++) {
                            mailAddress += userList.get(k).getMail() + ",";
                        }
                        String[] allSend = mailAddress.split(",");
                        try {
                            eMailTask.sendHtmlMail(eMailProperties.getNickname(),allSend,eMailProperties.getAdditems(),content.toString(),
                                    eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
                            mailListDemo.setMailStatus(1);
                            mailListDemo.setRemarks("发送成功");
                            mailListSerivice.save(mailListDemo);
                            log.info("邮件发送成功");
                        } catch (Exception e) {
                            // e.printStackTrace();
                            mailListDemo.setMailStatus(0);
                            mailListDemo.setRemarks("未发送成功");
                            mailListSerivice.save(mailListDemo);
                            log.error(e+"-----邮件未发出去");
                        }
                    }else if(mailListDemo.getMailStatus()==3 && mailListDemo.getStoreStatus().equals("待调整")){
                        StringBuffer content = new StringBuffer();
                        content.append("<html><head></head>");
                        content.append("<body><div><h2>门店设备调整通知</h2>" +
                                "亲爱的用户：您好！门店：" + mailListDemo.getStoreName() + "(" + mailListDemo.getStoreCode() + ")设备信息" +
                                "经管理员未审批通过，请您及时修改。如已处理请忽略。谢谢！</div>");
                        content.append("<div><span style ='float: right;'>总部资讯</span></div>");
                        content.append("</body></html>");
                        String mailAddress = "";
                        for (int k = 0; k < userList.size(); k++) {
                            mailAddress += userList.get(k).getMail() + ",";
                        }
                        String[] allSend = mailAddress.split(",");
                        try {
                            eMailTask.sendHtmlMail(eMailProperties.getNickname(),allSend,eMailProperties.getAdditems(),content.toString(),
                                    eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
                            mailListDemo.setMailStatus(3333);
                            mailListDemo.setRemarks("发送成功");
                            mailListSerivice.save(mailListDemo);
                            log.info("邮件发送成功");
                        } catch (Exception e) {
                            // e.printStackTrace();
                            mailListDemo.setMailStatus(3);
                            mailListDemo.setRemarks("未发送成功");
                            mailListSerivice.save(mailListDemo);
                            log.error(e+"-----邮件未发出去");
                        }

                    }


                }
            }
        }
        System.out.println("#########################>>>>>>>>>>>>>>>Q3使用用户给市场IT发送邮件 sync3------"+System.currentTimeMillis());
    }

    @Async
    @Scheduled(cron = "0 0 0/6 * * ?")
    public void  adminRun(){
        //对于管理员拿到所有的待发邮件的信息
        System.out.println("发送管理员定时任务开始执行");

        List<MailList> mailLists = mailListSerivice.findAll();

        List<User> userList = userService.findAll("it");

        List<User> adminUserList = new ArrayList<>();

        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);

            if(user.getUserType().getName().equals("系统管理员")&&user.getSign().equals("it")){
                adminUserList.add(user);
            }
        }
        System.out.println("###############################");
        System.out.println("管理员的个数----------》"+adminUserList.size());


        if(mailLists.size()!=0&&adminUserList.size()!=0){
            for (int i = 0; i <mailLists.size() ; i++) {

                MailList mailListDemo = mailLists.get(i);
                StringBuffer contentAdmin = new StringBuffer();

                if(mailListDemo.getMailStatus()==2&&mailListDemo.getStoreStatus().equals("待审批")){
                    contentAdmin.append("<html><head></head>");
                    contentAdmin.append("<body><div><h2>门店设备审批通知</h2>" +
                            "亲爱的用户：您好！门店："+mailListDemo.getStoreName()+"("+mailListDemo.getStoreCode()+")设备信息" +
                            "已经添加成功，请您尽快进行审批。如已处理请忽略。谢谢！</div>");
                    contentAdmin.append("<div><span style ='float: right;'>总部资讯</span></div>");
                    contentAdmin.append("</body></html>");
                    String send="";
                    for (int j = 0; j < adminUserList.size(); j++) {
                        send+=adminUserList.get(j).getMail()+",";
                    }
                    String[] sendAll = send.split(",");
                    try {
                        eMailTask.sendHtmlMail(eMailProperties.getNickname(),sendAll,eMailProperties.getCheckitems(),contentAdmin.toString(),
                                eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
                        mailListDemo.setMailStatus(2222);
                        mailListDemo.setRemarks("发送成功");
                        mailListSerivice.save(mailListDemo);
                    } catch (Exception e) {
                        // e.printStackTrace();
                        mailListDemo.setMailStatus(2);
                        mailListDemo.setRemarks("未发送成功");
                        mailListSerivice.save(mailListDemo);
                        log.error(e+"-----邮件未发出去");

                    }
                }
            }
        }
        System.out.println("#########################>>>>>>>>>>>>>>>Q4使用管理员发送邮件 sync4------"+System.currentTimeMillis());
    }




}

