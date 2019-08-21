package com.xiabuxiabu.storemanage.alrmtask;

import com.xiabuxiabu.storemanage.emailsend.EMailTask;
import com.xiabuxiabu.storemanage.entity.store.MailList;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.publicutils.EMailProperties;
import com.xiabuxiabu.storemanage.service.store.MailListSerivice;
import com.xiabuxiabu.storemanage.service.user.UserService;
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
    private static Logger log    = Logger.getLogger(EmailTaskInfoMsg.class);

    //发送给市场IT,让市场IT及时进行添加相应的设备（每4个小时执行一次）
    @Scheduled(cron = "0 0 0/4 * * ? ")
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
    }
    //发送给管理员的邮件列表(没两个小时执行一次)
   // @Async
    @Scheduled(fixedDelay = 7200000)
    public void  adminRun(){
        //对于管理员拿到所有的待发邮件的信息
        System.out.println("发送管理员定时任务开始执行");
        List<MailList> mailLists = mailListSerivice.findAll();
        List<User> userList = userService.findAll();
        List<User> adminUserList = new ArrayList<>();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);

            if(user.getUserType().getName().equals("系统管理员")){
                adminUserList.add(user);
            }
        }
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
    }
}

