package com.snowy.thinkbox.config;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

@ComponentScan("com.snowy")
@SpringBootApplication
@MapperScan("com.snowy.thinkbox.mapper")
public class ThinkBoxApplication {
    private static final Logger LOG = LoggerFactory.getLogger(ThinkBoxApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ThinkBoxApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！");
        LOG.info("地址：\thttp://127.0.0.1:{}",env.getProperty("server.port"));
    }

}
