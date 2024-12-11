package com.snowy.thinkbox.utils;


import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    //使用JavaMailSender发送邮件
    @Resource
    private JavaMailSender mailSender;

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

    public void sendEmail(String to,  String url) {

//        //创建一个简单邮件消息
//        SimpleMailMessage message = new SimpleMailMessage();
//        //设置收件人
//        message.setTo(to);
//        //设置主题
//        message.setSubject("激活账号");
//        //设置内容
//        message.setText("请点击链接激活账号：" + url);
//        //发送邮件
//        javaMailSender.send(message);
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText("请点击链接激活账号：" + url, true);
            helper.setTo(to);
            helper.setSubject("激活账号");
            helper.setFrom("ThinkBoxAsis@163.com");
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }



}


