package com.snowy.thinkbox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.163.com");  // SMTP 服务器地址
        mailSender.setPort(465);  // SMTP 端口

        // 设置邮件认证信息
        mailSender.setUsername("ThinkBoxAsis@163.com");
        mailSender.setPassword("xxx");//这里改一下，不是密码，是授权码

        // 配置邮件属性
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");  // 启用SSL加密
        props.put("mail.smtp.starttls.enable", "true");  // 启用TLS加密
        props.put("mail.debug", "true");


        return mailSender;
    }
}

