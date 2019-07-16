package com.xiabuxiabu.storemanage.emailsend;

import com.sun.mail.util.MailSSLSocketFactory;
import org.jboss.logging.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
;import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Properties;

public class MailUtil {

//    private static Logger log    = Logger.getLogger(MailUtil.class);
//    public static void sendHtmlMail() {
//        try{
//            String from = "总部资讯技术部";//发件人昵称展示   *
//            String[] to ={"2589125812@qq.com","648221745@qq.com"};//接收邮箱
//            String subject = "邮件主题";//邮件主题   *
//            String text = "邮件内容";
//            String host = "smtphz.qiye.163.com";//163企业邮箱smtp   *
//            String username = "yun.liu@xiabu.com";//企业邮箱   *
//            String password= "Abcd6148";//企业邮箱密码*
//
//            //设置服务器验证信息
//            Properties prop = new Properties();
//            prop.setProperty("mail.smtp.auth", "true");
//            prop.setProperty("mail.smtp.timeout", "25000"); // 加密端口(ssl)  可通过 https://qiye.163.com/help/client-profile.html 进行查询
//
//            MailSSLSocketFactory sf = new MailSSLSocketFactory();// SSL加密
//            sf.setTrustAllHosts(true); // 设置信任所有的主机
//
//            prop.put("mail.smtp.ssl.enable", "true");
//            prop.put("mail.smtp.ssl.socketFactory", sf);
//
//            //设置邮件内容
//            JavaMailSenderImpl javaMailSend = new JavaMailSenderImpl();
//            MimeMessage message = javaMailSend.createMimeMessage();
//            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
//            String nick = MimeUtility.encodeText(from);//设置昵称
//            messageHelper.setFrom(new InternetAddress(nick + " <"+username+">"));// 邮件发送者
//            messageHelper.setTo(to);
//            messageHelper.setSubject(subject);
//            messageHelper.setText(text, true);
//
//            //设置邮件服务器登录信息
//            javaMailSend.setHost(host);
//            javaMailSend.setUsername(username);
//            javaMailSend.setPassword(password);
//            javaMailSend.setJavaMailProperties(prop);
//            log.info("maillText：" + text);
//            javaMailSend.send(message);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            log.error(e);
//            e.printStackTrace();
//        }
//
//    }




}
