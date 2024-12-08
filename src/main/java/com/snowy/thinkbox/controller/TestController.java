package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.domain.Test;
import com.snowy.thinkbox.service.TestService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
public class TestController {
    @Resource
    private TestService testService;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);


    @RequestMapping("/hello")
    public String hello(){
        return "Hello World!";
    }

    @PostMapping("/hello/post")
    public String postHello(String name){
        return "Hello World!"+name;
    }

    @GetMapping("/test/list")
    public List<Test> List(){
        return testService.list();
    }

//    @RequestMapping("/redis/set/{key}/{value}")
//    public String setRedis(@PathVariable  Long key,@PathVariable String value){
//        redisTemplate.opsForValue().set(key,value);
//        return "success";
//    }
//
//    @RequestMapping("/redis/get/{key}")
//    public String getRedis(@PathVariable  Long key){
//        return (String) redisTemplate.opsForValue().get(key);
//    }

    //测试redis中是否成功存入数据
    @RequestMapping("/redis/set/{key}/{value}")
    public String setRedis(@PathVariable  Long key,@PathVariable String value){
        redisTemplate.opsForValue().set(key,value,3600, TimeUnit.SECONDS);
        log.info("redis存入数据成功,key:{},value:{}",key,value);
        return "success";
    }

    //测试redis中是否成功取出数据
    @RequestMapping("/redis/get/{key}")
    public String getRedis(@PathVariable  Long key){
        String value = (String) redisTemplate.opsForValue().get(key);
        log.info("redis取出数据成功",key,value);
        return value;
    }

}
