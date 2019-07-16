package com.xiabuxiabu.storemanage.emailsend;

import com.sun.mail.util.MailSSLSocketFactory;
import org.jboss.logging.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
@Component
public class EMailTask {

    private static Logger log    = Logger.getLogger(EMailTask.class);
    //添加邮件的密抄列表
    private static List<String> michao = new LinkedList<>();
    //添加抄送列表
    private static List<String> chaosong = new LinkedList<>();
    public static void sendHtmlMail(String from, String[] to, String subject, String text, String host, String username, String password) throws Exception {
        //设置服务器验证信息
        Properties prop = new Properties();
        prop.setProperty("mail.smtp.auth", "true");
        prop.setProperty("mail.smtp.timeout", "25000");
        prop.setProperty("mail.smtp.connectiontimeout", "10000");
        prop.setProperty("mail.smtp.writetimeout", "10000");
        prop.setProperty("mail.transport.protocol","smtp");
        MailSSLSocketFactory sf = new MailSSLSocketFactory();// SSL加密
        sf.setTrustAllHosts(true); // 设置信任所有的主机
        prop.setProperty("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        //密超收件人的邮箱
        michao.add("liuyun_1002@163.com");
        //设置邮件内容
        JavaMailSenderImpl javaMailSend = new JavaMailSenderImpl();
        MimeMessage message = javaMailSend.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
        String nick = MimeUtility.encodeText(from);//设置昵称
        messageHelper.setFrom(new InternetAddress("资讯技术部" + " <" + username + ">"));// 邮件发送者
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(text, true);
        if(michao!=null){
            InternetAddress[] bccAddress = new InternetAddress[michao.size()];
            for (int i = 0; i <michao.size() ; i++) {
                String emailAddress = michao.get(i);
               // new InternetAddress(emailAddress);
                bccAddress[i] = new InternetAddress(emailAddress);
            }
            message.setRecipients(Message.RecipientType.BCC,bccAddress);
        }
        //设置邮件服务器登录信息
        javaMailSend.setHost(host);
        javaMailSend.setUsername(username);
        javaMailSend.setPassword(password);
        javaMailSend.setJavaMailProperties(prop);
        javaMailSend.send(message);
    }

}
/**
 * 添加抄送收件人的邮箱
 * 在List中添加抄送人的邮箱的地址
 * List<String> list = new ArrayList<>();
 * if(cc != null) {
 *             InternetAddress[] ccAddress = new InternetAddress[cc.size()];
 *             for(int k = 0;k<cc.size();k++){
 *                 String emailAddress = cc.get(k);
 *                 new InternetAddress(emailAddress);
 *                 ccAddress[k]=new InternetAddress(emailAddress);
 *             }
 *             //添加密抄人
 *             message.addRecipients(Message.RecipientType.CC, ccAddress);
 *         }
 */

