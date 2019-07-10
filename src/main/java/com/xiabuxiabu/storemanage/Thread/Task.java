//package com.xiabuxiabu.storemanage.Thread;
//
//import com.xiabuxiabu.storemanage.emailsend.EMailTask;
//import com.xiabuxiabu.storemanage.entity.store.MailList;
//import com.xiabuxiabu.storemanage.entity.user.User;
//import com.xiabuxiabu.storemanage.publicutils.EMailProperties;
//import com.xiabuxiabu.storemanage.service.store.MailListSerivice;
//import com.xiabuxiabu.storemanage.service.user.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.AsyncResult;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.concurrent.Future;
//
//@Component
//public class Task {
//    @Autowired
//    private MailListSerivice mailListSerivice;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private EMailTask eMailTask;
//    @Autowired
//    private EMailProperties eMailProperties;
//
//
//    @Async
//    public Future<String> doTaskOne() throws Exception {
//        System.out.println("有市场IT发送邮件");
//
//        System.out.println("开始做任务一");
//        long start = System.currentTimeMillis();
//
//        List<String> marketNameList = mailListSerivice.marketList();
//        System.out.println("市场数量-----》"+marketNameList.size());
//
//        for (int i = 0; i < marketNameList.size(); i++) {
//            String marketName = marketNameList.get(i);
//
//            List<MailList> mailListList = mailListSerivice.findByMarketName(marketName);
//            List<User> userList = userService.findByMarketName(marketName);
//
//            if (mailListList.size() != 0 && userList.size() != 0) {
//
//                System.out.println("市场基本信息:市场名称" + marketName + ",门店数：" + mailListList.size() + ",人员数：" + userList.size());
//
//                for (int j = 0; j < mailListList.size(); j++) {
//                    MailList mailListDemo = mailListList.get(j);
//                    if (mailListDemo.getMailStatus() == 0 && mailListDemo.getStoreStatus().equals("待选择")) {
//                        StringBuffer content = new StringBuffer();
//                        content.append("<html><head></head>");
//                        content.append("<body><div><h2>门店设备添加通知</h2>" +
//                                "亲爱的用户：您好！门店：" + mailListDemo.getStoreName() + "(" + mailListDemo.getStoreCode() + ")基本" +
//                                "信息已经创建成功，请您尽快添加相应的宽带以及设备信息。如已处理请忽略。谢谢！</div>");
//                        content.append("<div><span style ='float: right;'>总部资讯</span></div>");
//                        content.append("</body></html>");
//                        String mailAddress = "";
//                        for (int k = 0; k < userList.size(); k++) {
//                            mailAddress += userList.get(k).getMail() + ",";
//                        }
//                        String[] allSend = mailAddress.split(",");
//                        try {
//                            eMailTask.sendHtmlMail(eMailProperties.getNickname(),allSend,eMailProperties.getAdditems(),content.toString(),
//                                    eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
//                            mailListDemo.setMailStatus(1);
//                            mailListDemo.setRemarks("发送成功");
//                            mailListSerivice.save(mailListDemo);
//                            Thread.sleep(10000);
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            mailListDemo.setMailStatus(0);
//                            mailListDemo.setRemarks("未发送成功");
//                            mailListSerivice.save(mailListDemo);
//                            Thread.sleep(10000);
//                        }
//                    }
//
//                }
//            }
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("完成任务一，耗时：" + (end - start) + "毫秒");
//        return new AsyncResult<>("任务一完成");
//    }
//    @Async
//    public Future<String> doTaskTwo() throws Exception {
//        System.out.println("开始做任务一");
//        long start = System.currentTimeMillis();
//        Thread.sleep(10000);
//        long end = System.currentTimeMillis();
//        System.out.println("完成任务二，耗时：" + (end - start) + "毫秒");
//        return new AsyncResult<>("任务二完成");
//    }
//
//}
