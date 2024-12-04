package com.snowy.thinkbox.controller;

import com.snowy.thinkbox.domain.Test;
import com.snowy.thinkbox.service.TestService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TestController {
    @Resource
    private TestService testService;
//    @Autowired
//    private RedisTemplate redisTemplate;


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
}
