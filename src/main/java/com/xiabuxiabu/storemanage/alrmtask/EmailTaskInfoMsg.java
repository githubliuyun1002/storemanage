package com.xiabuxiabu.storemanage.alrmtask;

import com.xiabuxiabu.storemanage.emailsend.EMailTask;
import com.xiabuxiabu.storemanage.entity.store.MailList;
import com.xiabuxiabu.storemanage.entity.user.User;
import com.xiabuxiabu.storemanage.publicutils.EMailProperties;
import com.xiabuxiabu.storemanage.service.store.MailListSerivice;
import com.xiabuxiabu.storemanage.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    private HttpServletRequest httpServletRequest;
    @Autowired
    private EMailProperties eMailProperties;

    //每两小时执行一次
    //发送给市场IT,让市场IT及时进行添加相应的设备
    @Scheduled(cron = "0 0 0/2 * * ?" )
    public void emailRun(){
        List<MailList> mailList = mailListSerivice.findAll();
        Page<MailList> allPage =mailListSerivice.findAllPage(1,mailList.size(),"");
        String userName = (String) httpServletRequest.getSession().getAttribute("userName");
        //拿到当前登录人(user)
        User user = userService.findByUserName(userName);
        List<User> userList = userService.findAll();
        //判断mailList中的状态，然后发送相应的邮件
        //提醒IT管理人员添加设备
        for (int i = 0; i <allPage.getContent().size() ; i++) {
            MailList mailListDemo = allPage.getContent().get(i);
            StringBuffer content = new StringBuffer();
            if(mailListDemo.getMailStatus()==1&&mailListDemo.getStoreStatus().equals("待选择")){
                 content.append("<html><head></head>");
                 content.append("<body><div><h2>门店设备添加通知</h2>" +
                         "亲爱的用户：您好！门店："+mailListDemo.getStoreName()+"("+mailListDemo.getStoreCode()+")基本" +
                         "信息已经创建成功，请您尽快添加相应的宽带以及设备信息。如已处理请忽略。谢谢！</div>");
                 content.append("<div><span style ='float: right;'>总部资讯</span></div>");
                 content.append("</body></html>");
            }
            String[] allSend = {user.getMail()};
            try {
                eMailTask.sendHtmlMail(eMailProperties.getNickname(),allSend,eMailProperties.getAdditems(),content.toString(),
                        eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //提醒管理员进行审批(所有的Store均需要进行审批)
        for (int i = 0; i < mailList.size(); i++) {
            MailList mailListAdmin = mailList.get(i);
            StringBuffer contentAdmin = new StringBuffer();
            if(mailListAdmin.getMailStatus()==2&&mailListAdmin.getStoreStatus().equals("待审批")){
                contentAdmin.append("<html><head></head>");
                contentAdmin.append("<body><div><h2>门店设备审批通知</h2>" +
                        "亲爱的用户：您好！门店："+mailListAdmin.getStoreName()+"("+mailListAdmin.getStoreCode()+")设备信息" +
                        "已经添加成功，请您尽快进行审批。如已处理请忽略。谢谢！</div>");
                contentAdmin.append("<div><span style ='float: right;'>总部资讯</span></div>");
                contentAdmin.append("</body></html>");
            }
            String[] sendPeople = new String[userList.size()];
            for (int j = 0; j <userList.size() ; j++) {
                User userDemo = userList.get(j);
                if(userDemo.getUserType().getName().equals("系统管理员")){
                    sendPeople[j]=userDemo.getMail();
                }
            }
            try {
                eMailTask.sendHtmlMail(eMailProperties.getNickname(),sendPeople,eMailProperties.getCheckitems(),contentAdmin.toString(),
                        eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        //拿到所有的门店(Page)
        //门店没有审核通过，需要需要市场IT进行修改的门店
        for (int i = 0; i <allPage.getContent().size() ; i++) {
            MailList mailListCheck = allPage.getContent().get(i);
            StringBuffer content = new StringBuffer();
            if(mailListCheck.getMailStatus()==3&&mailListCheck.getStoreStatus().equals("待调整")){
                content.append("<html><head></head>");
                content.append("<body><div><h2>门店设备修改通知</h2>" +
                        "亲爱的用户：您好！门店："+mailListCheck.getStoreName()+"("+mailListCheck.getStoreCode()+")的基本" +
                        "设备信息经审批需要进行修改。如已处理请忽略。谢谢！</div>");
                content.append("<div><span style ='float: right;'>总部资讯</span></div>");
                content.append("</body></html>");
            }
            String[] allSend = {user.getMail()};
            try {
                eMailTask.sendHtmlMail(eMailProperties.getNickname(),allSend,eMailProperties.getUpdateitems(),content.toString(),
                        eMailProperties.getHost(),eMailProperties.getUsername(),eMailProperties.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
